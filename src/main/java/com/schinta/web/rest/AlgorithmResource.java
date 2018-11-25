package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.Algorithm;
import com.schinta.repository.AlgorithmRepository;
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
 * REST controller for managing Algorithm.
 */
@RestController
@RequestMapping("/api")
public class AlgorithmResource {

    private final Logger log = LoggerFactory.getLogger(AlgorithmResource.class);

    private static final String ENTITY_NAME = "algorithm";

    private final AlgorithmRepository algorithmRepository;

    public AlgorithmResource(AlgorithmRepository algorithmRepository) {
        this.algorithmRepository = algorithmRepository;
    }

    /**
     * POST  /algorithms : Create a new algorithm.
     *
     * @param algorithm the algorithm to create
     * @return the ResponseEntity with status 201 (Created) and with body the new algorithm, or with status 400 (Bad Request) if the algorithm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/algorithms")
    @Timed
    public ResponseEntity<Algorithm> createAlgorithm(@Valid @RequestBody Algorithm algorithm) throws URISyntaxException {
        log.debug("REST request to save Algorithm : {}", algorithm);
        if (algorithm.getId() != null) {
            throw new BadRequestAlertException("A new algorithm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Algorithm result = algorithmRepository.save(algorithm);
        return ResponseEntity.created(new URI("/api/algorithms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /algorithms : Updates an existing algorithm.
     *
     * @param algorithm the algorithm to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated algorithm,
     * or with status 400 (Bad Request) if the algorithm is not valid,
     * or with status 500 (Internal Server Error) if the algorithm couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/algorithms")
    @Timed
    public ResponseEntity<Algorithm> updateAlgorithm(@Valid @RequestBody Algorithm algorithm) throws URISyntaxException {
        log.debug("REST request to update Algorithm : {}", algorithm);
        if (algorithm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Algorithm result = algorithmRepository.save(algorithm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, algorithm.getId().toString()))
            .body(result);
    }

    /**
     * GET  /algorithms : get all the algorithms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of algorithms in body
     */
    @GetMapping("/algorithms")
    @Timed
    public ResponseEntity<List<Algorithm>> getAllAlgorithms(Pageable pageable) {
        log.debug("REST request to get a page of Algorithms");
        Page<Algorithm> page = algorithmRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/algorithms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /algorithms/:id : get the "id" algorithm.
     *
     * @param id the id of the algorithm to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the algorithm, or with status 404 (Not Found)
     */
    @GetMapping("/algorithms/{id}")
    @Timed
    public ResponseEntity<Algorithm> getAlgorithm(@PathVariable Long id) {
        log.debug("REST request to get Algorithm : {}", id);
        Optional<Algorithm> algorithm = algorithmRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(algorithm);
    }

    /**
     * DELETE  /algorithms/:id : delete the "id" algorithm.
     *
     * @param id the id of the algorithm to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/algorithms/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlgorithm(@PathVariable Long id) {
        log.debug("REST request to delete Algorithm : {}", id);

        algorithmRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
