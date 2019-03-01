package com.schinta.service;

import com.schinta.domain.UserProperty;
import com.schinta.repository.UserPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing UserProperty.
 */
@Service
@Transactional
public class UserPropertyService {

    private final Logger log = LoggerFactory.getLogger(UserPropertyService.class);

    private final UserPropertyRepository userPropertyRepository;

    public UserPropertyService(UserPropertyRepository userPropertyRepository) {
        this.userPropertyRepository = userPropertyRepository;
    }

    /**
     * Save a userProperty.
     *
     * @param userProperty the entity to save
     * @return the persisted entity
     */
    public UserProperty save(UserProperty userProperty) {
        log.debug("Request to save UserProperty : {}", userProperty);        return userPropertyRepository.save(userProperty);
    }

    /**
     * Get all the userProperties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserProperty> findAll(Pageable pageable) {
        log.debug("Request to get all UserProperties");
        return userPropertyRepository.findAll(pageable);
    }


    /**
     * Get one userProperty by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserProperty> findOne(Long id) {
        log.debug("Request to get UserProperty : {}", id);
        return userPropertyRepository.findById(id);
    }

    /**
     * Delete the userProperty by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserProperty : {}", id);
        userPropertyRepository.deleteById(id);
    }

    public Page<UserProperty> findAllUserPictures(Pageable pageable) {
        return userPropertyRepository.findAllUserPictures(pageable);
    }
}
