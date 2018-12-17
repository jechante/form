package com.schinta.service;

import com.schinta.domain.FormSubmit;
import com.schinta.repository.FormSubmitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing FormSubmit.
 */
@Service
@Transactional
public class FormSubmitService {

    private final Logger log = LoggerFactory.getLogger(FormSubmitService.class);

    private final FormSubmitRepository formSubmitRepository;

    public FormSubmitService(FormSubmitRepository formSubmitRepository) {
        this.formSubmitRepository = formSubmitRepository;
    }

    /**
     * Save a formSubmit.
     *
     * @param formSubmit the entity to save
     * @return the persisted entity
     */
    public FormSubmit save(FormSubmit formSubmit) {
        log.debug("Request to save FormSubmit : {}", formSubmit);        return formSubmitRepository.save(formSubmit);
    }

    /**
     * Get all the formSubmits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FormSubmit> findAll(Pageable pageable) {
        log.debug("Request to get all FormSubmits");
        return formSubmitRepository.findAll(pageable);
    }


    /**
     * Get one formSubmit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<FormSubmit> findOne(Long id) {
        log.debug("Request to get FormSubmit : {}", id);
        return formSubmitRepository.findById(id);
    }

    /**
     * Delete the formSubmit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FormSubmit : {}", id);
        formSubmitRepository.deleteById(id);
    }
}
