package com.schinta.service;

import com.schinta.domain.WxUser;
import com.schinta.repository.WxUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing WxUser.
 */
@Service
@Transactional
public class WxUserService {

    private final Logger log = LoggerFactory.getLogger(WxUserService.class);

    private final WxUserRepository wxUserRepository;

    public WxUserService(WxUserRepository wxUserRepository) {
        this.wxUserRepository = wxUserRepository;
    }

    /**
     * Save a wxUser.
     *
     * @param wxUser the entity to save
     * @return the persisted entity
     */
    public WxUser save(WxUser wxUser) {
        log.debug("Request to save WxUser : {}", wxUser);        return wxUserRepository.save(wxUser);
    }

    /**
     * Get all the wxUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WxUser> findAll(Pageable pageable) {
        log.debug("Request to get all WxUsers");
        return wxUserRepository.findAll(pageable);
    }


    /**
     * Get one wxUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<WxUser> findOne(String id) {
        log.debug("Request to get WxUser : {}", id);
        return wxUserRepository.findById(id);
    }

    /**
     * Delete the wxUser by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete WxUser : {}", id);
        wxUserRepository.deleteById(id);
    }
}
