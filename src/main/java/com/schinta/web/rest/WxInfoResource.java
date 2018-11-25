package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.WxInfo;
import com.schinta.repository.WxInfoRepository;
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
 * REST controller for managing WxInfo.
 */
@RestController
@RequestMapping("/api")
public class WxInfoResource {

    private final Logger log = LoggerFactory.getLogger(WxInfoResource.class);

    private static final String ENTITY_NAME = "wxInfo";

    private final WxInfoRepository wxInfoRepository;

    public WxInfoResource(WxInfoRepository wxInfoRepository) {
        this.wxInfoRepository = wxInfoRepository;
    }

    /**
     * POST  /wx-infos : Create a new wxInfo.
     *
     * @param wxInfo the wxInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wxInfo, or with status 400 (Bad Request) if the wxInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wx-infos")
    @Timed
    public ResponseEntity<WxInfo> createWxInfo(@Valid @RequestBody WxInfo wxInfo) throws URISyntaxException {
        log.debug("REST request to save WxInfo : {}", wxInfo);
        if (wxInfo.getId() != null) {
            throw new BadRequestAlertException("A new wxInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WxInfo result = wxInfoRepository.save(wxInfo);
        return ResponseEntity.created(new URI("/api/wx-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wx-infos : Updates an existing wxInfo.
     *
     * @param wxInfo the wxInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wxInfo,
     * or with status 400 (Bad Request) if the wxInfo is not valid,
     * or with status 500 (Internal Server Error) if the wxInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wx-infos")
    @Timed
    public ResponseEntity<WxInfo> updateWxInfo(@Valid @RequestBody WxInfo wxInfo) throws URISyntaxException {
        log.debug("REST request to update WxInfo : {}", wxInfo);
        if (wxInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WxInfo result = wxInfoRepository.save(wxInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wxInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wx-infos : get all the wxInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wxInfos in body
     */
    @GetMapping("/wx-infos")
    @Timed
    public ResponseEntity<List<WxInfo>> getAllWxInfos(Pageable pageable) {
        log.debug("REST request to get a page of WxInfos");
        Page<WxInfo> page = wxInfoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wx-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wx-infos/:id : get the "id" wxInfo.
     *
     * @param id the id of the wxInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wxInfo, or with status 404 (Not Found)
     */
    @GetMapping("/wx-infos/{id}")
    @Timed
    public ResponseEntity<WxInfo> getWxInfo(@PathVariable Long id) {
        log.debug("REST request to get WxInfo : {}", id);
        Optional<WxInfo> wxInfo = wxInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wxInfo);
    }

    /**
     * DELETE  /wx-infos/:id : delete the "id" wxInfo.
     *
     * @param id the id of the wxInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wx-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteWxInfo(@PathVariable Long id) {
        log.debug("REST request to delete WxInfo : {}", id);

        wxInfoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
