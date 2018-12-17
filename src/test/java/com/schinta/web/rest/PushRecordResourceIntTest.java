package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.PushRecord;
import com.schinta.repository.PushRecordRepository;
import com.schinta.service.PushRecordService;
import com.schinta.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.schinta.web.rest.TestUtil.sameInstant;
import static com.schinta.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.schinta.domain.enumeration.PushType;
/**
 * Test class for the PushRecordResource REST controller.
 *
 * @see PushRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class PushRecordResourceIntTest {

    private static final PushType DEFAULT_PUSH_TYPE = PushType.ASK;
    private static final PushType UPDATED_PUSH_TYPE = PushType.Regular;

    private static final ZonedDateTime DEFAULT_PUSH_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUSH_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_SUCCESS = false;
    private static final Boolean UPDATED_SUCCESS = true;

    @Autowired
    private PushRecordRepository pushRecordRepository;
    
    @Autowired
    private PushRecordService pushRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPushRecordMockMvc;

    private PushRecord pushRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PushRecordResource pushRecordResource = new PushRecordResource(pushRecordService);
        this.restPushRecordMockMvc = MockMvcBuilders.standaloneSetup(pushRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PushRecord createEntity(EntityManager em) {
        PushRecord pushRecord = new PushRecord()
            .pushType(DEFAULT_PUSH_TYPE)
            .pushDateTime(DEFAULT_PUSH_DATE_TIME)
            .success(DEFAULT_SUCCESS);
        return pushRecord;
    }

    @Before
    public void initTest() {
        pushRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createPushRecord() throws Exception {
        int databaseSizeBeforeCreate = pushRecordRepository.findAll().size();

        // Create the PushRecord
        restPushRecordMockMvc.perform(post("/api/push-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pushRecord)))
            .andExpect(status().isCreated());

        // Validate the PushRecord in the database
        List<PushRecord> pushRecordList = pushRecordRepository.findAll();
        assertThat(pushRecordList).hasSize(databaseSizeBeforeCreate + 1);
        PushRecord testPushRecord = pushRecordList.get(pushRecordList.size() - 1);
        assertThat(testPushRecord.getPushType()).isEqualTo(DEFAULT_PUSH_TYPE);
        assertThat(testPushRecord.getPushDateTime()).isEqualTo(DEFAULT_PUSH_DATE_TIME);
        assertThat(testPushRecord.isSuccess()).isEqualTo(DEFAULT_SUCCESS);
    }

    @Test
    @Transactional
    public void createPushRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pushRecordRepository.findAll().size();

        // Create the PushRecord with an existing ID
        pushRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPushRecordMockMvc.perform(post("/api/push-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pushRecord)))
            .andExpect(status().isBadRequest());

        // Validate the PushRecord in the database
        List<PushRecord> pushRecordList = pushRecordRepository.findAll();
        assertThat(pushRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPushRecords() throws Exception {
        // Initialize the database
        pushRecordRepository.saveAndFlush(pushRecord);

        // Get all the pushRecordList
        restPushRecordMockMvc.perform(get("/api/push-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pushRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].pushType").value(hasItem(DEFAULT_PUSH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pushDateTime").value(hasItem(sameInstant(DEFAULT_PUSH_DATE_TIME))))
            .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPushRecord() throws Exception {
        // Initialize the database
        pushRecordRepository.saveAndFlush(pushRecord);

        // Get the pushRecord
        restPushRecordMockMvc.perform(get("/api/push-records/{id}", pushRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pushRecord.getId().intValue()))
            .andExpect(jsonPath("$.pushType").value(DEFAULT_PUSH_TYPE.toString()))
            .andExpect(jsonPath("$.pushDateTime").value(sameInstant(DEFAULT_PUSH_DATE_TIME)))
            .andExpect(jsonPath("$.success").value(DEFAULT_SUCCESS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPushRecord() throws Exception {
        // Get the pushRecord
        restPushRecordMockMvc.perform(get("/api/push-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePushRecord() throws Exception {
        // Initialize the database
        pushRecordService.save(pushRecord);

        int databaseSizeBeforeUpdate = pushRecordRepository.findAll().size();

        // Update the pushRecord
        PushRecord updatedPushRecord = pushRecordRepository.findById(pushRecord.getId()).get();
        // Disconnect from session so that the updates on updatedPushRecord are not directly saved in db
        em.detach(updatedPushRecord);
        updatedPushRecord
            .pushType(UPDATED_PUSH_TYPE)
            .pushDateTime(UPDATED_PUSH_DATE_TIME)
            .success(UPDATED_SUCCESS);

        restPushRecordMockMvc.perform(put("/api/push-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPushRecord)))
            .andExpect(status().isOk());

        // Validate the PushRecord in the database
        List<PushRecord> pushRecordList = pushRecordRepository.findAll();
        assertThat(pushRecordList).hasSize(databaseSizeBeforeUpdate);
        PushRecord testPushRecord = pushRecordList.get(pushRecordList.size() - 1);
        assertThat(testPushRecord.getPushType()).isEqualTo(UPDATED_PUSH_TYPE);
        assertThat(testPushRecord.getPushDateTime()).isEqualTo(UPDATED_PUSH_DATE_TIME);
        assertThat(testPushRecord.isSuccess()).isEqualTo(UPDATED_SUCCESS);
    }

    @Test
    @Transactional
    public void updateNonExistingPushRecord() throws Exception {
        int databaseSizeBeforeUpdate = pushRecordRepository.findAll().size();

        // Create the PushRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPushRecordMockMvc.perform(put("/api/push-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pushRecord)))
            .andExpect(status().isBadRequest());

        // Validate the PushRecord in the database
        List<PushRecord> pushRecordList = pushRecordRepository.findAll();
        assertThat(pushRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePushRecord() throws Exception {
        // Initialize the database
        pushRecordService.save(pushRecord);

        int databaseSizeBeforeDelete = pushRecordRepository.findAll().size();

        // Get the pushRecord
        restPushRecordMockMvc.perform(delete("/api/push-records/{id}", pushRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PushRecord> pushRecordList = pushRecordRepository.findAll();
        assertThat(pushRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PushRecord.class);
        PushRecord pushRecord1 = new PushRecord();
        pushRecord1.setId(1L);
        PushRecord pushRecord2 = new PushRecord();
        pushRecord2.setId(pushRecord1.getId());
        assertThat(pushRecord1).isEqualTo(pushRecord2);
        pushRecord2.setId(2L);
        assertThat(pushRecord1).isNotEqualTo(pushRecord2);
        pushRecord1.setId(null);
        assertThat(pushRecord1).isNotEqualTo(pushRecord2);
    }
}
