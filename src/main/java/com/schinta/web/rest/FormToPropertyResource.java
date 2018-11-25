package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.FormToProperty;
import com.schinta.repository.FormToPropertyRepository;
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
 * REST controller for managing FormToProperty.
 */
@RestController
@RequestMapping("/api")
public class FormToPropertyResource {

    private final Logger log = LoggerFactory.getLogger(FormToPropertyResource.class);

    private static final String ENTITY_NAME = "formToProperty";

    private final FormToPropertyRepository formToPropertyRepository;

    public FormToPropertyResource(FormToPropertyRepository formToPropertyRepository) {
        this.formToPropertyRepository = formToPropertyRepository;
    }

    /**
     * POST  /form-to-properties : Create a new formToProperty.
     *
     * @param formToProperty the formToProperty to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formToProperty, or with status 400 (Bad Request) if the formToProperty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/form-to-properties")
    @Timed
    public ResponseEntity<FormToProperty> createFormToProperty(@Valid @RequestBody FormToProperty formToProperty) throws URISyntaxException {
        log.debug("REST request to save FormToProperty : {}", formToProperty);
        if (formToProperty.getId() != null) {
            throw new BadRequestAlertException("A new formToProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormToProperty result = formToPropertyRepository.save(formToProperty);
        return ResponseEntity.created(new URI("/api/form-to-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /form-to-properties : Updates an existing formToProperty.
     *
     * @param formToProperty the formToProperty to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formToProperty,
     * or with status 400 (Bad Request) if the formToProperty is not valid,
     * or with status 500 (Internal Server Error) if the formToProperty couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/form-to-properties")
    @Timed
    public ResponseEntity<FormToProperty> updateFormToProperty(@Valid @RequestBody FormToProperty formToProperty) throws URISyntaxException {
        log.debug("REST request to update FormToProperty : {}", formToProperty);
        if (formToProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormToProperty result = formToPropertyRepository.save(formToProperty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formToProperty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /form-to-properties : get all the formToProperties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of formToProperties in body
     */
    @GetMapping("/form-to-properties")
    @Timed
    public ResponseEntity<List<FormToProperty>> getAllFormToProperties(Pageable pageable) {
        log.debug("REST request to get a page of FormToProperties");
        Page<FormToProperty> page = formToPropertyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/form-to-properties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /form-to-properties/:id : get the "id" formToProperty.
     *
     * @param id the id of the formToProperty to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formToProperty, or with status 404 (Not Found)
     */
    @GetMapping("/form-to-properties/{id}")
    @Timed
    public ResponseEntity<FormToProperty> getFormToProperty(@PathVariable Long id) {
        log.debug("REST request to get FormToProperty : {}", id);
        Optional<FormToProperty> formToProperty = formToPropertyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(formToProperty);
    }

    /**
     * DELETE  /form-to-properties/:id : delete the "id" formToProperty.
     *
     * @param id the id of the formToProperty to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/form-to-properties/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormToProperty(@PathVariable Long id) {
        log.debug("REST request to delete FormToProperty : {}", id);

        formToPropertyRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
