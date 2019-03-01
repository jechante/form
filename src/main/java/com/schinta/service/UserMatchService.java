package com.schinta.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.util.StringUtil;
import com.schinta.domain.*;
import com.schinta.domain.enumeration.FormyType;
import com.schinta.repository.AlgorithmRepository;
import com.schinta.repository.UserDemandRepository;
import com.schinta.repository.UserMatchRepository;
import com.schinta.repository.UserPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing UserMatch.
 */
@Service
@Transactional
//@SelectBeforeUpdate
public class UserMatchService {

    private final Logger log = LoggerFactory.getLogger(UserMatchService.class);

    private final UserMatchRepository userMatchRepository;
    private final UserPropertyRepository userPropertyRepository;
    private final UserDemandRepository userDemandRepository;
    private final AlgorithmRepository algorithmRepository;

    @Autowired
    private EntityManager entityManager;

    private int baseScore = 50; // 满足过滤条件的基础分 todo 应该存到算法或者其他配置中

    ObjectMapper mapper = new ObjectMapper(); //转换器

    public UserMatchService(UserMatchRepository userMatchRepository,
                            UserPropertyRepository userPropertyRepository,
                            UserDemandRepository userDemandRepository,
                            AlgorithmRepository algorithmRepository) {
        this.userMatchRepository = userMatchRepository;
        this.userPropertyRepository = userPropertyRepository;
        this.userDemandRepository = userDemandRepository;
        this.algorithmRepository = algorithmRepository;
    }

    /**
     * Save a userMatch.
     *
     * @param userMatch the entity to save
     * @return the persisted entity
     */
    public UserMatch save(UserMatch userMatch) {
        log.debug("Request to save UserMatch : {}", userMatch);
        return userMatchRepository.save(userMatch);
    }

