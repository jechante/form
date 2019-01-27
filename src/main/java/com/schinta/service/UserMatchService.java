package com.schinta.service;

import com.schinta.domain.*;
import com.schinta.repository.AlgorithmRepository;
import com.schinta.repository.UserDemandRepository;
import com.schinta.repository.UserMatchRepository;
import com.schinta.repository.UserPropertyRepository;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.debug("Request to save UserMatch : {}", userMatch);        return userMatchRepository.save(userMatch);
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

    // 计算效用矩阵 todo 同存储userProperty一样，不确定userMatch是新增数据还是
    public void computeUserToOthers(WxUser wxUser) {
        List<UserProperty> allProperties = this.userPropertyRepository.findAllActiveWithUser();
        List<UserDemand> allDemands = this.userDemandRepository.findAllActiveWithUser();
        // 按用户分组
        Map<WxUser,List<UserProperty>> allPropertiesMap = allProperties.stream().collect(Collectors.groupingBy(userProperty -> userProperty.getWxUser(),
            Collectors.toList()));
        Map<WxUser,List<UserDemand>> allDemandsMap = allDemands.stream().collect(Collectors.groupingBy(userDemand -> userDemand.getWxUser(),
            Collectors.toList()));
        // 将wxUser单独拿出来
        List<UserProperty> userProperties = allPropertiesMap.remove(wxUser);
        List<UserDemand> userDemands = allDemandsMap.remove(wxUser);
        // 获取默认算法
        Algorithm algorithm = algorithmRepository.findEnabledOneWithEagerRelationships().get();
        // 首先获取所有其他用户(考虑部分用户只填了属性或者只填了需求的情况)
        Set<WxUser> allUsers = allPropertiesMap.keySet();
        allUsers.addAll(allDemandsMap.keySet());
        // 以该用户需求对其他用户属性打分
        allUsers.forEach(toUser -> {
            UserMatch userMatch = new UserMatch();
            userMatch.setUserA(wxUser);
            userMatch.setUserB(toUser);
            userMatch.setAlgorithm(algorithm);


        });
    }
}
