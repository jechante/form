package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.BaseProperty;
import com.schinta.repository.BasePropertyRepository;
import com.schinta.service.BasePropertyService;
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

import com.schinta.domain.enumeration.PropertyType;
import com.schinta.domain.enumeration.FormyType;
/**
 * Test class for the BasePropertyResource REST controller.
 *
 * @see BasePropertyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class BasePropertyResourceIntTest {

    private static final String DEFAULT_PROPERTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY_DESCRIBE = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_DESCRIBE = "BBBBBBBBBB";

    private static final PropertyType DEFAULT_PROPERTY_TYPE = PropertyType.BASIC;
    private static final PropertyType UPDATED_PROPERTY_TYPE = PropertyType.VALUES;

    private static final Integer DEFAULT_PROPERTY_SCORE = 1;
    private static final Integer UPDATED_PROPERTY_SCORE = 2;

    private static final Integer DEFAULT_PROPERTY_MAX_SCORE = 1;
    private static final Integer UPDATED_PROPERTY_MAX_SCORE = 2;

    private static final FormyType DEFAULT_FORMY_TYPE = FormyType.ONETOONE;
    private static final FormyType UPDATED_FORMY_TYPE = FormyType.ONETOMANY;

    private static final Integer DEFAULT_COMPLETION_RATE = 1;
    private static final Integer UPDATED_COMPLETION_RATE = 2;

    @Autowired
    private BasePropertyRepository basePropertyRepository;
    
    @Autowired
    private BasePropertyService basePropertyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBasePropertyMockMvc;

    private BaseProperty baseProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BasePropertyResource basePropertyResource = new BasePropertyResource(basePropertyService, basePropertyRepository);
        this.restBasePropertyMockMvc = MockMvcBuilders.standaloneSetup(basePropertyResource)
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
    public static BaseProperty createEntity(EntityManager em) {
        BaseProperty baseProperty = new BaseProperty()
            .propertyName(DEFAULT_PROPERTY_NAME)
            .propertyDescribe(DEFAULT_PROPERTY_DESCRIBE)
            .propertyType(DEFAULT_PROPERTY_TYPE)
            .propertyScore(DEFAULT_PROPERTY_SCORE)
            .propertyMaxScore(DEFAULT_PROPERTY_MAX_SCORE)
            .formyType(DEFAULT_FORMY_TYPE)
            .completionRate(DEFAULT_COMPLETION_RATE);
        return baseProperty;
    }

    @Before
    public void initTest() {
        baseProperty = createEntity(em);
    }

    @Test
    @Transactional
    public void createBaseProperty() throws Exception {
        int databaseSizeBeforeCreate = basePropertyRepository.findAll().size();

        // Create the BaseProperty
        restBasePropertyMockMvc.perform(post("/api/base-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseProperty)))
            .andExpect(status().isCreated());

        // Validate the BaseProperty in the database
        List<BaseProperty> basePropertyList = basePropertyRepository.findAll();
        assertThat(basePropertyList).hasSize(databaseSizeBeforeCreate + 1);
        BaseProperty testBaseProperty = basePropertyList.get(basePropertyList.size() - 1);
        assertThat(testBaseProperty.getPropertyName()).isEqualTo(DEFAULT_PROPERTY_NAME);
        assertThat(testBaseProperty.getPropertyDescribe()).isEqualTo(DEFAULT_PROPERTY_DESCRIBE);
        assertThat(testBaseProperty.getPropertyType()).isEqualTo(DEFAULT_PROPERTY_TYPE);
        assertThat(testBaseProperty.getPropertyScore()).isEqualTo(DEFAULT_PROPERTY_SCORE);
        assertThat(testBaseProperty.getPropertyMaxScore()).isEqualTo(DEFAULT_PROPERTY_MAX_SCORE);
        assertThat(testBaseProperty.getFormyType()).isEqualTo(DEFAULT_FORMY_TYPE);
        assertThat(testBaseProperty.getCompletionRate()).isEqualTo(DEFAULT_COMPLETION_RATE);
    }

    @Test
    @Transactional
    public void createBasePropertyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = basePropertyRepository.findAll().size();

        // Create the BaseProperty with an existing ID
        baseProperty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBasePropertyMockMvc.perform(post("/api/base-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseProperty)))
            .andExpect(status().isBadRequest());

        // Validate the BaseProperty in the database
        List<BaseProperty> basePropertyList = basePropertyRepository.findAll();
        assertThat(basePropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPropertyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = basePropertyRepository.findAll().size();
        // set the field null
        baseProperty.setPropertyName(null);

        // Create the BaseProperty, which fails.

        restBasePropertyMockMvc.perform(post("/api/base-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseProperty)))
            .andExpect(status().isBadRequest());

        List<BaseProperty> basePropertyList = basePropertyRepository.findAll();
        assertThat(basePropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBaseProperties() throws Exception {
        // Initialize the database
        basePropertyRepository.saveAndFlush(baseProperty);

        // Get all the basePropertyList
        restBasePropertyMockMvc.perform(get("/api/base-properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baseProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].propertyName").value(hasItem(DEFAULT_PROPERTY_NAME.toString())))
            .andExpect(jsonPath("$.[*].propertyDescribe").value(hasItem(DEFAULT_PROPERTY_DESCRIBE.toString())))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].propertyScore").value(hasItem(DEFAULT_PROPERTY_SCORE)))
            .andExpect(jsonPath("$.[*].propertyMaxScore").value(hasItem(DEFAULT_PROPERTY_MAX_SCORE)))
            .andExpect(jsonPath("$.[*].formyType").value(hasItem(DEFAULT_FORMY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].completionRate").value(hasItem(DEFAULT_COMPLETION_RATE)));
    }
    
    @Test
    @Transactional
    public void getBaseProperty() throws Exception {
        // Initialize the database
        basePropertyRepository.saveAndFlush(baseProperty);

        // Get the baseProperty
        restBasePropertyMockMvc.perform(get("/api/base-properties/{id}", baseProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(baseProperty.getId().intValue()))
            .andExpect(jsonPath("$.propertyName").value(DEFAULT_PROPERTY_NAME.toString()))
            .andExpect(jsonPath("$.propertyDescribe").value(DEFAULT_PROPERTY_DESCRIBE.toString()))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()))
            .andExpect(jsonPath("$.propertyScore").value(DEFAULT_PROPERTY_SCORE))
            .andExpect(jsonPath("$.propertyMaxScore").value(DEFAULT_PROPERTY_MAX_SCORE))
            .andExpect(jsonPath("$.formyType").value(DEFAULT_FORMY_TYPE.toString()))
            .andExpect(jsonPath("$.completionRate").value(DEFAULT_COMPLETION_RATE));
    }

    @Test
    @Transactional
    public void getNonExistingBaseProperty() throws Exception {
        // Get the baseProperty
        restBasePropertyMockMvc.perform(get("/api/base-properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBaseProperty() throws Exception {
        // Initialize the database
        basePropertyService.save(baseProperty);

        int databaseSizeBeforeUpdate = basePropertyRepository.findAll().size();

        // Update the baseProperty
        BaseProperty updatedBaseProperty = basePropertyRepository.findById(baseProperty.getId()).get();
        // Disconnect from session so that the updates on updatedBaseProperty are not directly saved in db
        em.detach(updatedBaseProperty);
        updatedBaseProperty
            .propertyName(UPDATED_PROPERTY_NAME)
            .propertyDescribe(UPDATED_PROPERTY_DESCRIBE)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .propertyScore(UPDATED_PROPERTY_SCORE)
            .propertyMaxScore(UPDATED_PROPERTY_MAX_SCORE)
            .formyType(UPDATED_FORMY_TYPE)
            .completionRate(UPDATED_COMPLETION_RATE);

        restBasePropertyMockMvc.perform(put("/api/base-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBaseProperty)))
            .andExpect(status().isOk());

        // Validate the BaseProperty in the database
        List<BaseProperty> basePropertyList = basePropertyRepository.findAll();
        assertThat(basePropertyList).hasSize(databaseSizeBeforeUpdate);
        BaseProperty testBaseProperty = basePropertyList.get(basePropertyList.size() - 1);
        assertThat(testBaseProperty.getPropertyName()).isEqualTo(UPDATED_PROPERTY_NAME);
        assertThat(testBaseProperty.getPropertyDescribe()).isEqualTo(UPDATED_PROPERTY_DESCRIBE);
        assertThat(testBaseProperty.getPropertyType()).isEqualTo(UPDATED_PROPERTY_TYPE);
        assertThat(testBaseProperty.getPropertyScore()).isEqualTo(UPDATED_PROPERTY_SCORE);
        assertThat(testBaseProperty.getPropertyMaxScore()).isEqualTo(UPDATED_PROPERTY_MAX_SCORE);
        assertThat(testBaseProperty.getFormyType()).isEqualTo(UPDATED_FORMY_TYPE);
        assertThat(testBaseProperty.getCompletionRate()).isEqualTo(UPDATED_COMPLETION_RATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBaseProperty() throws Exception {
        int databaseSizeBeforeUpdate = basePropertyRepository.findAll().size();

        // Create the BaseProperty

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasePropertyMockMvc.perform(put("/api/base-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseProperty)))
            .andExpect(status().isBadRequest());

        // Validate the BaseProperty in the database
        List<BaseProperty> basePropertyList = basePropertyRepository.findAll();
        assertThat(basePropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBaseProperty() throws Exception {
        // Initialize the database
        basePropertyService.save(baseProperty);

        int databaseSizeBeforeDelete = basePropertyRepository.findAll().size();

        // Get the baseProperty
        restBasePropertyMockMvc.perform(delete("/api/base-properties/{id}", baseProperty.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BaseProperty> basePropertyList = basePropertyRepository.findAll();
        assertThat(basePropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaseProperty.class);
        BaseProperty baseProperty1 = new BaseProperty();
        baseProperty1.setId(1L);
        BaseProperty baseProperty2 = new BaseProperty();
        baseProperty2.setId(baseProperty1.getId());
        assertThat(baseProperty1).isEqualTo(baseProperty2);
        baseProperty2.setId(2L);
        assertThat(baseProperty1).isNotEqualTo(baseProperty2);
        baseProperty1.setId(null);
        assertThat(baseProperty1).isNotEqualTo(baseProperty2);
    }
}
