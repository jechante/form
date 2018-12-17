package com.schinta.service;

import com.schinta.domain.Algorithm;
import com.schinta.repository.AlgorithmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Algorithm.
 */
@Service
@Transactional
public class AlgorithmService {

    private final Logger log = LoggerFactory.getLogger(AlgorithmService.class);

    private final AlgorithmRepository algorithmRepository;

    public AlgorithmService(AlgorithmRepository algorithmRepository) {
        this.algorithmRepository = algorithmRepository;
    }

    /**
     * Save a algorithm.
     *
     * @param algorithm the entity to save
     * @return the persisted entity
     */
    public Algorithm save(Algorithm algorithm) {
        log.debug("Request to save Algorithm : {}", algorithm);        return algorithmRepository.save(algorithm);
    }

    /**
     * Get all the algorithms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Algorithm> findAll(Pageable pageable) {
        log.debug("Request to get all Algorithms");
        return algorithmRepository.findAll(pageable);
    }

    /**
     * Get all the Algorithm with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Algorithm> findAllWithEagerRelationships(Pageable pageable) {
        return algorithmRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one algorithm by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Algorithm> findOne(Long id) {
        log.debug("Request to get Algorithm : {}", id);
        return algorithmRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the algorithm by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Algorithm : {}", id);
        algorithmRepository.deleteById(id);
    }
}
