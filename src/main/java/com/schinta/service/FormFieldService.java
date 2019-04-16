package com.schinta.service;

import com.schinta.domain.FormField;
import com.schinta.repository.FormFieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing FormField.
 */
@Service
@Transactional
public class FormFieldService {

    private final Logger log = LoggerFactory.getLogger(FormFieldService.class);

    private final FormFieldRepository formFieldRepository;

    public FormFieldService(FormFieldRepository formFieldRepository) {
        this.formFieldRepository = formFieldRepository;
    }

    /**
     * Save a formField.
     *
     * @param formField the entity to save
     * @return the persisted entity
     */
    public FormField save(FormField formField) {
        log.debug("Request to save FormField : {}", formField);        return formFieldRepository.save(formField);
    }

    /**
     * Get all the formFields.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FormField> findAll(Pageable pageable) {
        log.debug("Request to get all FormFields");
        return formFieldRepository.findAll(pageable);
    }


    /**
     * Get one formField by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<FormField> findOne(Long id) {
        log.debug("Request to get FormField : {}", id);
//         return formFieldRepository.findById(id);
         return formFieldRepository.findByIdWithBase(id);
    }

    /**
     * Delete the formField by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FormField : {}", id);
        formFieldRepository.deleteById(id);
    }

    public Page<FormField> findAllByFormId(Pageable pageable, Long formId) {
        return formFieldRepository.findAllByFormId(pageable, formId);
    }
}
