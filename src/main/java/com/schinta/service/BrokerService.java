package com.schinta.service;

import com.schinta.domain.Broker;
import com.schinta.repository.BrokerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Broker.
 */
@Service
@Transactional
public class BrokerService {

    private final Logger log = LoggerFactory.getLogger(BrokerService.class);

    private final BrokerRepository brokerRepository;

    public BrokerService(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;
    }

    /**
     * Save a broker.
     *
     * @param broker the entity to save
     * @return the persisted entity
     */
    public Broker save(Broker broker) {
        log.debug("Request to save Broker : {}", broker);        return brokerRepository.save(broker);
    }

    /**
     * Get all the brokers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Broker> findAll(Pageable pageable) {
        log.debug("Request to get all Brokers");
        return brokerRepository.findAll(pageable);
    }


    /**
     * Get one broker by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Broker> findOne(Long id) {
        log.debug("Request to get Broker : {}", id);
        return brokerRepository.findById(id);
    }

    /**
     * Delete the broker by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Broker : {}", id);
        brokerRepository.deleteById(id);
    }
}
