package com.schinta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.PushRecord;
import com.schinta.domain.WxUser;
import com.schinta.repository.WxUserRepository;
import com.schinta.service.PushRecordService;
import com.schinta.web.rest.errors.BadRequestAlertException;
import com.schinta.web.rest.util.HeaderUtil;
import com.schinta.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PushRecord.
 */
@RestController
@RequestMapping("/api")
public class PushRecordResource {

    private final Logger log = LoggerFactory.getLogger(PushRecordResource.class);

    private static final String ENTITY_NAME = "pushRecord";

    private final PushRecordService pushRecordService;
    private final WxUserRepository wxUserRepository;

    public PushRecordResource(PushRecordService pushRecordService,
                              WxUserRepository wxUserRepository) {
        this.pushRecordService = pushRecordService;
        this.wxUserRepository = wxUserRepository;
    }

    /**
     * POST  /push-records : Create a new pushRecord.
     *
     * @param pushRecord the pushRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pushRecord, or with status 400 (Bad Request) if the pushRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/push-records")
    @Timed
    public ResponseEntity<PushRecord> createPushRecord(@RequestBody PushRecord pushRecord) throws URISyntaxException {
        log.debug("REST request to save PushRecord : {}", pushRecord);
        if (pushRecord.getId() != null) {
            throw new BadRequestAlertException("A new pushRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PushRecord result = pushRecordService.save(pushRecord);
        return ResponseEntity.created(new URI("/api/push-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /push-records : Updates an existing pushRecord.
     *
     * @param pushRecord the pushRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pushRecord,
     * or with status 400 (Bad Request) if the pushRecord is not valid,
     * or with status 500 (Internal Server Error) if the pushRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/push-records")
    @Timed
    public ResponseEntity<PushRecord> updatePushRecord(@RequestBody PushRecord pushRecord) throws URISyntaxException {
        log.debug("REST request to update PushRecord : {}", pushRecord);
        if (pushRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PushRecord result = pushRecordService.save(pushRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pushRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /push-records : get all the pushRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pushRecords in body
     */
    @GetMapping("/push-records")
    @Timed
    public ResponseEntity<List<PushRecord>> getAllPushRecords(Pageable pageable, @RequestParam(required = false) LocalDateTime time) {
        log.debug("REST request to get a page of PushRecords");
        Page<PushRecord> page;
        if (time != null) {
            page = pushRecordService.findAllByPushTime(pageable,time);
        } else {
            page = pushRecordService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/push-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /push-records/:id : get the "id" pushRecord.
     *
     * @param id the id of the pushRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
     */
    @GetMapping("/push-records/{id}")
    @Timed
    public ResponseEntity<PushRecord> getPushRecord(@PathVariable Long id) {
        log.debug("REST request to get PushRecord : {}", id);
        Optional<PushRecord> pushRecord = pushRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pushRecord);
    }

    /**
     * DELETE  /push-records/:id : delete the "id" pushRecord.
     *
     * @param id the id of the pushRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/push-records/{id}")
    @Timed
    public ResponseEntity<Void> deletePushRecord(@PathVariable Long id) {
        log.debug("REST request to delete PushRecord : {}", id);
        pushRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     *
     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
     */
    @GetMapping("/push-timestamps")
    @Timed
    public ResponseEntity<List<LocalDateTime>> getPushTimes() throws WxErrorException, MalformedURLException {
        List<LocalDateTime> localDateTimes = pushRecordService.getPushTimes();
        return ResponseEntity.ok().body(localDateTimes);
    }

    /**
     * GET  /push-user-asked/:id : 用户主动获取最佳配对，返回计算得到的推送记录.
     *
     * @param id 用户的openid
     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
     */
    @GetMapping("/push-user-asked/{id}")
    @Timed
    public ResponseEntity<PushRecord> pushUserAsked(@PathVariable String id) throws WxErrorException, MalformedURLException {
        WxUser wxUser = wxUserRepository.findById(id).get();
        PushRecord pushRecord = pushRecordService.getUserAsked(wxUser);
//        PushRecord pushRecord = pushRecordService.findOneWithMatches(Long.valueOf(2)); // 测试用
        pushRecordService.wxPush(pushRecord);

        return ResponseEntity.ok().body(null);
    }

    /**
     * GET  /broadcast : 直接群发（不提供后台预览确认的机会）。分成两个事务：1.计算生成推送记录；2.向用户群发消息（只是一个带时间戳的地址）
     * todo 应该合成一个事务，并且注意捕获WxErrorException并回滚事务
     *
     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
     */
    @GetMapping("/broadcast-immediate")
    @Timed
    public ResponseEntity<List<PushRecord>> broadcastImmediate() throws WxErrorException, MalformedURLException {
        LocalDateTime localDateTime = LocalDateTime.now();
        // 计算生成新的推送记录（同时更新usermatch部分）
        List<PushRecord> pushRecords = pushRecordService.getBroadcast(localDateTime);
        pushRecordService.wxBroadcast(localDateTime, pushRecords);
        return ResponseEntity.ok().body(pushRecords);
    }

//    /**
//     * GET  /broadcast : 测试群发：1.计算所有活跃用户的最新配对；2.推送给指定人预览
//     *
//     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
//     */
//    @GetMapping("/broadcast-test")
//    @Timed
//    public ResponseEntity<List<PushRecord>> testBroadcast(@RequestParam String openid) throws WxErrorException, MalformedURLException {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        // 计算生成新的推送记录（同时更新usermatch部分）
//        List<PushRecord> pushRecords = pushRecordService.getBroadcast(localDateTime);
//        pushRecordService.wxMassPreview(localDateTime, openid);
//        return ResponseEntity.ok().body(pushRecords);
//    }

    /**
     * GET  /broadcast-records : 生成群发推送结果
     *
     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
     */
    @GetMapping("/broadcast-records")
    @Timed
    public ResponseEntity<LocalDateTime> broadcastRecords() {
        LocalDateTime localDateTime = LocalDateTime.now();
        // 计算生成新的推送记录（同时更新usermatch部分）
        List<PushRecord> pushRecords = pushRecordService.getBroadcast(localDateTime);
        return ResponseEntity.ok().body(localDateTime);
    }

    /**
     * GET  /broadcast-push : 群发指定时间戳的已生成记录
     *
     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
     */
    @GetMapping("/broadcast-push")
    @Timed
    public ResponseEntity broadcastPush(@RequestParam LocalDateTime timestamp) throws WxErrorException, MalformedURLException {
        // 根据时间戳查找相应的推送记录
        List<PushRecord> pushRecords = pushRecordService.findAllByPushTime(timestamp);
        pushRecordService.wxBroadcast(timestamp, pushRecords);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("success", /*null*/"")) // 这里不能为中文，，否则会让webpack dev server报错并奔溃，不知道生产环境下服务器是否会报错
            .body(null);
    }

    /**
     * GET  /mass-preview : 群发消息微信预览
     *
     * @return the ResponseEntity with status 200 (OK) and with body the pushRecord, or with status 404 (Not Found)
     */
    @GetMapping("/mass-preview")
    @Timed
    public ResponseEntity massPreview(@RequestParam LocalDateTime timestamp, @RequestParam String openid) throws WxErrorException, MalformedURLException {
        pushRecordService.wxMassPreview(timestamp, openid);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("success", /*null*/"")) // 这里不能为中文，，否则会让webpack dev server报错并奔溃，不知道生产环境下服务器是否会报错
            .body(null);
    }
}
