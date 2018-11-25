package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.FormToProperty;
import com.schinta.repository.FormToPropertyRepository;
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
 * Test class for the FormToPropertyResource REST controller.
 *
 * @see FormToPropertyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class FormToPropertyResourceIntTest {

    private static final Integer DEFAULT_FORM_ID = 1;
    private static final Integer UPDATED_FORM_ID = 2;

    private static final String DEFAULT_KEY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_KEY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_TYPE = "A";
    private static final String UPDATED_KEY_TYPE = "B";

    private static final String DEFAULT_PROPERTY_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_ID = "BBBBBBBBBB";

    @Autowired
    private FormToPropertyRepository formToPropertyRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormToPropertyMockMvc;

    private FormToProperty formToProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormToPropertyResource formToPropertyResource = new FormToPropertyResource(formToPropertyRepository);
        this.restFormToPropertyMockMvc = MockMvcBuilders.standaloneSetup(formToPropertyResource)
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
    public static FormToProperty createEntity(EntityManager em) {
        FormToProperty formToProperty = new FormToProperty()
            .formID(DEFAULT_FORM_ID)
            .keyName(DEFAULT_KEY_NAME)
            .keyType(DEFAULT_KEY_TYPE)
            .propertyId(DEFAULT_PROPERTY_ID);
        return formToProperty;
    }

    @Before
    public void initTest() {
        formToProperty = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormToProperty() throws Exception {
        int databaseSizeBeforeCreate = formToPropertyRepository.findAll().size();

        // Create the FormToProperty
        restFormToPropertyMockMvc.perform(post("/api/form-to-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formToProperty)))
            .andExpect(status().isCreated());

        // Validate the FormToProperty in the database
        List<FormToProperty> formToPropertyList = formToPropertyRepository.findAll();
        assertThat(formToPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        FormToProperty testFormToProperty = formToPropertyList.get(formToPropertyList.size() - 1);
        assertThat(testFormToProperty.getFormID()).isEqualTo(DEFAULT_FORM_ID);
        assertThat(testFormToProperty.getKeyName()).isEqualTo(DEFAULT_KEY_NAME);
        assertThat(testFormToProperty.getKeyType()).isEqualTo(DEFAULT_KEY_TYPE);
        assertThat(testFormToProperty.getPropertyId()).isEqualTo(DEFAULT_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void createFormToPropertyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formToPropertyRepository.findAll().size();

        // Create the FormToProperty with an existing ID
        formToProperty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormToPropertyMockMvc.perform(post("/api/form-to-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formToProperty)))
            .andExpect(status().isBadRequest());

        // Validate the FormToProperty in the database
        List<FormToProperty> formToPropertyList = formToPropertyRepository.findAll();
        assertThat(formToPropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFormToProperties() throws Exception {
        // Initialize the database
        formToPropertyRepository.saveAndFlush(formToProperty);

        // Get all the formToPropertyList
        restFormToPropertyMockMvc.perform(get("/api/form-to-properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formToProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].formID").value(hasItem(DEFAULT_FORM_ID)))
            .andExpect(jsonPath("$.[*].keyName").value(hasItem(DEFAULT_KEY_NAME.toString())))
            .andExpect(jsonPath("$.[*].keyType").value(hasItem(DEFAULT_KEY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].propertyId").value(hasItem(DEFAULT_PROPERTY_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getFormToProperty() throws Exception {
        // Initialize the database
        formToPropertyRepository.saveAndFlush(formToProperty);

        // Get the formToProperty
        restFormToPropertyMockMvc.perform(get("/api/form-to-properties/{id}", formToProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formToProperty.getId().intValue()))
            .andExpect(jsonPath("$.formID").value(DEFAULT_FORM_ID))
            .andExpect(jsonPath("$.keyName").value(DEFAULT_KEY_NAME.toString()))
            .andExpect(jsonPath("$.keyType").value(DEFAULT_KEY_TYPE.toString()))
            .andExpect(jsonPath("$.propertyId").value(DEFAULT_PROPERTY_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormToProperty() throws Exception {
        // Get the formToProperty
        restFormToPropertyMockMvc.perform(get("/api/form-to-properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormToProperty() throws Exception {
        // Initialize the database
        formToPropertyRepository.saveAndFlush(formToProperty);

        int databaseSizeBeforeUpdate = formToPropertyRepository.findAll().size();

        // Update the formToProperty
        FormToProperty updatedFormToProperty = formToPropertyRepository.findById(formToProperty.getId()).get();
        // Disconnect from session so that the updates on updatedFormToProperty are not directly saved in db
        em.detach(updatedFormToProperty);
        updatedFormToProperty
            .formID(UPDATED_FORM_ID)
            .keyName(UPDATED_KEY_NAME)
            .keyType(UPDATED_KEY_TYPE)
            .propertyId(UPDATED_PROPERTY_ID);

        restFormToPropertyMockMvc.perform(put("/api/form-to-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormToProperty)))
            .andExpect(status().isOk());

        // Validate the FormToProperty in the database
        List<FormToProperty> formToPropertyList = formToPropertyRepository.findAll();
        assertThat(formToPropertyList).hasSize(databaseSizeBeforeUpdate);
        FormToProperty testFormToProperty = formToPropertyList.get(formToPropertyList.size() - 1);
        assertThat(testFormToProperty.getFormID()).isEqualTo(UPDATED_FORM_ID);
        assertThat(testFormToProperty.getKeyName()).isEqualTo(UPDATED_KEY_NAME);
        assertThat(testFormToProperty.getKeyType()).isEqualTo(UPDATED_KEY_TYPE);
        assertThat(testFormToProperty.getPropertyId()).isEqualTo(UPDATED_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingFormToProperty() throws Exception {
        int databaseSizeBeforeUpdate = formToPropertyRepository.findAll().size();

        // Create the FormToProperty

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormToPropertyMockMvc.perform(put("/api/form-to-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formToProperty)))
            .andExpect(status().isBadRequest());

        // Validate the FormToProperty in the database
        List<FormToProperty> formToPropertyList = formToPropertyRepository.findAll();
        assertThat(formToPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFormToProperty() throws Exception {
        // Initialize the database
        formToPropertyRepository.saveAndFlush(formToProperty);

        int databaseSizeBeforeDelete = formToPropertyRepository.findAll().size();

        // Get the formToProperty
        restFormToPropertyMockMvc.perform(delete("/api/form-to-properties/{id}", formToProperty.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FormToProperty> formToPropertyList = formToPropertyRepository.findAll();
        assertThat(formToPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormToProperty.class);
        FormToProperty formToProperty1 = new FormToProperty();
        formToProperty1.setId(1L);
        FormToProperty formToProperty2 = new FormToProperty();
        formToProperty2.setId(formToProperty1.getId());
        assertThat(formToProperty1).isEqualTo(formToProperty2);
        formToProperty2.setId(2L);
        assertThat(formToProperty1).isNotEqualTo(formToProperty2);
        formToProperty1.setId(null);
        assertThat(formToProperty1).isNotEqualTo(formToProperty2);
    }
}
