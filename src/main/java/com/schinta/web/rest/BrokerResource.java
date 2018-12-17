package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.Broker;
import com.schinta.service.BrokerService;
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
 * REST controller for managing Broker.
 */
@RestController
@RequestMapping("/api")
public class BrokerResource {

    private final Logger log = LoggerFactory.getLogger(BrokerResource.class);

    private static final String ENTITY_NAME = "broker";

    private final BrokerService brokerService;

    public BrokerResource(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    /**
     * POST  /brokers : Create a new broker.
     *
     * @param broker the broker to create
     * @return the ResponseEntity with status 201 (Created) and with body the new broker, or with status 400 (Bad Request) if the broker has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokers")
    @Timed
    public ResponseEntity<Broker> createBroker(@Valid @RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to save Broker : {}", broker);
        if (broker.getId() != null) {
            throw new BadRequestAlertException("A new broker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Broker result = brokerService.save(broker);
        return ResponseEntity.created(new URI("/api/brokers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokers : Updates an existing broker.
     *
     * @param broker the broker to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated broker,
     * or with status 400 (Bad Request) if the broker is not valid,
     * or with status 500 (Internal Server Error) if the broker couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokers")
    @Timed
    public ResponseEntity<Broker> updateBroker(@Valid @RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to update Broker : {}", broker);
        if (broker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Broker result = brokerService.save(broker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, broker.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokers : get all the brokers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of brokers in body
     */
    @GetMapping("/brokers")
    @Timed
    public ResponseEntity<List<Broker>> getAllBrokers(Pageable pageable) {
        log.debug("REST request to get a page of Brokers");
        Page<Broker> page = brokerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brokers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /brokers/:id : get the "id" broker.
     *
     * @param id the id of the broker to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the broker, or with status 404 (Not Found)
     */
    @GetMapping("/brokers/{id}")
    @Timed
    public ResponseEntity<Broker> getBroker(@PathVariable Long id) {
        log.debug("REST request to get Broker : {}", id);
        Optional<Broker> broker = brokerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(broker);
    }

    /**
     * DELETE  /brokers/:id : delete the "id" broker.
     *
     * @param id the id of the broker to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokers/{id}")
    @Timed
    public ResponseEntity<Void> deleteBroker(@PathVariable Long id) {
        log.debug("REST request to delete Broker : {}", id);
        brokerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
