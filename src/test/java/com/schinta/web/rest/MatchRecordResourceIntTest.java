package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.MatchRecord;
import com.schinta.repository.MatchRecordRepository;
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
import java.util.List;


import static com.schinta.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MatchRecordResource REST controller.
 *
 * @see MatchRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class MatchRecordResourceIntTest {

    private static final String DEFAULT_RECORD_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_A = "AAAAAAAAAA";
    private static final String UPDATED_USER_A = "BBBBBBBBBB";

    private static final String DEFAULT_USER_B = "AAAAAAAAAA";
    private static final String UPDATED_USER_B = "BBBBBBBBBB";

    private static final String DEFAULT_ALGORITHM_ID = "AAAAAAAAAA";
    private static final String UPDATED_ALGORITHM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INITIATOR_TYPE = "A";
    private static final String UPDATED_INITIATOR_TYPE = "B";

    private static final Integer DEFAULT_SCORE_ATO_B = 1;
    private static final Integer UPDATED_SCORE_ATO_B = 2;

    private static final Integer DEFAULT_SCORE_BTO_A = 1;
    private static final Integer UPDATED_SCORE_BTO_A = 2;

    private static final Integer DEFAULT_SCORE_TOTAL = 1;
    private static final Integer UPDATED_SCORE_TOTAL = 2;

    private static final Double DEFAULT_GENDER_WEIGHT = 1D;
    private static final Double UPDATED_GENDER_WEIGHT = 2D;

    private static final String DEFAULT_PROPERTY_A = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_A = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY_B = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_B = "BBBBBBBBBB";

    @Autowired
    private MatchRecordRepository matchRecordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMatchRecordMockMvc;

    private MatchRecord matchRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MatchRecordResource matchRecordResource = new MatchRecordResource(matchRecordRepository);
        this.restMatchRecordMockMvc = MockMvcBuilders.standaloneSetup(matchRecordResource)
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
    public static MatchRecord createEntity(EntityManager em) {
        MatchRecord matchRecord = new MatchRecord()
            .recordID(DEFAULT_RECORD_ID)
            .userA(DEFAULT_USER_A)
            .userB(DEFAULT_USER_B)
            .algorithmID(DEFAULT_ALGORITHM_ID)
            .initiatorType(DEFAULT_INITIATOR_TYPE)
            .scoreAtoB(DEFAULT_SCORE_ATO_B)
            .scoreBtoA(DEFAULT_SCORE_BTO_A)
            .scoreTotal(DEFAULT_SCORE_TOTAL)
            .genderWeight(DEFAULT_GENDER_WEIGHT)
            .propertyA(DEFAULT_PROPERTY_A)
            .propertyB(DEFAULT_PROPERTY_B);
        return matchRecord;
    }

    @Before
    public void initTest() {
        matchRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createMatchRecord() throws Exception {
        int databaseSizeBeforeCreate = matchRecordRepository.findAll().size();

        // Create the MatchRecord
        restMatchRecordMockMvc.perform(post("/api/match-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(matchRecord)))
            .andExpect(status().isCreated());

        // Validate the MatchRecord in the database
        List<MatchRecord> matchRecordList = matchRecordRepository.findAll();
        assertThat(matchRecordList).hasSize(databaseSizeBeforeCreate + 1);
        MatchRecord testMatchRecord = matchRecordList.get(matchRecordList.size() - 1);
        assertThat(testMatchRecord.getRecordID()).isEqualTo(DEFAULT_RECORD_ID);
        assertThat(testMatchRecord.getUserA()).isEqualTo(DEFAULT_USER_A);
        assertThat(testMatchRecord.getUserB()).isEqualTo(DEFAULT_USER_B);
        assertThat(testMatchRecord.getAlgorithmID()).isEqualTo(DEFAULT_ALGORITHM_ID);
        assertThat(testMatchRecord.getInitiatorType()).isEqualTo(DEFAULT_INITIATOR_TYPE);
        assertThat(testMatchRecord.getScoreAtoB()).isEqualTo(DEFAULT_SCORE_ATO_B);
        assertThat(testMatchRecord.getScoreBtoA()).isEqualTo(DEFAULT_SCORE_BTO_A);
        assertThat(testMatchRecord.getScoreTotal()).isEqualTo(DEFAULT_SCORE_TOTAL);
        assertThat(testMatchRecord.getGenderWeight()).isEqualTo(DEFAULT_GENDER_WEIGHT);
        assertThat(testMatchRecord.getPropertyA()).isEqualTo(DEFAULT_PROPERTY_A);
        assertThat(testMatchRecord.getPropertyB()).isEqualTo(DEFAULT_PROPERTY_B);
    }

    @Test
    @Transactional
    public void createMatchRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = matchRecordRepository.findAll().size();

        // Create the MatchRecord with an existing ID
        matchRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchRecordMockMvc.perform(post("/api/match-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(matchRecord)))
            .andExpect(status().isBadRequest());

        // Validate the MatchRecord in the database
        List<MatchRecord> matchRecordList = matchRecordRepository.findAll();
        assertThat(matchRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMatchRecords() throws Exception {
        // Initialize the database
        matchRecordRepository.saveAndFlush(matchRecord);

        // Get all the matchRecordList
        restMatchRecordMockMvc.perform(get("/api/match-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matchRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordID").value(hasItem(DEFAULT_RECORD_ID.toString())))
            .andExpect(jsonPath("$.[*].userA").value(hasItem(DEFAULT_USER_A.toString())))
            .andExpect(jsonPath("$.[*].userB").value(hasItem(DEFAULT_USER_B.toString())))
            .andExpect(jsonPath("$.[*].algorithmID").value(hasItem(DEFAULT_ALGORITHM_ID.toString())))
            .andExpect(jsonPath("$.[*].initiatorType").value(hasItem(DEFAULT_INITIATOR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].scoreAtoB").value(hasItem(DEFAULT_SCORE_ATO_B)))
            .andExpect(jsonPath("$.[*].scoreBtoA").value(hasItem(DEFAULT_SCORE_BTO_A)))
            .andExpect(jsonPath("$.[*].scoreTotal").value(hasItem(DEFAULT_SCORE_TOTAL)))
            .andExpect(jsonPath("$.[*].genderWeight").value(hasItem(DEFAULT_GENDER_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].propertyA").value(hasItem(DEFAULT_PROPERTY_A.toString())))
            .andExpect(jsonPath("$.[*].propertyB").value(hasItem(DEFAULT_PROPERTY_B.toString())));
    }
    
    @Test
    @Transactional
    public void getMatchRecord() throws Exception {
        // Initialize the database
        matchRecordRepository.saveAndFlush(matchRecord);

        // Get the matchRecord
        restMatchRecordMockMvc.perform(get("/api/match-records/{id}", matchRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(matchRecord.getId().intValue()))
            .andExpect(jsonPath("$.recordID").value(DEFAULT_RECORD_ID.toString()))
            .andExpect(jsonPath("$.userA").value(DEFAULT_USER_A.toString()))
            .andExpect(jsonPath("$.userB").value(DEFAULT_USER_B.toString()))
            .andExpect(jsonPath("$.algorithmID").value(DEFAULT_ALGORITHM_ID.toString()))
            .andExpect(jsonPath("$.initiatorType").value(DEFAULT_INITIATOR_TYPE.toString()))
            .andExpect(jsonPath("$.scoreAtoB").value(DEFAULT_SCORE_ATO_B))
            .andExpect(jsonPath("$.scoreBtoA").value(DEFAULT_SCORE_BTO_A))
            .andExpect(jsonPath("$.scoreTotal").value(DEFAULT_SCORE_TOTAL))
            .andExpect(jsonPath("$.genderWeight").value(DEFAULT_GENDER_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.propertyA").value(DEFAULT_PROPERTY_A.toString()))
            .andExpect(jsonPath("$.propertyB").value(DEFAULT_PROPERTY_B.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMatchRecord() throws Exception {
        // Get the matchRecord
        restMatchRecordMockMvc.perform(get("/api/match-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatchRecord() throws Exception {
        // Initialize the database
        matchRecordRepository.saveAndFlush(matchRecord);

        int databaseSizeBeforeUpdate = matchRecordRepository.findAll().size();

        // Update the matchRecord
        MatchRecord updatedMatchRecord = matchRecordRepository.findById(matchRecord.getId()).get();
        // Disconnect from session so that the updates on updatedMatchRecord are not directly saved in db
        em.detach(updatedMatchRecord);
        updatedMatchRecord
            .recordID(UPDATED_RECORD_ID)
            .userA(UPDATED_USER_A)
            .userB(UPDATED_USER_B)
            .algorithmID(UPDATED_ALGORITHM_ID)
            .initiatorType(UPDATED_INITIATOR_TYPE)
            .scoreAtoB(UPDATED_SCORE_ATO_B)
            .scoreBtoA(UPDATED_SCORE_BTO_A)
            .scoreTotal(UPDATED_SCORE_TOTAL)
            .genderWeight(UPDATED_GENDER_WEIGHT)
            .propertyA(UPDATED_PROPERTY_A)
            .propertyB(UPDATED_PROPERTY_B);

        restMatchRecordMockMvc.perform(put("/api/match-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMatchRecord)))
            .andExpect(status().isOk());

        // Validate the MatchRecord in the database
        List<MatchRecord> matchRecordList = matchRecordRepository.findAll();
        assertThat(matchRecordList).hasSize(databaseSizeBeforeUpdate);
        MatchRecord testMatchRecord = matchRecordList.get(matchRecordList.size() - 1);
        assertThat(testMatchRecord.getRecordID()).isEqualTo(UPDATED_RECORD_ID);
        assertThat(testMatchRecord.getUserA()).isEqualTo(UPDATED_USER_A);
        assertThat(testMatchRecord.getUserB()).isEqualTo(UPDATED_USER_B);
        assertThat(testMatchRecord.getAlgorithmID()).isEqualTo(UPDATED_ALGORITHM_ID);
        assertThat(testMatchRecord.getInitiatorType()).isEqualTo(UPDATED_INITIATOR_TYPE);
        assertThat(testMatchRecord.getScoreAtoB()).isEqualTo(UPDATED_SCORE_ATO_B);
        assertThat(testMatchRecord.getScoreBtoA()).isEqualTo(UPDATED_SCORE_BTO_A);
        assertThat(testMatchRecord.getScoreTotal()).isEqualTo(UPDATED_SCORE_TOTAL);
        assertThat(testMatchRecord.getGenderWeight()).isEqualTo(UPDATED_GENDER_WEIGHT);
        assertThat(testMatchRecord.getPropertyA()).isEqualTo(UPDATED_PROPERTY_A);
        assertThat(testMatchRecord.getPropertyB()).isEqualTo(UPDATED_PROPERTY_B);
    }

    @Test
    @Transactional
    public void updateNonExistingMatchRecord() throws Exception {
        int databaseSizeBeforeUpdate = matchRecordRepository.findAll().size();

        // Create the MatchRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchRecordMockMvc.perform(put("/api/match-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(matchRecord)))
            .andExpect(status().isBadRequest());

        // Validate the MatchRecord in the database
        List<MatchRecord> matchRecordList = matchRecordRepository.findAll();
        assertThat(matchRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMatchRecord() throws Exception {
        // Initialize the database
        matchRecordRepository.saveAndFlush(matchRecord);

        int databaseSizeBeforeDelete = matchRecordRepository.findAll().size();

        // Get the matchRecord
        restMatchRecordMockMvc.perform(delete("/api/match-records/{id}", matchRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MatchRecord> matchRecordList = matchRecordRepository.findAll();
        assertThat(matchRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchRecord.class);
        MatchRecord matchRecord1 = new MatchRecord();
        matchRecord1.setId(1L);
        MatchRecord matchRecord2 = new MatchRecord();
        matchRecord2.setId(matchRecord1.getId());
        assertThat(matchRecord1).isEqualTo(matchRecord2);
        matchRecord2.setId(2L);
        assertThat(matchRecord1).isNotEqualTo(matchRecord2);
        matchRecord1.setId(null);
        assertThat(matchRecord1).isNotEqualTo(matchRecord2);
    }
}
