package com.schinta.service;

import com.schinta.domain.UserDemand;
import com.schinta.domain.UserMatch;
import com.schinta.domain.UserProperty;
import com.schinta.domain.WxUser;
import com.schinta.repository.UserDemandRepository;
import com.schinta.repository.UserMatchRepository;
import com.schinta.repository.UserPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing UserMatch.
 */
@Service
@Transactional
public class UserMatchService {

    private final Logger log = LoggerFactory.getLogger(UserMatchService.class);

    private final UserMatchRepository userMatchRepository;
    private final UserPropertyRepository userPropertyRepository;
    private final UserDemandRepository userDemandRepository;

    public UserMatchService(UserMatchRepository userMatchRepository,
                            UserPropertyRepository userPropertyRepository,
                            UserDemandRepository userDemandRepository) {
        this.userMatchRepository = userMatchRepository;
        this.userPropertyRepository = userPropertyRepository;
        this.userDemandRepository = userDemandRepository;
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

    // 计算效用矩阵
    public void computeUserToOthers(WxUser wxUser) {
        List<UserProperty> allProperties = this.userPropertyRepository.findAllActiveWithUser();
        List<UserDemand> allDemands = this.userDemandRepository.findAllActiveWithUser();
        // 按用户分组、剔除wxUser


    }
}