    /**
     * Get all the userMatches.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserMatch> findAll(Pageable pageable) {
        log.debug("Request to get all UserMatches");
        return userMatchRepository.findAll(pageable);
    }

    /**
     * Get all the UserMatch with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<UserMatch> findAllWithEagerRelationships(Pageable pageable) {
        return userMatchRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one userMatch by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserMatch> findOne(Long id) {
        log.debug("Request to get UserMatch : {}", id);
        return userMatchRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the userMatch by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserMatch : {}", id);
        userMatchRepository.deleteById(id);
    }

    // 计算效用矩阵 同存储userProperty一样，不确定userMatch是insert还是update
    // todo 判断是否启动了批处理
    public void computeUserToOthers(FormSubmit formSubmit) throws IOException {
        WxUser wxUser = formSubmit.getWxUser();
        computeUserToOthers(wxUser);
        formSubmit.setComputed(true);
        entityManager.merge(formSubmit);
    }

    public void computeUserToOthers(WxUser wxUser) throws IOException {
        // 获取默认算法
        Algorithm algorithm = algorithmRepository.findEnabledOneWithEagerRelationships().get();
        // 获取用户与其他所有用户已有的效用结果
        List<UserMatch> userMatches = userMatchRepository.findAllByWxUserAndAlgorithm(wxUser, algorithm);
        // 获取所有用户的属性及需求（由于算法已经急加载出了计算所需的BaseProperty，因此猜测下面的manyToOne到BaseProperty的关系会自动补上）
        List<UserProperty> allProperties = this.userPropertyRepository.findAllActiveWithUser();
        List<UserDemand> allDemands = this.userDemandRepository.findAllActiveWithUser();
        // 按用户分组
        Map<WxUser, List<UserProperty>> allPropertiesMap = allProperties.stream().collect(Collectors.groupingBy(userProperty -> userProperty.getWxUser(),
            Collectors.toList()));
        Map<WxUser, List<UserDemand>> allDemandsMap = allDemands.stream().collect(Collectors.groupingBy(userDemand -> userDemand.getWxUser(),
            Collectors.toList()));
        // 将wxUser单独拿出来
        List<UserProperty> userProperties = allPropertiesMap.remove(wxUser);
        List<UserDemand> userDemands = allDemandsMap.remove(wxUser);
        // 转化成Map，BaseProperty可以作key，因为按id实现了equals和hashCode方法，不论其是否初始化，都认为是相等的
        Map<BaseProperty, UserProperty> userPropertyMap = userPropertyMap(userProperties);
        Map<BaseProperty, UserDemand> userDemandMap = userDemandMap(userDemands);

        // 计算目前可能的最大相互效用分
        int maxScore = algorithm.getScoreProperties().stream().collect(Collectors.summingInt(BaseProperty::getPropertyMaxScore)) + baseScore;

        // 首先获取所有其他用户(考虑部分用户只填了属性或者只填了需求的情况)
        Set<WxUser> allUsers = new HashSet<>(); // 需要new，否则直接用allPropertiesMap.keySet()返回的Set去add会报java.lang.UnsupportedOperationException错误
        allUsers.addAll(allPropertiesMap.keySet());
        allUsers.addAll(allDemandsMap.keySet());

        // 计算效用分
        // 批处理（主要是防止内存溢出）
        int batchSize = 25;
        int i = 0; // set循环的index记录变量
        for (WxUser toUser : allUsers) {
            if ( i > 0 && i % batchSize == 0 ) {
                //flush a batch of inserts and release memory
                entityManager.flush();
                entityManager.clear();
            }
            List<UserProperty> toUserProperties = allPropertiesMap.get(toUser);
            List<UserDemand> toUserDemands = allDemandsMap.get(toUser);
            Map<BaseProperty, UserProperty> toUserPropertyMap = userPropertyMap(toUserProperties);
            Map<BaseProperty, UserDemand> toUserDemandMap = userDemandMap(toUserDemands);

//            if (wxUser.getId().compareTo(toUser.getId()) <= 0) { // wxUser是userA
                i = i + computeUserAToUserB(wxUser, toUser, algorithm, userMatches, maxScore, userPropertyMap, userDemandMap, toUserPropertyMap, toUserDemandMap);
//            } else { // wxUser是userB
//                i = i + computeUserAToUserB(toUser, wxUser, algorithm, userMatches, maxScore, toUserPropertyMap, toUserDemandMap, userPropertyMap, userDemandMap);
//            }

        }
    }

    private Map<BaseProperty, UserProperty> userPropertyMap(List<UserProperty> userProperties) {
        Map<BaseProperty, UserProperty> userPropertyMap = null;
        if (userProperties != null) {
            userPropertyMap = userProperties.stream().collect(Collectors.toMap(UserProperty::getBase, userProperty -> userProperty));
        }
        return userPropertyMap;
    }

    private Map<BaseProperty, UserDemand> userDemandMap(List<UserDemand> userDemands) {
        Map<BaseProperty, UserDemand> userDemandMap = null;
        if (userDemands != null) {
            userDemandMap = userDemands.stream().collect(Collectors.toMap(UserDemand::getBase, userDemand -> userDemand));
        }
        return userDemandMap;
    }

    // 如果两者有一方得分不为0，则返回1，否则返回0
    // 该方法已经考虑了UserMatch结果userA和userB的无方向性
    private int computeUserAToUserB(WxUser wxUser, WxUser toUser, Algorithm algorithm, List<UserMatch> userMatches, int maxScore,
                                    Map<BaseProperty, UserProperty> userPropertyMap, Map<BaseProperty, UserDemand> userDemandMap, Map<BaseProperty, UserProperty> toUserPropertyMap, Map<BaseProperty, UserDemand> toUserDemandMap) throws IOException {

        // 以该用户需求对其他用户属性打分，即计算scoreAtoB
        float scoreAtoB = scoreOnOneSide(userDemandMap, toUserPropertyMap, algorithm);
        // 以其他用户需求对该用户属性打分，即计算scoreBtoA
        float scoreBtoA = scoreOnOneSide(toUserDemandMap, userPropertyMap, algorithm);
        if (scoreAtoB != 0 || scoreBtoA != 0) { // 如果有一方得分不为0，则将结果存库 todo 如果有1方得分为0，是否还要推送（需要看过滤条件是否是强过滤条件，例如身高不是强过滤、性别是强过滤）
            UserMatch userMatch;
            String type;

            UserMatch atoB = new UserMatch();
            atoB.setAlgorithm(algorithm);
            atoB.setUserA(wxUser);
            atoB.setUserB(toUser);

            UserMatch btoA = new UserMatch();
            btoA.setAlgorithm(algorithm);
            btoA.setUserA(toUser);
            btoA.setUserB(wxUser);

            int indexAtoB = userMatches.indexOf(atoB);
            int indexBtoA = userMatches.indexOf(btoA);

            if (indexAtoB != -1) { // 如果已经有该记录，且user为A，toUser为B
                type = "AToB";
                userMatch = userMatches.get(indexAtoB);
            } else if (indexBtoA != -1) { // 如果已经有该记录，且user为B，toUser为A
                type = "BToA";
                userMatch = userMatches.get(indexBtoA);
            } else { // 如果没有该记录
                type = "new";
                userMatch = atoB; // 新记录按A、B存
            }
            if (type.equals("new") || type.equals("AToB")) {
                userMatch.setScoreAtoB(scoreAtoB);
                userMatch.setScoreBtoA(scoreBtoA);
            } else if (type.equals("BToA")){
                userMatch.setScoreAtoB(scoreBtoA);
                userMatch.setScoreBtoA(scoreAtoB);
            }

            // 获取性别
            BaseProperty sex = algorithm.getFilterProperties().stream().filter(baseProperty -> baseProperty.getPropertyName().equals("性别")).findAny().get(); // todo 性别一定要是过滤属性（必填属性？）
            String sexA = readSingleValue(userPropertyMap.get(sex));
            String sexB = readSingleValue(toUserPropertyMap.get(sex));
            float total;
            float genderWeight = algorithm.getGenderWeight(); // 需求权重
            if (sexA.equals("男") && sexB.equals("女")) {
                total = (1-genderWeight)*scoreAtoB + genderWeight*scoreBtoA;
            } else if (sexA.equals("女") && sexB.equals("男")) {
                total = genderWeight*scoreAtoB + (1-genderWeight)*scoreBtoA;
            } else {
                total = (scoreAtoB+scoreBtoA)/2; // (float) (0.5*scoreAtoB + 0.5*scoreBtoA);
            }
            userMatch.setScoreTotal(total);
            userMatch.setRatio(total/maxScore);
            entityManager.persist(userMatch); // 这里merge也可以，但感觉尽量merge尽量还是用于detached的实体，其余情况用persist更高校
            return 1;
        }
        return 0;
    }


    private boolean matchLocation(UserDemand userDemand, UserProperty toUserProperty) throws IOException {
        Map<String, String> userLocation = mapper.readValue(userDemand.getPropertyValue(), Map.class); // 需求
        Map<String, String> toUserLocation = mapper.readValue(toUserProperty.getPropertyValue(), Map.class);
        if (StringUtil.isNullOrEmpty(userLocation.get("district"))) {
            if (StringUtil.isNullOrEmpty(userLocation.get("city"))) { // 如果只填到省
                return (userLocation.get("province")).equals(
                    toUserLocation.get("province")
                );
            } else { // 如果填到了市
                return (userLocation.get("city") + userLocation.get("province")).equals(
                    toUserLocation.get("city") + toUserLocation.get("province")
                );
            }
        } else { // 如果填到了区/县
            return (userLocation.get("district") + userLocation.get("city") + userLocation.get("province")).equals(
                toUserLocation.get("district") + toUserLocation.get("city") + toUserLocation.get("province")
            );
        }

    }

    // 性别（取向）、体型等
    private boolean matchOneToMany(UserDemand userDemand, UserProperty toUserProperty) throws IOException {
        List demands = mapper.readValue(userDemand.getPropertyValue(), List.class); // 需求方是多
        Object property = readSingleValue(toUserProperty);
        for (Object demand : demands) {
            if (demand.equals(property)) {
                return true;
            }
        }
        return false;
    }

    // 一对一
    private boolean matchOneToOne(UserDemand userDemand, UserProperty toUserProperty) throws IOException {
        Object demand = readSingleValue(userDemand);
        Object property = readSingleValue(toUserProperty);
        if (demand.equals(property)) {
            return true;
        }
        return false;
    }

    // 读取单值属性或需求
    private String readSingleValue(PropertyValue propertyValue) throws IOException {
        if (propertyValue == null) return null;
        Object propertyObj = mapper.readValue(propertyValue.getPropertyValue(), Object.class);
        Object property = null;
        if (propertyObj instanceof String) { // 如果是单选框
            property = propertyObj;
        } else if (propertyObj instanceof List) { // 如果是多选框
            property = ((ArrayList) propertyObj).get(0);
        }
        return (String) property;
    }

//    // 性别（取向）、体型等
//    private int scoreOneToMany(UserDemand userDemand, UserProperty toUserProperty) throws IOException {
//        List demands = mapper.readValue(userDemand.getPropertyValue(), List.class); // 需求方是多
//        Object propertyObj = mapper.readValue(toUserProperty.getPropertyValue(), Object.class);
//        Object property = null;
//        if (propertyObj instanceof String) { // 如果是单选框
//            property = propertyObj;
//        } else if (propertyObj instanceof List) { // 如果是多选框
//            property = ((ArrayList)propertyObj).get(0);
//        }
//        for (Object demand: demands) {
//            if (demand.equals(property)) {
//                return toUserProperty.getBase().getPropertyScore();
//            }
//        }
//        return 0;
//    }

    // todo 暂时只支持所填的数字和范围均为整数的情况，范围只支持“-”为分隔符
    private boolean matchOneToRange(UserDemand userDemand, UserProperty toUserProperty) throws IOException {
        // range即需求侧为文本
        String demand = mapper.readValue(userDemand.getPropertyValue(), String.class);
        int[] demandMinMax = Arrays.stream(demand.split("-")).mapToInt(str -> Integer.valueOf(str)).toArray();
        // 属性侧
        int property = -1;
        if (toUserProperty.getBase().getPropertyName().equals("年龄")) { // 年龄需要特殊处理，这里需要急加载BaseProperty
            String birthStr = mapper.readValue(toUserProperty.getPropertyValue(), String.class);
            int[] birthArray = Arrays.stream(birthStr.split("-")).mapToInt(str -> Integer.valueOf(str)).toArray();
            LocalDate birthDate = LocalDate.of(birthArray[0], birthArray[1], birthArray[2]);
            property = (int) birthDate.until(LocalDate.now(), ChronoUnit.YEARS);
        } else {
            property = mapper.readValue(toUserProperty.getPropertyValue(), Integer.class);
        }
        return property > demandMinMax[0] && property < demandMinMax[1];
    }

    // 专业等
    private boolean matchManyToMany(UserDemand userDemand, UserProperty toUserProperty) throws IOException {
        List demands = mapper.readValue(userDemand.getPropertyValue(), List.class);
        List properties = mapper.readValue(toUserProperty.getPropertyValue(), List.class);

        for (Object demand : demands) {
            for (Object property : properties) {
                if (property.equals(demand)) {
                    return true;
                }
            }
        }
        return false;

    }

    private int scoreManyToMany(UserDemand userDemand, UserProperty toUserProperty/*, BaseProperty baseProperty*/) throws IOException {
        int score = 0;
        List demands = mapper.readValue(userDemand.getPropertyValue(), List.class);
        List properties = mapper.readValue(toUserProperty.getPropertyValue(), List.class);

        for (Object demand : demands) {
            for (Object property : properties) {
                if (property.equals(demand)) {
                    score += toUserProperty.getBase().getPropertyScore();
                }
            }
        }
        if (score == 0) {
            return 0;
        } else {
            int max = toUserProperty.getBase().getPropertyMaxScore();
            return score > max ? max : score;
        }
    }

    // 以一方的需求对另一方属性打分
    private float scoreOnOneSide (Map<BaseProperty, UserDemand> userDemandMap, Map<BaseProperty, UserProperty> toUserPropertyMap, Algorithm algorithm) throws IOException {
        float filterRate;
        int score = baseScore;
        if (userDemandMap == null || userDemandMap.size() == 0) { // 如果没有填需求
            filterRate = 0;
        } else {
            // 先计算过滤条件是否满足
            filterRate = 1; // 还是用系数而不是用Boolean判断是否满足过滤条件，如果是0则不满足，如果非0则满足，且最后得分需要乘以该系数
            for (BaseProperty baseProperty : algorithm.getFilterProperties()) {
                // 用户需求与其他用户属性
                UserDemand userDemand = userDemandMap.get(baseProperty);
                UserProperty toUserProperty = toUserPropertyMap.get(baseProperty);
                if (userDemand != null) { // 仅考虑需求不为空
                    if (toUserProperty == null) { // 如果属性为空，则认为不满足配对条件
                        filterRate = 0;
                        break;
                    } else {
                        if (baseProperty.getFormyType() == FormyType.LOCATION) { // 如果是当前位置等
                            // 方案一：采用微信位置信息
//                        if (wxUser.getWxCountry().equals(toUser.getWxCountry())){
//                            if (wxUser.getWxProvince().equals(toUser.getWxProvince())){
//                                if (wxUser.getWxCity().equals(toUser.getWxCity())) { // 如果市相同
//                                    filterRate = 1;
//                                } else { // 如果省相同
//                                    filterRate = 0.8;
//                                }
//                            } else { // 如果国家相同
//                                filterRate = 0.64;
//                            }
//                        } else { // 如果国家不同
//                            filterRate = 0;
//                            break;
//                        }
                            // 方案二：采用表单中的位置信息
                            if (!matchLocation(userDemand, toUserProperty)) {
                                filterRate = 0;
                                break;
                            }
                        } else if (baseProperty.getFormyType() == FormyType.ONETOMANY) { // 如果是性别等
                            if (!matchOneToMany(userDemand, toUserProperty)) {
                                filterRate = 0;
                                break;
                            }
                        } else if (baseProperty.getFormyType() == FormyType.ONETORANGE) { // 如果是年龄、身高等
                            if (!matchOneToRange(userDemand, toUserProperty)) {
                                filterRate = 0;
                                break;
                            }
                        } else if (baseProperty.getFormyType() == FormyType.ONETOONE) { // 如果是一对一，暂时没有
                            if (!matchOneToOne(userDemand, toUserProperty)) {
                                filterRate = 0;
                                break;
                            }
                        } else if (baseProperty.getFormyType() == FormyType.MANYTOMANY) { // 如果是多对多，暂时没有
                            if (!matchManyToMany(userDemand, toUserProperty)) {
                                filterRate = 0;
                                break;
                            }
                        }
                    }
                }
            }
        }
        // 如果满足过滤条件，计算匹配得分
        if (filterRate != 0) {
            for (BaseProperty baseProperty : algorithm.getScoreProperties()) {
                UserDemand userDemand = userDemandMap.get(baseProperty);
                UserProperty toUserProperty = toUserPropertyMap.get(baseProperty);
                if (baseProperty.getFormyType() == FormyType.OTHER) { // 头像等字段特殊处理，只要有则加分
                    if (toUserProperty != null) {
                        score += baseProperty.getPropertyScore();
                    }
                } else {
                    if (userDemand != null && toUserProperty != null) { // 仅需求和属性均不为空时，才计算配对分
                        if (baseProperty.getFormyType() == FormyType.LOCATION) { // 如果是家乡等
                            if (matchLocation(userDemand, toUserProperty)) { // 如果匹配
                                score += baseProperty.getPropertyScore();
                            }
                        } else if (baseProperty.getFormyType() == FormyType.ONETOMANY) { // 如果是体型、学历等
                            if (matchOneToMany(userDemand, toUserProperty)) { // 如果匹配
                                score += baseProperty.getPropertyScore();
                            }
                        } else if (baseProperty.getFormyType() == FormyType.ONETORANGE) { // 如果是外部打分等
                            if (matchOneToRange(userDemand, toUserProperty)) { // 如果匹配
                                score += baseProperty.getPropertyScore();
                            }
                        } else if (baseProperty.getFormyType() == FormyType.MANYTOMANY) { // 如果是专业等
                            score += scoreManyToMany(userDemand, toUserProperty);
                        } else if (baseProperty.getFormyType() == FormyType.ONETOONE) { // 如果是一对一（暂时没有）
                            if (matchOneToOne(userDemand,toUserProperty)) {
                                score += baseProperty.getPropertyScore();
                            }
                        }
                    }
                }
            }
        }
        return filterRate*score;
    }


    public void regenerate() throws IOException {
        // 获取默认算法
        Algorithm algorithm = algorithmRepository.findEnabledOneWithEagerRelationships().get();
        // 获取用户与其他所有用户已有的效用结果
        List<UserMatch> userMatches = userMatchRepository.findAllByAlgorithm(algorithm);
        // 获取所有用户的属性及需求（由于算法已经急加载出了计算所需的BaseProperty，因此猜测下面的manyToOne到BaseProperty的关系会自动补上）
        List<UserProperty> allProperties = this.userPropertyRepository.findAllActiveWithUser();
        List<UserDemand> allDemands = this.userDemandRepository.findAllActiveWithUser();
        // 按用户分组
        Map<WxUser, List<UserProperty>> allPropertiesMap = allProperties.stream().collect(Collectors.groupingBy(userProperty -> userProperty.getWxUser(),
            Collectors.toList()));
        Map<WxUser, List<UserDemand>> allDemandsMap = allDemands.stream().collect(Collectors.groupingBy(userDemand -> userDemand.getWxUser(),
            Collectors.toList()));

        // 计算目前可能的最大相互效用分
        int maxScore = algorithm.getScoreProperties().stream().collect(Collectors.summingInt(BaseProperty::getPropertyMaxScore)) + baseScore;

        // 首先获取所有用户(考虑部分用户只填了属性或者只填了需求的情况)
        Set<WxUser> allUsersSet = new HashSet<>(); // 需要先用Set添加然后转成list，直接用list添加会有问题
        allUsersSet.addAll(allPropertiesMap.keySet());
        allUsersSet.addAll(allDemandsMap.keySet());

        List<WxUser> allUsers = allUsersSet.stream().collect(Collectors.toList());

        // 计算效用分
        // 批处理（主要是防止内存溢出）
        int userSize = allUsers.size();
        int batchSize = 25;
        int i = 0; // set循环的index记录变量
        for (int j = 0; j < userSize; j++) {
            WxUser wxUser = allUsers.get(j);
            Map<BaseProperty, UserProperty> userPropertyMap = userPropertyMap(allPropertiesMap.get(wxUser));
            Map<BaseProperty, UserDemand> userDemandMap = userDemandMap(allDemandsMap.get(wxUser));
            for (int k = 0; k < userSize; k++) {
                if (k > j) { // 因为userMatch无方向，因此只需要计算一半
                    WxUser toUser = allUsers.get(k);
                    if ( i > 0 && i % batchSize == 0 ) {
                        //flush a batch of inserts and release memory
                        entityManager.flush();
                        entityManager.clear();
                    }
                    Map<BaseProperty, UserProperty> toUserPropertyMap = userPropertyMap(allPropertiesMap.get(toUser));
                    Map<BaseProperty, UserDemand> toUserDemandMap = userDemandMap(allDemandsMap.get(toUser));
                    i = i + computeUserAToUserB(wxUser, toUser, algorithm, userMatches, maxScore, userPropertyMap, userDemandMap, toUserPropertyMap, toUserDemandMap);
                }
            }
        }
    }
}
