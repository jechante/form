package com.schinta.service;

import com.schinta.domain.FormSubmit;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing FormSubmit.
 */
public interface FormSubmitService {

    /**
     * Save a formSubmit.
     *
     * @param formSubmit the entity to save
     * @return the persisted entity
     */
    FormSubmit save(FormSubmit formSubmit);

    /**
     * Get all the formSubmits.
     *
     * @return the list of entities
     */
    List<FormSubmit> findAll();


    /**
     * Get the "id" formSubmit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FormSubmit> findOne(Long id);

    /**
     * Delete the "id" formSubmit.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
