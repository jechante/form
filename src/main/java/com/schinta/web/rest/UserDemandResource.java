package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.UserDemand;
import com.schinta.service.UserDemandService;
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
 * REST controller for managing UserDemand.
 */
@RestController
@RequestMapping("/api")
public class UserDemandResource {

    private final Logger log = LoggerFactory.getLogger(UserDemandResource.class);

    private static final String ENTITY_NAME = "userDemand";

    private final UserDemandService userDemandService;

    public UserDemandResource(UserDemandService userDemandService) {
        this.userDemandService = userDemandService;
    }

    /**
     * POST  /user-demands : Create a new userDemand.
     *
     * @param userDemand the userDemand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userDemand, or with status 400 (Bad Request) if the userDemand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-demands")
    @Timed
    public ResponseEntity<UserDemand> createUserDemand(@Valid @RequestBody UserDemand userDemand) throws URISyntaxException {
        log.debug("REST request to save UserDemand : {}", userDemand);
        if (userDemand.getId() != null) {
            throw new BadRequestAlertException("A new userDemand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDemand result = userDemandService.save(userDemand);
        return ResponseEntity.created(new URI("/api/user-demands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-demands : Updates an existing userDemand.
     *
     * @param userDemand the userDemand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userDemand,
     * or with status 400 (Bad Request) if the userDemand is not valid,
     * or with status 500 (Internal Server Error) if the userDemand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-demands")
    @Timed
    public ResponseEntity<UserDemand> updateUserDemand(@Valid @RequestBody UserDemand userDemand) throws URISyntaxException {
        log.debug("REST request to update UserDemand : {}", userDemand);
        if (userDemand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserDemand result = userDemandService.save(userDemand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userDemand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-demands : get all the userDemands.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userDemands in body
     */
    @GetMapping("/user-demands")
    @Timed
    public ResponseEntity<List<UserDemand>> getAllUserDemands(Pageable pageable) {
        log.debug("REST request to get a page of UserDemands");
        Page<UserDemand> page = userDemandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-demands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-demands/:id : get the "id" userDemand.
     *
     * @param id the id of the userDemand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userDemand, or with status 404 (Not Found)
     */
    @GetMapping("/user-demands/{id}")
    @Timed
    public ResponseEntity<UserDemand> getUserDemand(@PathVariable Long id) {
        log.debug("REST request to get UserDemand : {}", id);
        Optional<UserDemand> userDemand = userDemandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDemand);
    }

    /**
     * DELETE  /user-demands/:id : delete the "id" userDemand.
     *
     * @param id the id of the userDemand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-demands/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserDemand(@PathVariable Long id) {
        log.debug("REST request to delete UserDemand : {}", id);
        userDemandService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
