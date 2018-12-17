package com.schinta.service;

import com.schinta.domain.BaseProperty;
import com.schinta.repository.BasePropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing BaseProperty.
 */
@Service
@Transactional
public class BasePropertyService {

    private final Logger log = LoggerFactory.getLogger(BasePropertyService.class);

    private final BasePropertyRepository basePropertyRepository;

    public BasePropertyService(BasePropertyRepository basePropertyRepository) {
        this.basePropertyRepository = basePropertyRepository;
    }

    /**
     * Save a baseProperty.
     *
     * @param baseProperty the entity to save
     * @return the persisted entity
     */
    public BaseProperty save(BaseProperty baseProperty) {
        log.debug("Request to save BaseProperty : {}", baseProperty);        return basePropertyRepository.save(baseProperty);
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
}
