package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.UserProperty;
import com.schinta.service.UserPropertyService;
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
 * REST controller for managing UserProperty.
 */
@RestController
@RequestMapping("/api")
public class UserPropertyResource {

    private final Logger log = LoggerFactory.getLogger(UserPropertyResource.class);

    private static final String ENTITY_NAME = "userProperty";

    private final UserPropertyService userPropertyService;

    public UserPropertyResource(UserPropertyService userPropertyService) {
        this.userPropertyService = userPropertyService;
    }

    /**
     * POST  /user-properties : Create a new userProperty.
     *
     * @param userProperty the userProperty to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userProperty, or with status 400 (Bad Request) if the userProperty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-properties")
    @Timed
    public ResponseEntity<UserProperty> createUserProperty(@Valid @RequestBody UserProperty userProperty) throws URISyntaxException {
        log.debug("REST request to save UserProperty : {}", userProperty);
        if (userProperty.getId() != null) {
            throw new BadRequestAlertException("A new userProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserProperty result = userPropertyService.save(userProperty);
        return ResponseEntity.created(new URI("/api/user-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-properties : Updates an existing userProperty.
     *
     * @param userProperty the userProperty to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userProperty,
     * or with status 400 (Bad Request) if the userProperty is not valid,
     * or with status 500 (Internal Server Error) if the userProperty couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-properties")
    @Timed
    public ResponseEntity<UserProperty> updateUserProperty(@Valid @RequestBody UserProperty userProperty) throws URISyntaxException {
        log.debug("REST request to update UserProperty : {}", userProperty);
        if (userProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserProperty result = userPropertyService.save(userProperty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userProperty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-properties : get all the userProperties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userProperties in body
     */
    @GetMapping("/user-properties")
    @Timed
    public ResponseEntity<List<UserProperty>> getAllUserProperties(Pageable pageable) {
        log.debug("REST request to get a page of UserProperties");
        Page<UserProperty> page = userPropertyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-properties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-properties/:id : get the "id" userProperty.
     *
     * @param id the id of the userProperty to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userProperty, or with status 404 (Not Found)
     */
    @GetMapping("/user-properties/{id}")
    @Timed
    public ResponseEntity<UserProperty> getUserProperty(@PathVariable Long id) {
        log.debug("REST request to get UserProperty : {}", id);
        Optional<UserProperty> userProperty = userPropertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userProperty);
    }

    /**
     * DELETE  /user-properties/:id : delete the "id" userProperty.
     *
     * @param id the id of the userProperty to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-properties/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserProperty(@PathVariable Long id) {
        log.debug("REST request to delete UserProperty : {}", id);
        userPropertyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /user-pictures : get all the pictures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userProperties in body
     */
    @GetMapping("/user-pictures")
    @Timed
    public ResponseEntity<List<UserProperty>> getAllUserPictures(Pageable pageable) {
        log.debug("REST request to get a page of 用户头像");
        Page<UserProperty> page = userPropertyService.findAllUserPictures(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-pictures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
