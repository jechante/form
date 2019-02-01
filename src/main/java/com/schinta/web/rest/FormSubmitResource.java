package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.FormSubmit;
import com.schinta.repository.BaseFormRepository;
import com.schinta.repository.FormSubmitRepository;
import com.schinta.repository.WxUserRepository;
import com.schinta.service.FormSubmitService;
import com.schinta.service.UserMatchService;
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
 * REST controller for managing FormSubmit.
 */
@RestController
@RequestMapping("/api")
public class FormSubmitResource {

    private final Logger log = LoggerFactory.getLogger(FormSubmitResource.class);

    private static final String ENTITY_NAME = "formSubmit";

    private final FormSubmitService formSubmitService;

//    private final BaseFormRepository baseFormRepository;

//    private final WxUserRepository wxUserRepository;
//    private final FormSubmitRepository formSubmitRepository;
    private final UserMatchService userMatchService;

    public FormSubmitResource(FormSubmitService formSubmitService,
//                              BaseFormRepository baseFormRepository,
//                              WxUserRepository wxUserRepository,
//                              FormSubmitRepository formSubmitRepository,
                              UserMatchService userMatchService) {
        this.formSubmitService = formSubmitService;
//        this.baseFormRepository = baseFormRepository;
//        this.wxUserRepository = wxUserRepository;
//        this.formSubmitRepository = formSubmitRepository;
        this.userMatchService = userMatchService;
    }

    /**
     * POST  /form-submits : Create a new formSubmit.
     *
     * @param formSubmit the formSubmit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formSubmit, or with status 400 (Bad Request) if the formSubmit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/form-submits")
    @Timed
    public ResponseEntity<FormSubmit> createFormSubmit(@Valid @RequestBody FormSubmit formSubmit) throws URISyntaxException {
        log.debug("REST request to save FormSubmit : {}", formSubmit);
        if (formSubmit.getId() != null) {
            throw new BadRequestAlertException("A new formSubmit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormSubmit result = formSubmitService.save(formSubmit);
        return ResponseEntity.created(new URI("/api/form-submits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /form-submits : Updates an existing formSubmit.
     *
     * @param formSubmit the formSubmit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formSubmit,
     * or with status 400 (Bad Request) if the formSubmit is not valid,
     * or with status 500 (Internal Server Error) if the formSubmit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/form-submits")
    @Timed
    public ResponseEntity<FormSubmit> updateFormSubmit(@Valid @RequestBody FormSubmit formSubmit) throws URISyntaxException {
        log.debug("REST request to update FormSubmit : {}", formSubmit);
        if (formSubmit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormSubmit result = formSubmitService.save(formSubmit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formSubmit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /form-submits : get all the formSubmits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of formSubmits in body
     */
    @GetMapping("/form-submits")
    @Timed
    public ResponseEntity<List<FormSubmit>> getAllFormSubmits(Pageable pageable) {
        log.debug("REST request to get a page of FormSubmits");
        Page<FormSubmit> page = formSubmitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/form-submits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /form-submits/:id : get the "id" formSubmit.
     *
     * @param id the id of the formSubmit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formSubmit, or with status 404 (Not Found)
     */
    @GetMapping("/form-submits/{id}")
    @Timed
    public ResponseEntity<FormSubmit> getFormSubmit(@PathVariable Long id) {
        log.debug("REST request to get FormSubmit : {}", id);
        Optional<FormSubmit> formSubmit = formSubmitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formSubmit);
    }

    /**
     * DELETE  /form-submits/:id : delete the "id" formSubmit.
     *
     * @param id the id of the formSubmit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/form-submits/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormSubmit(@PathVariable Long id) {
        log.debug("REST request to delete FormSubmit : {}", id);
        formSubmitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 金数据表单「 邀请你来填写金数据表单《[新]金华-测试用表单》https://jinshuju.net/f/Q4qaJD 」的数据推送地址
     * formSubmit、userProperty和userDemand的保存、计算该用户与现有其他用户的效用矩阵分成三个事务，避免推送表单数据保存失败（目前用dealflag、computed标记是否处理）。
     */
    @PostMapping("/form-submits-jin/Q4qaJD")
    @Timed
    public ResponseEntity<Object> printTest(@Valid @RequestBody String submitJson) throws URISyntaxException, IOException, InstantiationException, IllegalAccessException {
        log.info("收到金数据推送表单 : {}", submitJson);

        // 保存推送表单json及元数据
        FormSubmit newSubmit = formSubmitService.saveSubmitJson(submitJson);
        if (newSubmit == null) return ResponseEntity.ok().body("重复推送");
        log.debug("不是重复推送");
        // 更新用户属性和需求值
        formSubmitService.updateUserPropertyAndDemand(newSubmit);
        // 计算该用户与现有其他用户的效用矩阵
        userMatchService.computeUserToOthers(newSubmit);
        return ResponseEntity.created(new URI("/api/form-submits-jin/Q4qaJD/" + newSubmit.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, newSubmit.getId().toString()))
            .body(newSubmit);
    }
}
