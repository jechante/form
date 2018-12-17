package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.BaseForm;
import com.schinta.repository.BaseFormRepository;
import com.schinta.service.BaseFormService;
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

import com.schinta.domain.enumeration.FormVendor;
/**
 * Test class for the BaseFormResource REST controller.
 *
 * @see BaseFormResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class BaseFormResourceIntTest {

    private static final String DEFAULT_FORM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FORM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FORM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_DESCRIBE = "AAAAAAAAAA";
    private static final String UPDATED_FORM_DESCRIBE = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_WEB = "AAAAAAAAAA";
    private static final String UPDATED_FORM_WEB = "BBBBBBBBBB";

    private static final String DEFAULT_SUBMIT_URL = "AAAAAAAAAA";
    private static final String UPDATED_SUBMIT_URL = "BBBBBBBBBB";

    private static final FormVendor DEFAULT_VENDOR = FormVendor.SELF;
    private static final FormVendor UPDATED_VENDOR = FormVendor.JIN;

    @Autowired
    private BaseFormRepository baseFormRepository;
    
    @Autowired
    private BaseFormService baseFormService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBaseFormMockMvc;

    private BaseForm baseForm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BaseFormResource baseFormResource = new BaseFormResource(baseFormService);
        this.restBaseFormMockMvc = MockMvcBuilders.standaloneSetup(baseFormResource)
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
    public static BaseForm createEntity(EntityManager em) {
        BaseForm baseForm = new BaseForm()
            .formCode(DEFAULT_FORM_CODE)
            .formName(DEFAULT_FORM_NAME)
            .formDescribe(DEFAULT_FORM_DESCRIBE)
            .formWeb(DEFAULT_FORM_WEB)
            .submitUrl(DEFAULT_SUBMIT_URL)
            .vendor(DEFAULT_VENDOR);
        return baseForm;
    }

    @Before
    public void initTest() {
        baseForm = createEntity(em);
    }

    @Test
    @Transactional
    public void createBaseForm() throws Exception {
        int databaseSizeBeforeCreate = baseFormRepository.findAll().size();

        // Create the BaseForm
        restBaseFormMockMvc.perform(post("/api/base-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseForm)))
            .andExpect(status().isCreated());

        // Validate the BaseForm in the database
        List<BaseForm> baseFormList = baseFormRepository.findAll();
        assertThat(baseFormList).hasSize(databaseSizeBeforeCreate + 1);
        BaseForm testBaseForm = baseFormList.get(baseFormList.size() - 1);
        assertThat(testBaseForm.getFormCode()).isEqualTo(DEFAULT_FORM_CODE);
        assertThat(testBaseForm.getFormName()).isEqualTo(DEFAULT_FORM_NAME);
        assertThat(testBaseForm.getFormDescribe()).isEqualTo(DEFAULT_FORM_DESCRIBE);
        assertThat(testBaseForm.getFormWeb()).isEqualTo(DEFAULT_FORM_WEB);
        assertThat(testBaseForm.getSubmitUrl()).isEqualTo(DEFAULT_SUBMIT_URL);
        assertThat(testBaseForm.getVendor()).isEqualTo(DEFAULT_VENDOR);
    }

    @Test
    @Transactional
    public void createBaseFormWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = baseFormRepository.findAll().size();

        // Create the BaseForm with an existing ID
        baseForm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaseFormMockMvc.perform(post("/api/base-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseForm)))
            .andExpect(status().isBadRequest());

        // Validate the BaseForm in the database
        List<BaseForm> baseFormList = baseFormRepository.findAll();
        assertThat(baseFormList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFormCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = baseFormRepository.findAll().size();
        // set the field null
        baseForm.setFormCode(null);

        // Create the BaseForm, which fails.

        restBaseFormMockMvc.perform(post("/api/base-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseForm)))
            .andExpect(status().isBadRequest());

        List<BaseForm> baseFormList = baseFormRepository.findAll();
        assertThat(baseFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBaseForms() throws Exception {
        // Initialize the database
        baseFormRepository.saveAndFlush(baseForm);

        // Get all the baseFormList
        restBaseFormMockMvc.perform(get("/api/base-forms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baseForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].formCode").value(hasItem(DEFAULT_FORM_CODE.toString())))
            .andExpect(jsonPath("$.[*].formName").value(hasItem(DEFAULT_FORM_NAME.toString())))
            .andExpect(jsonPath("$.[*].formDescribe").value(hasItem(DEFAULT_FORM_DESCRIBE.toString())))
            .andExpect(jsonPath("$.[*].formWeb").value(hasItem(DEFAULT_FORM_WEB.toString())))
            .andExpect(jsonPath("$.[*].submitUrl").value(hasItem(DEFAULT_SUBMIT_URL.toString())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR.toString())));
    }
    
    @Test
    @Transactional
    public void getBaseForm() throws Exception {
        // Initialize the database
        baseFormRepository.saveAndFlush(baseForm);

        // Get the baseForm
        restBaseFormMockMvc.perform(get("/api/base-forms/{id}", baseForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(baseForm.getId().intValue()))
            .andExpect(jsonPath("$.formCode").value(DEFAULT_FORM_CODE.toString()))
            .andExpect(jsonPath("$.formName").value(DEFAULT_FORM_NAME.toString()))
            .andExpect(jsonPath("$.formDescribe").value(DEFAULT_FORM_DESCRIBE.toString()))
            .andExpect(jsonPath("$.formWeb").value(DEFAULT_FORM_WEB.toString()))
            .andExpect(jsonPath("$.submitUrl").value(DEFAULT_SUBMIT_URL.toString()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBaseForm() throws Exception {
        // Get the baseForm
        restBaseFormMockMvc.perform(get("/api/base-forms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBaseForm() throws Exception {
        // Initialize the database
        baseFormService.save(baseForm);

        int databaseSizeBeforeUpdate = baseFormRepository.findAll().size();

        // Update the baseForm
        BaseForm updatedBaseForm = baseFormRepository.findById(baseForm.getId()).get();
        // Disconnect from session so that the updates on updatedBaseForm are not directly saved in db
        em.detach(updatedBaseForm);
        updatedBaseForm
            .formCode(UPDATED_FORM_CODE)
            .formName(UPDATED_FORM_NAME)
            .formDescribe(UPDATED_FORM_DESCRIBE)
            .formWeb(UPDATED_FORM_WEB)
            .submitUrl(UPDATED_SUBMIT_URL)
            .vendor(UPDATED_VENDOR);

        restBaseFormMockMvc.perform(put("/api/base-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBaseForm)))
            .andExpect(status().isOk());

        // Validate the BaseForm in the database
        List<BaseForm> baseFormList = baseFormRepository.findAll();
        assertThat(baseFormList).hasSize(databaseSizeBeforeUpdate);
        BaseForm testBaseForm = baseFormList.get(baseFormList.size() - 1);
        assertThat(testBaseForm.getFormCode()).isEqualTo(UPDATED_FORM_CODE);
        assertThat(testBaseForm.getFormName()).isEqualTo(UPDATED_FORM_NAME);
        assertThat(testBaseForm.getFormDescribe()).isEqualTo(UPDATED_FORM_DESCRIBE);
        assertThat(testBaseForm.getFormWeb()).isEqualTo(UPDATED_FORM_WEB);
        assertThat(testBaseForm.getSubmitUrl()).isEqualTo(UPDATED_SUBMIT_URL);
        assertThat(testBaseForm.getVendor()).isEqualTo(UPDATED_VENDOR);
    }

    @Test
    @Transactional
    public void updateNonExistingBaseForm() throws Exception {
        int databaseSizeBeforeUpdate = baseFormRepository.findAll().size();

        // Create the BaseForm

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaseFormMockMvc.perform(put("/api/base-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(baseForm)))
            .andExpect(status().isBadRequest());

        // Validate the BaseForm in the database
        List<BaseForm> baseFormList = baseFormRepository.findAll();
        assertThat(baseFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBaseForm() throws Exception {
        // Initialize the database
        baseFormService.save(baseForm);

        int databaseSizeBeforeDelete = baseFormRepository.findAll().size();

        // Get the baseForm
        restBaseFormMockMvc.perform(delete("/api/base-forms/{id}", baseForm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BaseForm> baseFormList = baseFormRepository.findAll();
        assertThat(baseFormList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaseForm.class);
        BaseForm baseForm1 = new BaseForm();
        baseForm1.setId(1L);
        BaseForm baseForm2 = new BaseForm();
        baseForm2.setId(baseForm1.getId());
        assertThat(baseForm1).isEqualTo(baseForm2);
        baseForm2.setId(2L);
        assertThat(baseForm1).isNotEqualTo(baseForm2);
        baseForm1.setId(null);
        assertThat(baseForm1).isNotEqualTo(baseForm2);
    }
}
