package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.WxUser;
import com.schinta.service.WxUserService;
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
 * REST controller for managing WxUser.
 */
@RestController
@RequestMapping("/api")
public class WxUserResource {

    private final Logger log = LoggerFactory.getLogger(WxUserResource.class);

    private static final String ENTITY_NAME = "wxUser";

    private final WxUserService wxUserService;

    public WxUserResource(WxUserService wxUserService) {
        this.wxUserService = wxUserService;
    }

    /**
     * POST  /wx-users : Create a new wxUser.
     *
     * @param wxUser the wxUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wxUser, or with status 400 (Bad Request) if the wxUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wx-users")
    @Timed
    public ResponseEntity<WxUser> createWxUser(@Valid @RequestBody WxUser wxUser) throws URISyntaxException {
        log.debug("REST request to save WxUser : {}", wxUser);
        if (wxUser.getId() != null) {
            throw new BadRequestAlertException("A new wxUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WxUser result = wxUserService.save(wxUser);
        return ResponseEntity.created(new URI("/api/wx-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wx-users : Updates an existing wxUser.
     *
     * @param wxUser the wxUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wxUser,
     * or with status 400 (Bad Request) if the wxUser is not valid,
     * or with status 500 (Internal Server Error) if the wxUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wx-users")
    @Timed
    public ResponseEntity<WxUser> updateWxUser(@Valid @RequestBody WxUser wxUser) throws URISyntaxException {
        log.debug("REST request to update WxUser : {}", wxUser);
        if (wxUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WxUser result = wxUserService.save(wxUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wxUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wx-users : get all the wxUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wxUsers in body
     */
    @GetMapping("/wx-users")
    @Timed
    public ResponseEntity<List<WxUser>> getAllWxUsers(Pageable pageable) {
        log.debug("REST request to get a page of WxUsers");
        Page<WxUser> page = wxUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wx-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wx-users/:id : get the "id" wxUser.
     *
     * @param id the id of the wxUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wxUser, or with status 404 (Not Found)
     */
    @GetMapping("/wx-users/{id}")
    @Timed
    public ResponseEntity<WxUser> getWxUser(@PathVariable Long id) {
        log.debug("REST request to get WxUser : {}", id);
        Optional<WxUser> wxUser = wxUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wxUser);
    }

    /**
     * DELETE  /wx-users/:id : delete the "id" wxUser.
     *
     * @param id the id of the wxUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wx-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteWxUser(@PathVariable Long id) {
        log.debug("REST request to delete WxUser : {}", id);
        wxUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
