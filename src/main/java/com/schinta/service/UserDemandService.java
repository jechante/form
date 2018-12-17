package com.schinta.service;

import com.schinta.domain.UserDemand;
import com.schinta.repository.UserDemandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing UserDemand.
 */
@Service
@Transactional
public class UserDemandService {

    private final Logger log = LoggerFactory.getLogger(UserDemandService.class);

    private final UserDemandRepository userDemandRepository;

    public UserDemandService(UserDemandRepository userDemandRepository) {
        this.userDemandRepository = userDemandRepository;
    }

    /**
     * Save a userDemand.
     *
     * @param userDemand the entity to save
     * @return the persisted entity
     */
    public UserDemand save(UserDemand userDemand) {
        log.debug("Request to save UserDemand : {}", userDemand);        return userDemandRepository.save(userDemand);
    }

    /**
     * Get all the userDemands.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserDemand> findAll(Pageable pageable) {
        log.debug("Request to get all UserDemands");
        return userDemandRepository.findAll(pageable);
    }


    /**
     * Get one userDemand by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserDemand> findOne(Long id) {
        log.debug("Request to get UserDemand : {}", id);
        return userDemandRepository.findById(id);
    }

    /**
     * Delete the userDemand by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserDemand : {}", id);
        userDemandRepository.deleteById(id);
    }
}
