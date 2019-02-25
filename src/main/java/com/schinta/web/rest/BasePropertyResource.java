package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.BaseProperty;
import com.schinta.repository.BasePropertyRepository;
import com.schinta.service.BasePropertyService;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BaseProperty.
 */
@RestController
@RequestMapping("/api")
public class BasePropertyResource {

    private final Logger log = LoggerFactory.getLogger(BasePropertyResource.class);

    private static final String ENTITY_NAME = "baseProperty";

    private final BasePropertyService basePropertyService;
    private final BasePropertyRepository basePropertyRepository;

    public BasePropertyResource(BasePropertyService basePropertyService,
                                BasePropertyRepository basePropertyRepository) {
        this.basePropertyService = basePropertyService;
        this.basePropertyRepository = basePropertyRepository;
    }

    /**
     * POST  /base-properties : Create a new baseProperty.
     *
     * @param baseProperty the baseProperty to create
     * @return the ResponseEntity with status 201 (Created) and with body the new baseProperty, or with status 400 (Bad Request) if the baseProperty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/base-properties")
    @Timed
    public ResponseEntity<BaseProperty> createBaseProperty(@Valid @RequestBody BaseProperty baseProperty) throws URISyntaxException {
        log.debug("REST request to save BaseProperty : {}", baseProperty);
        if (baseProperty.getId() != null) {
            throw new BadRequestAlertException("A new baseProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BaseProperty result = basePropertyService.save(baseProperty);
        return ResponseEntity.created(new URI("/api/base-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /base-properties : Updates an existing baseProperty.
     *
     * @param baseProperty the baseProperty to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated baseProperty,
     * or with status 400 (Bad Request) if the baseProperty is not valid,
     * or with status 500 (Internal Server Error) if the baseProperty couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/base-properties")
    @Timed
    public ResponseEntity<BaseProperty> updateBaseProperty(@Valid @RequestBody BaseProperty baseProperty) throws URISyntaxException {
        log.debug("REST request to update BaseProperty : {}", baseProperty);
        if (baseProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BaseProperty result = basePropertyService.save(baseProperty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, baseProperty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /base-properties : get all the baseProperties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of baseProperties in body
     */
    @GetMapping("/base-properties")
    @Timed
    public ResponseEntity<List<BaseProperty>> getAllBaseProperties(Pageable pageable) {
        log.debug("REST request to get a page of BaseProperties");
        Page<BaseProperty> page = basePropertyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/base-properties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /base-properties : get all the baseProperties.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of baseProperties in body
     */
    @GetMapping("/base-properties-all")
    @Timed
    public ResponseEntity<List<BaseProperty>> getAllBaseProperties() {
        log.debug("REST request to get all BaseProperties");
        return ResponseEntity.ok(basePropertyRepository.findAll());
    }

    /**
     * GET  /base-properties/:id : get the "id" baseProperty.
     *
     * @param id the id of the baseProperty to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the baseProperty, or with status 404 (Not Found)
     */
    @GetMapping("/base-properties/{id}")
    @Timed
    public ResponseEntity<BaseProperty> getBaseProperty(@PathVariable Long id) {
        log.debug("REST request to get BaseProperty : {}", id);
        Optional<BaseProperty> baseProperty = basePropertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(baseProperty);
    }

    /**
     * DELETE  /base-properties/:id : delete the "id" baseProperty.
     *
     * @param id the id of the baseProperty to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/base-properties/{id}")
    @Timed
    public ResponseEntity<Void> deleteBaseProperty(@PathVariable Long id) {
        log.debug("REST request to delete BaseProperty : {}", id);
        basePropertyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /user-properties-demands/:id : 获取用户的全部属性、需求 .
     *
     * @param id 用户的openId
     * @return the ResponseEntity with status 200 (OK) and with body the baseProperty, or with status 404 (Not Found)
     */
    @GetMapping("/user-properties-demands/{id}")
    @Timed
    public ResponseEntity<List<BaseProperty>> getUserPropertyDemands(@PathVariable String id) {
        log.debug("获取用户的全部属性、需求 : {}", id);
        List<BaseProperty> userPropertyDemands = basePropertyService.findUserPropertyDemands(id);
        return ResponseEntity.ok(userPropertyDemands);
    }

    /**
     * POST  /user-properties-demands/:id : 保存用户的全部属性、需求 .
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new baseProperty, or with status 400 (Bad Request) if the baseProperty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-properties-demands")
    @Timed
    public ResponseEntity saveUserProperty(@Valid @RequestBody List<BaseProperty> baseProperties) throws URISyntaxException, IOException {
        log.debug("通过小伊管理后台保存用户的全部属性、需求");
        basePropertyService.saveUserProperties(baseProperties);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("success",""))
            .body(null);
    }
}
