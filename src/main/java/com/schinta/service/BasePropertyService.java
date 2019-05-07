package com.schinta.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schinta.domain.BaseProperty;
import com.schinta.domain.UserDemand;
import com.schinta.domain.UserProperty;
import com.schinta.domain.WxUser;
import com.schinta.repository.BasePropertyRepository;
import com.schinta.repository.UserDemandRepository;
import com.schinta.repository.UserPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.schinta.config.Constants.CACHE_SEX;

/**
 * Service Implementation for managing BaseProperty.
 */
@Service
@Transactional
public class BasePropertyService {

    private final Logger log = LoggerFactory.getLogger(BasePropertyService.class);

    private final BasePropertyRepository basePropertyRepository;
    private final UserPropertyRepository userPropertyRepository;
    private final UserDemandRepository userDemandRepository;
    private final FormSubmitService formSubmitService;
    private final UserMatchService userMatchService;

    public BasePropertyService(BasePropertyRepository basePropertyRepository,
                               UserPropertyRepository userPropertyRepository,
                               UserDemandRepository userDemandRepository,
                               FormSubmitService formSubmitService,
                               UserMatchService userMatchService) {
        this.basePropertyRepository = basePropertyRepository;
        this.userPropertyRepository = userPropertyRepository;
        this.userDemandRepository = userDemandRepository;
        this.formSubmitService = formSubmitService;
        this.userMatchService = userMatchService;
    }

    /**
     * Save a baseProperty.
     *
     * @param baseProperty the entity to save
     * @return the persisted entity
     */
    public BaseProperty save(BaseProperty baseProperty) {
        log.debug("Request to save BaseProperty : {}", baseProperty);

        return basePropertyRepository.save(baseProperty);
    }

    /**
     * Get all the baseProperties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BaseProperty> findAll(Pageable pageable) {
        log.debug("Request to get all BaseProperties");
        return basePropertyRepository.findAll(pageable);
    }


    /**
     * Get one baseProperty by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BaseProperty> findOne(Long id) {
        log.debug("Request to get BaseProperty : {}", id);
        return basePropertyRepository.findById(id);
    }

    /**
     * Delete the baseProperty by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BaseProperty : {}", id);
        basePropertyRepository.deleteById(id);
    }

    // 急加载用户属性和需求
    public List<BaseProperty> findUserPropertyDemands(String id) {
        return basePropertyRepository.findUserPropertyDemands(id).stream().map(objects -> {
            BaseProperty baseProperty = (BaseProperty) objects[0];
            Set<UserProperty> proSet = new HashSet(); // 无论objects[1]是否为空，均需要初始化一个set
            if (objects[1] != null) {
                proSet.add((UserProperty) objects[1]);
            }
            baseProperty.setPropertyValues(proSet);
            Set<UserDemand> demSet = new HashSet();
            if (objects[2] != null) {
                demSet.add((UserDemand) objects[2]);
            }
            baseProperty.setDemandValues(demSet);
            return baseProperty;
        }).collect(Collectors.toList());
    }

    // 保存用户属性信息。这里有别于第三方表单只能根据唯一约束（base和wxUser）来判断数据库中是否已有记录，这里可以直接根据是否有id来判断
    public void saveUserProperties(List<BaseProperty> baseProperties) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); //转换器
        for (BaseProperty baseProperty : baseProperties) {
//        baseProperties.forEach(baseProperty -> {
            Set<UserProperty> userProperties = baseProperty.getPropertyValues();
            for (UserProperty userProperty : userProperties) {
//            baseProperty.getPropertyValues().forEach(userProperty -> {
                // 判断是否有值以及是否是填写的有效值（防止填入""、[]这种空值的情况）
//                try {
                Object value = userProperty.getPropertyValue() == null || userProperty.getPropertyValue().equals("") ? userProperty.getPropertyValue() : mapper.readValue(userProperty.getPropertyValue(), Object.class);
                if (formSubmitService.hasValue(value)) {
                    userProperty.setBase(baseProperty);
                    userPropertyRepository.save(userProperty);
                } else if (userProperty.getId() != null) { // 通过小伊后台可以删除用户的某条属性（但是通过第三方表单不可以，因为第三方表单，用户无法查看历史填写记录）
                    userPropertyRepository.delete(userProperty);
                }
//                } catch (IOException e) { // 不能随便catch，否则事务不会回滚
//                    e.printStackTrace();
//                }
            }
//            });
            Set<UserDemand> userDemands = baseProperty.getDemandValues();
            for (UserDemand userDemand : userDemands) {
//                baseProperty.getDemandValues().forEach(userDemand -> {
//                    try {
                Object value = userDemand.getPropertyValue() == null || userDemand.getPropertyValue().equals("") ? userDemand.getPropertyValue() : mapper.readValue(userDemand.getPropertyValue(), Object.class);
                if (formSubmitService.hasValue(value)) {
                    userDemand.setBase(baseProperty);
                    userDemandRepository.save(userDemand);
                } else if (userDemand.getId() != null) { // 通过小伊后台可以删除用户的某条属性（但是通过第三方表单不可以，因为第三方表单，用户无法查看历史填写记录）
                    userDemandRepository.delete(userDemand);
                }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
            }
//                });
        }
//        });

        WxUser wxUser = baseProperties.get(0).getPropertyValues().stream().findAny().map(userProperty -> userProperty.getWxUser()).get();
        // 计算该用户与现有其他用户的效用矩阵
        log.info("计算该用户与现有其他用户的效用矩阵");
        userMatchService.computeUserToOthers(wxUser);
    }

    @Cacheable(cacheNames = CACHE_SEX)
    public BaseProperty findSex() {
        return basePropertyRepository.findSex();
    }
}
