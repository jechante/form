package com.schinta.service.impl;

import com.schinta.service.FormSubmitService;
import com.schinta.domain.FormSubmit;
import com.schinta.repository.FormSubmitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing FormSubmit.
 */
@Service
@Transactional
public class FormSubmitServiceImpl implements FormSubmitService {

    private final Logger log = LoggerFactory.getLogger(FormSubmitServiceImpl.class);

    private final FormSubmitRepository formSubmitRepository;

    public FormSubmitServiceImpl(FormSubmitRepository formSubmitRepository) {
        this.formSubmitRepository = formSubmitRepository;
    }

    /**
     * Save a formSubmit.
     *
     * @param formSubmit the entity to save
     * @return the persisted entity
     */
    @Override
    public FormSubmit save(FormSubmit formSubmit) {
        log.debug("Request to save FormSubmit : {}", formSubmit);        return formSubmitRepository.save(formSubmit);
    }

    /**
     * Get all the formSubmits.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormSubmit> findAll() {
        log.debug("Request to get all FormSubmits");
        return formSubmitRepository.findAll();
    }


    /**
     * Get one formSubmit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
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
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormSubmit : {}", id);
        formSubmitRepository.deleteById(id);
    }
}
