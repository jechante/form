package com.schinta.service;

import com.schinta.domain.BaseForm;
import com.schinta.repository.BaseFormRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing BaseForm.
 */
@Service
@Transactional
public class BaseFormService {

    private final Logger log = LoggerFactory.getLogger(BaseFormService.class);

    private final BaseFormRepository baseFormRepository;

    public BaseFormService(BaseFormRepository baseFormRepository) {
        this.baseFormRepository = baseFormRepository;
    }

    /**
     * Save a baseForm.
     *
     * @param baseForm the entity to save
     * @return the persisted entity
     */
    public BaseForm save(BaseForm baseForm) {
        log.debug("Request to save BaseForm : {}", baseForm);        return baseFormRepository.save(baseForm);
    }

    /**
     * Get all the baseForms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BaseForm> findAll(Pageable pageable) {
        log.debug("Request to get all BaseForms");
        return baseFormRepository.findAll(pageable);
    }


    /**
     * Get one baseForm by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BaseForm> findOne(Long id) {
        log.debug("Request to get BaseForm : {}", id);
        return baseFormRepository.findById(id);
    }

    /**
     * Delete the baseForm by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BaseForm : {}", id);
        baseFormRepository.deleteById(id);
    }
}
