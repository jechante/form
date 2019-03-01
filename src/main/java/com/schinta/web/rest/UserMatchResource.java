package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.UserMatch;
import com.schinta.service.UserMatchService;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserMatch.
 */
@RestController
@RequestMapping("/api")
public class UserMatchResource {

    private final Logger log = LoggerFactory.getLogger(UserMatchResource.class);

    private static final String ENTITY_NAME = "userMatch";

    private final UserMatchService userMatchService;

    public UserMatchResource(UserMatchService userMatchService) {
        this.userMatchService = userMatchService;
    }

    /**
     * POST  /user-matches : Create a new userMatch.
     *
     * @param userMatch the userMatch to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userMatch, or with status 400 (Bad Request) if the userMatch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-matches")
    @Timed
    public ResponseEntity<UserMatch> createUserMatch(@RequestBody UserMatch userMatch) throws URISyntaxException {
        log.debug("REST request to save UserMatch : {}", userMatch);
        if (userMatch.getId() != null) {
            throw new BadRequestAlertException("A new userMatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserMatch result = userMatchService.save(userMatch);
        return ResponseEntity.created(new URI("/api/user-matches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-matches : Updates an existing userMatch.
     *
     * @param userMatch the userMatch to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userMatch,
     * or with status 400 (Bad Request) if the userMatch is not valid,
     * or with status 500 (Internal Server Error) if the userMatch couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-matches")
    @Timed
    public ResponseEntity<UserMatch> updateUserMatch(@RequestBody UserMatch userMatch) throws URISyntaxException {
        log.debug("REST request to update UserMatch : {}", userMatch);
        if (userMatch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserMatch result = userMatchService.save(userMatch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userMatch.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-matches : get all the userMatches.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of userMatches in body
     */
    @GetMapping("/user-matches")
    @Timed
    public ResponseEntity<List<UserMatch>> getAllUserMatches(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of UserMatches");
        Page<UserMatch> page;
        if (eagerload) {
            page = userMatchService.findAllWithEagerRelationships(pageable);
        } else {
            page = userMatchService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/user-matches?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-matches/:id : get the "id" userMatch.
     *
     * @param id the id of the userMatch to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userMatch, or with status 404 (Not Found)
     */
    @GetMapping("/user-matches/{id}")
    @Timed
    public ResponseEntity<UserMatch> getUserMatch(@PathVariable Long id) {
        log.debug("REST request to get UserMatch : {}", id);
        Optional<UserMatch> userMatch = userMatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userMatch);
    }

    /**
     * DELETE  /user-matches/:id : delete the "id" userMatch.
     *
     * @param id the id of the userMatch to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-matches/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserMatch(@PathVariable Long id) {
        log.debug("REST request to delete UserMatch : {}", id);
        userMatchService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /user-matches-regenerate : 重新计算效用矩阵
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userMatches in body
     */
    @GetMapping("/user-matches-regenerate")
    @Timed
    public ResponseEntity getAllUserMatches() throws IOException {
        userMatchService.regenerate();
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("success", /*null*/"")) // 这里不能为中文，，否则会让webpack dev server报错并奔溃，不知道生产环境下服务器是否会报错
            .body(null);
    }
}
