package com.schinta.service;

import com.schinta.domain.PushRecord;
import com.schinta.repository.PushRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing PushRecord.
 */
@Service
@Transactional
public class PushRecordService {

    private final Logger log = LoggerFactory.getLogger(PushRecordService.class);

    private final PushRecordRepository pushRecordRepository;

    public PushRecordService(PushRecordRepository pushRecordRepository) {
        this.pushRecordRepository = pushRecordRepository;
    }

    /**
     * Save a pushRecord.
     *
     * @param pushRecord the entity to save
     * @return the persisted entity
     */
    public PushRecord save(PushRecord pushRecord) {
        log.debug("Request to save PushRecord : {}", pushRecord);        return pushRecordRepository.save(pushRecord);
    }

    /**
     * Get all the pushRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PushRecord> findAll(Pageable pageable) {
        log.debug("Request to get all PushRecords");
        return pushRecordRepository.findAll(pageable);
    }


    /**
     * Get one pushRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PushRecord> findOne(Long id) {
        log.debug("Request to get PushRecord : {}", id);
        return pushRecordRepository.findById(id);
    }

    /**
     * Delete the pushRecord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PushRecord : {}", id);
        pushRecordRepository.deleteById(id);
    }
}
