package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.FormField;
import com.schinta.repository.FormFieldRepository;
import com.schinta.service.FormFieldService;
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

import com.schinta.domain.enumeration.FieldType;
/**
 * Test class for the FormFieldResource REST controller.
 *
 * @see FormFieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class FormFieldResourceIntTest {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_DESC = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_DESC = "BBBBBBBBBB";

    private static final FieldType DEFAULT_FIELD_TYPE = FieldType.PROPERTY;
    private static final FieldType UPDATED_FIELD_TYPE = FieldType.DEMAND;

    @Autowired
    private FormFieldRepository formFieldRepository;
    
    @Autowired
    private FormFieldService formFieldService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormFieldMockMvc;

    private FormField formField;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormFieldResource formFieldResource = new FormFieldResource(formFieldService);
        this.restFormFieldMockMvc = MockMvcBuilders.standaloneSetup(formFieldResource)
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
    public static FormField createEntity(EntityManager em) {
        FormField formField = new FormField()
            .fieldName(DEFAULT_FIELD_NAME)
            .fieldDesc(DEFAULT_FIELD_DESC)
            .fieldType(DEFAULT_FIELD_TYPE);
        return formField;
    }

    @Before
    public void initTest() {
        formField = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormField() throws Exception {
        int databaseSizeBeforeCreate = formFieldRepository.findAll().size();

        // Create the FormField
        restFormFieldMockMvc.perform(post("/api/form-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formField)))
            .andExpect(status().isCreated());

        // Validate the FormField in the database
        List<FormField> formFieldList = formFieldRepository.findAll();
        assertThat(formFieldList).hasSize(databaseSizeBeforeCreate + 1);
        FormField testFormField = formFieldList.get(formFieldList.size() - 1);
        assertThat(testFormField.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testFormField.getFieldDesc()).isEqualTo(DEFAULT_FIELD_DESC);
        assertThat(testFormField.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
    }

    @Test
    @Transactional
    public void createFormFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formFieldRepository.findAll().size();

        // Create the FormField with an existing ID
        formField.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormFieldMockMvc.perform(post("/api/form-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formField)))
            .andExpect(status().isBadRequest());

        // Validate the FormField in the database
        List<FormField> formFieldList = formFieldRepository.findAll();
        assertThat(formFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = formFieldRepository.findAll().size();
        // set the field null
        formField.setFieldName(null);

        // Create the FormField, which fails.

        restFormFieldMockMvc.perform(post("/api/form-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formField)))
            .andExpect(status().isBadRequest());

        List<FormField> formFieldList = formFieldRepository.findAll();
        assertThat(formFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormFields() throws Exception {
        // Initialize the database
        formFieldRepository.saveAndFlush(formField);

        // Get all the formFieldList
        restFormFieldMockMvc.perform(get("/api/form-fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME.toString())))
            .andExpect(jsonPath("$.[*].fieldDesc").value(hasItem(DEFAULT_FIELD_DESC.toString())))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getFormField() throws Exception {
        // Initialize the database
        formFieldRepository.saveAndFlush(formField);

        // Get the formField
        restFormFieldMockMvc.perform(get("/api/form-fields/{id}", formField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formField.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME.toString()))
            .andExpect(jsonPath("$.fieldDesc").value(DEFAULT_FIELD_DESC.toString()))
            .andExpect(jsonPath("$.fieldType").value(DEFAULT_FIELD_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormField() throws Exception {
        // Get the formField
        restFormFieldMockMvc.perform(get("/api/form-fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormField() throws Exception {
        // Initialize the database
        formFieldService.save(formField);

        int databaseSizeBeforeUpdate = formFieldRepository.findAll().size();

        // Update the formField
        FormField updatedFormField = formFieldRepository.findById(formField.getId()).get();
        // Disconnect from session so that the updates on updatedFormField are not directly saved in db
        em.detach(updatedFormField);
        updatedFormField
            .fieldName(UPDATED_FIELD_NAME)
            .fieldDesc(UPDATED_FIELD_DESC)
            .fieldType(UPDATED_FIELD_TYPE);

        restFormFieldMockMvc.perform(put("/api/form-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormField)))
            .andExpect(status().isOk());

        // Validate the FormField in the database
        List<FormField> formFieldList = formFieldRepository.findAll();
        assertThat(formFieldList).hasSize(databaseSizeBeforeUpdate);
        FormField testFormField = formFieldList.get(formFieldList.size() - 1);
        assertThat(testFormField.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testFormField.getFieldDesc()).isEqualTo(UPDATED_FIELD_DESC);
        assertThat(testFormField.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFormField() throws Exception {
        int databaseSizeBeforeUpdate = formFieldRepository.findAll().size();

        // Create the FormField

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormFieldMockMvc.perform(put("/api/form-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formField)))
            .andExpect(status().isBadRequest());

        // Validate the FormField in the database
        List<FormField> formFieldList = formFieldRepository.findAll();
        assertThat(formFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFormField() throws Exception {
        // Initialize the database
        formFieldService.save(formField);

        int databaseSizeBeforeDelete = formFieldRepository.findAll().size();

        // Get the formField
        restFormFieldMockMvc.perform(delete("/api/form-fields/{id}", formField.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FormField> formFieldList = formFieldRepository.findAll();
        assertThat(formFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormField.class);
        FormField formField1 = new FormField();
        formField1.setId(1L);
        FormField formField2 = new FormField();
        formField2.setId(formField1.getId());
        assertThat(formField1).isEqualTo(formField2);
        formField2.setId(2L);
        assertThat(formField1).isNotEqualTo(formField2);
        formField1.setId(null);
        assertThat(formField1).isNotEqualTo(formField2);
    }
}
