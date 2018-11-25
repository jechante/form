package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.MatchRecord;
import com.schinta.repository.MatchRecordRepository;
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
 * REST controller for managing MatchRecord.
 */
@RestController
@RequestMapping("/api")
public class MatchRecordResource {

    private final Logger log = LoggerFactory.getLogger(MatchRecordResource.class);

    private static final String ENTITY_NAME = "matchRecord";

    private final MatchRecordRepository matchRecordRepository;

    public MatchRecordResource(MatchRecordRepository matchRecordRepository) {
        this.matchRecordRepository = matchRecordRepository;
    }

    /**
     * POST  /match-records : Create a new matchRecord.
     *
     * @param matchRecord the matchRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new matchRecord, or with status 400 (Bad Request) if the matchRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/match-records")
    @Timed
    public ResponseEntity<MatchRecord> createMatchRecord(@Valid @RequestBody MatchRecord matchRecord) throws URISyntaxException {
        log.debug("REST request to save MatchRecord : {}", matchRecord);
        if (matchRecord.getId() != null) {
            throw new BadRequestAlertException("A new matchRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatchRecord result = matchRecordRepository.save(matchRecord);
        return ResponseEntity.created(new URI("/api/match-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /match-records : Updates an existing matchRecord.
     *
     * @param matchRecord the matchRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated matchRecord,
     * or with status 400 (Bad Request) if the matchRecord is not valid,
     * or with status 500 (Internal Server Error) if the matchRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/match-records")
    @Timed
    public ResponseEntity<MatchRecord> updateMatchRecord(@Valid @RequestBody MatchRecord matchRecord) throws URISyntaxException {
        log.debug("REST request to update MatchRecord : {}", matchRecord);
        if (matchRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MatchRecord result = matchRecordRepository.save(matchRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, matchRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /match-records : get all the matchRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of matchRecords in body
     */
    @GetMapping("/match-records")
    @Timed
    public ResponseEntity<List<MatchRecord>> getAllMatchRecords(Pageable pageable) {
        log.debug("REST request to get a page of MatchRecords");
        Page<MatchRecord> page = matchRecordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/match-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /match-records/:id : get the "id" matchRecord.
     *
     * @param id the id of the matchRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the matchRecord, or with status 404 (Not Found)
     */
    @GetMapping("/match-records/{id}")
    @Timed
    public ResponseEntity<MatchRecord> getMatchRecord(@PathVariable Long id) {
        log.debug("REST request to get MatchRecord : {}", id);
        Optional<MatchRecord> matchRecord = matchRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(matchRecord);
    }

    /**
     * DELETE  /match-records/:id : delete the "id" matchRecord.
     *
     * @param id the id of the matchRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/match-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteMatchRecord(@PathVariable Long id) {
        log.debug("REST request to delete MatchRecord : {}", id);

        matchRecordRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
