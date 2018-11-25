package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.UserBase;
import com.schinta.repository.UserBaseRepository;
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
 * REST controller for managing UserBase.
 */
@RestController
@RequestMapping("/api")
public class UserBaseResource {

    private final Logger log = LoggerFactory.getLogger(UserBaseResource.class);

    private static final String ENTITY_NAME = "userBase";

    private final UserBaseRepository userBaseRepository;

    public UserBaseResource(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }

    /**
     * POST  /user-bases : Create a new userBase.
     *
     * @param userBase the userBase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userBase, or with status 400 (Bad Request) if the userBase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-bases")
    @Timed
    public ResponseEntity<UserBase> createUserBase(@Valid @RequestBody UserBase userBase) throws URISyntaxException {
        log.debug("REST request to save UserBase : {}", userBase);
        if (userBase.getId() != null) {
            throw new BadRequestAlertException("A new userBase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserBase result = userBaseRepository.save(userBase);
        return ResponseEntity.created(new URI("/api/user-bases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-bases : Updates an existing userBase.
     *
     * @param userBase the userBase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userBase,
     * or with status 400 (Bad Request) if the userBase is not valid,
     * or with status 500 (Internal Server Error) if the userBase couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-bases")
    @Timed
    public ResponseEntity<UserBase> updateUserBase(@Valid @RequestBody UserBase userBase) throws URISyntaxException {
        log.debug("REST request to update UserBase : {}", userBase);
        if (userBase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserBase result = userBaseRepository.save(userBase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userBase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-bases : get all the userBases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userBases in body
     */
    @GetMapping("/user-bases")
    @Timed
    public ResponseEntity<List<UserBase>> getAllUserBases(Pageable pageable) {
        log.debug("REST request to get a page of UserBases");
        Page<UserBase> page = userBaseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-bases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-bases/:id : get the "id" userBase.
     *
     * @param id the id of the userBase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userBase, or with status 404 (Not Found)
     */
    @GetMapping("/user-bases/{id}")
    @Timed
    public ResponseEntity<UserBase> getUserBase(@PathVariable Long id) {
        log.debug("REST request to get UserBase : {}", id);
        Optional<UserBase> userBase = userBaseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userBase);
    }

    /**
     * DELETE  /user-bases/:id : delete the "id" userBase.
     *
     * @param id the id of the userBase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-bases/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserBase(@PathVariable Long id) {
        log.debug("REST request to delete UserBase : {}", id);

        userBaseRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
