package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.Algorithm;
import com.schinta.repository.AlgorithmRepository;
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
 * Test class for the AlgorithmResource REST controller.
 *
 * @see AlgorithmResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class AlgorithmResourceIntTest {

    private static final String DEFAULT_ALGORITHM_ID = "AAAAAAAAAA";
    private static final String UPDATED_ALGORITHM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FILTER_STRINGS = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_STRINGS = "BBBBBBBBBB";

    private static final String DEFAULT_SCORE_STRINGS = "AAAAAAAAAA";
    private static final String UPDATED_SCORE_STRINGS = "BBBBBBBBBB";

    private static final Double DEFAULT_GENDER_WEIGHT = 1D;
    private static final Double UPDATED_GENDER_WEIGHT = 2D;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAlgorithmMockMvc;

    private Algorithm algorithm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlgorithmResource algorithmResource = new AlgorithmResource(algorithmRepository);
        this.restAlgorithmMockMvc = MockMvcBuilders.standaloneSetup(algorithmResource)
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
    public static Algorithm createEntity(EntityManager em) {
        Algorithm algorithm = new Algorithm()
            .algorithmID(DEFAULT_ALGORITHM_ID)
            .filterStrings(DEFAULT_FILTER_STRINGS)
            .scoreStrings(DEFAULT_SCORE_STRINGS)
            .genderWeight(DEFAULT_GENDER_WEIGHT)
            .remark(DEFAULT_REMARK);
        return algorithm;
    }

    @Before
    public void initTest() {
        algorithm = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlgorithm() throws Exception {
        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();

        // Create the Algorithm
        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithm)))
            .andExpect(status().isCreated());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate + 1);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getAlgorithmID()).isEqualTo(DEFAULT_ALGORITHM_ID);
        assertThat(testAlgorithm.getFilterStrings()).isEqualTo(DEFAULT_FILTER_STRINGS);
        assertThat(testAlgorithm.getScoreStrings()).isEqualTo(DEFAULT_SCORE_STRINGS);
        assertThat(testAlgorithm.getGenderWeight()).isEqualTo(DEFAULT_GENDER_WEIGHT);
        assertThat(testAlgorithm.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createAlgorithmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();

        // Create the Algorithm with an existing ID
        algorithm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithm)))
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAlgorithms() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList
        restAlgorithmMockMvc.perform(get("/api/algorithms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(algorithm.getId().intValue())))
            .andExpect(jsonPath("$.[*].algorithmID").value(hasItem(DEFAULT_ALGORITHM_ID.toString())))
            .andExpect(jsonPath("$.[*].filterStrings").value(hasItem(DEFAULT_FILTER_STRINGS.toString())))
            .andExpect(jsonPath("$.[*].scoreStrings").value(hasItem(DEFAULT_SCORE_STRINGS.toString())))
            .andExpect(jsonPath("$.[*].genderWeight").value(hasItem(DEFAULT_GENDER_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }
    
    @Test
    @Transactional
    public void getAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get the algorithm
        restAlgorithmMockMvc.perform(get("/api/algorithms/{id}", algorithm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(algorithm.getId().intValue()))
            .andExpect(jsonPath("$.algorithmID").value(DEFAULT_ALGORITHM_ID.toString()))
            .andExpect(jsonPath("$.filterStrings").value(DEFAULT_FILTER_STRINGS.toString()))
            .andExpect(jsonPath("$.scoreStrings").value(DEFAULT_SCORE_STRINGS.toString()))
            .andExpect(jsonPath("$.genderWeight").value(DEFAULT_GENDER_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlgorithm() throws Exception {
        // Get the algorithm
        restAlgorithmMockMvc.perform(get("/api/algorithms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Update the algorithm
        Algorithm updatedAlgorithm = algorithmRepository.findById(algorithm.getId()).get();
        // Disconnect from session so that the updates on updatedAlgorithm are not directly saved in db
        em.detach(updatedAlgorithm);
        updatedAlgorithm
            .algorithmID(UPDATED_ALGORITHM_ID)
            .filterStrings(UPDATED_FILTER_STRINGS)
            .scoreStrings(UPDATED_SCORE_STRINGS)
            .genderWeight(UPDATED_GENDER_WEIGHT)
            .remark(UPDATED_REMARK);

        restAlgorithmMockMvc.perform(put("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlgorithm)))
            .andExpect(status().isOk());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getAlgorithmID()).isEqualTo(UPDATED_ALGORITHM_ID);
        assertThat(testAlgorithm.getFilterStrings()).isEqualTo(UPDATED_FILTER_STRINGS);
        assertThat(testAlgorithm.getScoreStrings()).isEqualTo(UPDATED_SCORE_STRINGS);
        assertThat(testAlgorithm.getGenderWeight()).isEqualTo(UPDATED_GENDER_WEIGHT);
        assertThat(testAlgorithm.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Create the Algorithm

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc.perform(put("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithm)))
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeDelete = algorithmRepository.findAll().size();

        // Get the algorithm
        restAlgorithmMockMvc.perform(delete("/api/algorithms/{id}", algorithm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Algorithm.class);
        Algorithm algorithm1 = new Algorithm();
        algorithm1.setId(1L);
        Algorithm algorithm2 = new Algorithm();
        algorithm2.setId(algorithm1.getId());
        assertThat(algorithm1).isEqualTo(algorithm2);
        algorithm2.setId(2L);
        assertThat(algorithm1).isNotEqualTo(algorithm2);
        algorithm1.setId(null);
        assertThat(algorithm1).isNotEqualTo(algorithm2);
    }
}
