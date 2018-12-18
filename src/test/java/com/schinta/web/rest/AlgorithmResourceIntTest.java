package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.Algorithm;
import com.schinta.repository.AlgorithmRepository;
import com.schinta.service.AlgorithmService;
import com.schinta.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.schinta.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_GENDER_WEIGHT = 1D;
    private static final Double UPDATED_GENDER_WEIGHT = 2D;

    private static final Integer DEFAULT_K_VALUE = 1;
    private static final Integer UPDATED_K_VALUE = 2;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Mock
    private AlgorithmRepository algorithmRepositoryMock;
    

    @Mock
    private AlgorithmService algorithmServiceMock;

    @Autowired
    private AlgorithmService algorithmService;

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
        final AlgorithmResource algorithmResource = new AlgorithmResource(algorithmService);
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
            .name(DEFAULT_NAME)
            .genderWeight(DEFAULT_GENDER_WEIGHT)
            .kValue(DEFAULT_K_VALUE)
            .remark(DEFAULT_REMARK)
            .enabled(DEFAULT_ENABLED);
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
        assertThat(testAlgorithm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlgorithm.getGenderWeight()).isEqualTo(DEFAULT_GENDER_WEIGHT);
        assertThat(testAlgorithm.getkValue()).isEqualTo(DEFAULT_K_VALUE);
        assertThat(testAlgorithm.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testAlgorithm.isEnabled()).isEqualTo(DEFAULT_ENABLED);
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
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = algorithmRepository.findAll().size();
        // set the field null
        algorithm.setName(null);

        // Create the Algorithm, which fails.

        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithm)))
            .andExpect(status().isBadRequest());

        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].genderWeight").value(hasItem(DEFAULT_GENDER_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].kValue").value(hasItem(DEFAULT_K_VALUE)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    public void getAllAlgorithmsWithEagerRelationshipsIsEnabled() throws Exception {
        AlgorithmResource algorithmResource = new AlgorithmResource(algorithmServiceMock);
        when(algorithmServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restAlgorithmMockMvc = MockMvcBuilders.standaloneSetup(algorithmResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAlgorithmMockMvc.perform(get("/api/algorithms?eagerload=true"))
        .andExpect(status().isOk());

        verify(algorithmServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllAlgorithmsWithEagerRelationshipsIsNotEnabled() throws Exception {
        AlgorithmResource algorithmResource = new AlgorithmResource(algorithmServiceMock);
            when(algorithmServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restAlgorithmMockMvc = MockMvcBuilders.standaloneSetup(algorithmResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAlgorithmMockMvc.perform(get("/api/algorithms?eagerload=true"))
        .andExpect(status().isOk());

            verify(algorithmServiceMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.genderWeight").value(DEFAULT_GENDER_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.kValue").value(DEFAULT_K_VALUE))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
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
        algorithmService.save(algorithm);

        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Update the algorithm
        Algorithm updatedAlgorithm = algorithmRepository.findById(algorithm.getId()).get();
        // Disconnect from session so that the updates on updatedAlgorithm are not directly saved in db
        em.detach(updatedAlgorithm);
        updatedAlgorithm
            .name(UPDATED_NAME)
            .genderWeight(UPDATED_GENDER_WEIGHT)
            .kValue(UPDATED_K_VALUE)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);

        restAlgorithmMockMvc.perform(put("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlgorithm)))
            .andExpect(status().isOk());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlgorithm.getGenderWeight()).isEqualTo(UPDATED_GENDER_WEIGHT);
        assertThat(testAlgorithm.getkValue()).isEqualTo(UPDATED_K_VALUE);
        assertThat(testAlgorithm.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testAlgorithm.isEnabled()).isEqualTo(UPDATED_ENABLED);
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
        algorithmService.save(algorithm);

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
