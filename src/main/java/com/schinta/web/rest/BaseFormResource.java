package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.BaseForm;
import com.schinta.repository.BaseFormRepository;
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
 * REST controller for managing BaseForm.
 */
@RestController
@RequestMapping("/api")
public class BaseFormResource {

    private final Logger log = LoggerFactory.getLogger(BaseFormResource.class);

    private static final String ENTITY_NAME = "baseForm";

    private final BaseFormRepository baseFormRepository;

    public BaseFormResource(BaseFormRepository baseFormRepository) {
        this.baseFormRepository = baseFormRepository;
    }

    /**
     * POST  /base-forms : Create a new baseForm.
     *
     * @param baseForm the baseForm to create
     * @return the ResponseEntity with status 201 (Created) and with body the new baseForm, or with status 400 (Bad Request) if the baseForm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/base-forms")
    @Timed
    public ResponseEntity<BaseForm> createBaseForm(@Valid @RequestBody BaseForm baseForm) throws URISyntaxException {
        log.debug("REST request to save BaseForm : {}", baseForm);
        if (baseForm.getId() != null) {
            throw new BadRequestAlertException("A new baseForm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BaseForm result = baseFormRepository.save(baseForm);
        return ResponseEntity.created(new URI("/api/base-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /base-forms : Updates an existing baseForm.
     *
     * @param baseForm the baseForm to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated baseForm,
     * or with status 400 (Bad Request) if the baseForm is not valid,
     * or with status 500 (Internal Server Error) if the baseForm couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/base-forms")
    @Timed
    public ResponseEntity<BaseForm> updateBaseForm(@Valid @RequestBody BaseForm baseForm) throws URISyntaxException {
        log.debug("REST request to update BaseForm : {}", baseForm);
        if (baseForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BaseForm result = baseFormRepository.save(baseForm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, baseForm.getId().toString()))
            .body(result);
    }

    /**
     * GET  /base-forms : get all the baseForms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of baseForms in body
     */
    @GetMapping("/base-forms")
    @Timed
    public ResponseEntity<List<BaseForm>> getAllBaseForms(Pageable pageable) {
        log.debug("REST request to get a page of BaseForms");
        Page<BaseForm> page = baseFormRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/base-forms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /base-forms/:id : get the "id" baseForm.
     *
     * @param id the id of the baseForm to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the baseForm, or with status 404 (Not Found)
     */
    @GetMapping("/base-forms/{id}")
    @Timed
    public ResponseEntity<BaseForm> getBaseForm(@PathVariable Long id) {
        log.debug("REST request to get BaseForm : {}", id);
        Optional<BaseForm> baseForm = baseFormRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(baseForm);
    }

    /**
     * DELETE  /base-forms/:id : delete the "id" baseForm.
     *
     * @param id the id of the baseForm to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/base-forms/{id}")
    @Timed
    public ResponseEntity<Void> deleteBaseForm(@PathVariable Long id) {
        log.debug("REST request to delete BaseForm : {}", id);

        baseFormRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
