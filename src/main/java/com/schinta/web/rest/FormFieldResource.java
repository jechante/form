package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.FormField;
import com.schinta.service.FormFieldService;
import com.schinta.web.rest.errors.BadRequestAlertException;
import com.schinta.web.rest.util.HeaderUtil;
import com.schinta.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FormField.
 */
@RestController
@RequestMapping("/api")
public class FormFieldResource {

    private final Logger log = LoggerFactory.getLogger(FormFieldResource.class);

    private static final String ENTITY_NAME = "formField";

    private final FormFieldService formFieldService;

    public FormFieldResource(FormFieldService formFieldService) {
        this.formFieldService = formFieldService;
    }

    /**
     * POST  /form-fields : Create a new formField.
     *
     * @param formField the formField to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formField, or with status 400 (Bad Request) if the formField has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/form-fields")
    @Timed
    public ResponseEntity<FormField> createFormField(@Valid @RequestBody FormField formField) throws URISyntaxException {
        log.debug("REST request to save FormField : {}", formField);
        if (formField.getId() != null) {
            throw new BadRequestAlertException("A new formField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormField result = formFieldService.save(formField);
        return ResponseEntity.created(new URI("/api/form-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /form-fields : Updates an existing formField.
     *
     * @param formField the formField to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formField,
     * or with status 400 (Bad Request) if the formField is not valid,
     * or with status 500 (Internal Server Error) if the formField couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/form-fields")
    @Timed
    public ResponseEntity<FormField> updateFormField(@Valid @RequestBody FormField formField) throws URISyntaxException {
        log.debug("REST request to update FormField : {}", formField);
        if (formField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormField result = formFieldService.save(formField);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formField.getId().toString()))
            .body(result);
    }

    /**
     * GET  /form-fields : get all the formFields.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of formFields in body
     */
    @GetMapping("/form-fields")
    @Timed
    public ResponseEntity<List<FormField>> getAllFormFields(Pageable pageable) {
        log.debug("REST request to get a page of FormFields");
        Page<FormField> page = formFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/form-fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /form-fields/:id : get the "id" formField.
     *
     * @param id the id of the formField to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formField, or with status 404 (Not Found)
     */
    @GetMapping("/form-fields/{id}")
    @Timed
    public ResponseEntity<FormField> getFormField(@PathVariable Long id) {
        log.debug("REST request to get FormField : {}", id);
        Optional<FormField> formField = formFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formField);
    }

    /**
     * DELETE  /form-fields/:id : delete the "id" formField.
     *
     * @param id the id of the formField to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/form-fields/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormField(@PathVariable Long id) {
        log.debug("REST request to delete FormField : {}", id);
        formFieldService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
