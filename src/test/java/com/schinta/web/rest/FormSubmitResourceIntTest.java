package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.FormSubmit;
import com.schinta.repository.FormSubmitRepository;
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
 * Test class for the FormSubmitResource REST controller.
 *
 * @see FormSubmitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class FormSubmitResourceIntTest {

    private static final String DEFAULT_SUBMIT_ID = "AAAAAAAAAA";
    private static final String UPDATED_SUBMIT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SUBMIT_SOURCE = "A";
    private static final String UPDATED_SUBMIT_SOURCE = "B";

    private static final Integer DEFAULT_FORM_ID = 1;
    private static final Integer UPDATED_FORM_ID = 2;

    private static final String DEFAULT_FORM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FORM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBMIT_JOSN = "AAAAAAAAAA";
    private static final String UPDATED_SUBMIT_JOSN = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTER_CHANNEL = "A";
    private static final String UPDATED_REGISTER_CHANNEL = "B";

    private static final Integer DEFAULT_SUBMIT_DATE = 1;
    private static final Integer UPDATED_SUBMIT_DATE = 2;

    private static final Integer DEFAULT_SUBMIT_TIME = 1;
    private static final Integer UPDATED_SUBMIT_TIME = 2;

    private static final String DEFAULT_DEALFLAG = "A";
    private static final String UPDATED_DEALFLAG = "B";

    @Autowired
    private FormSubmitRepository formSubmitRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormSubmitMockMvc;

    private FormSubmit formSubmit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormSubmitResource formSubmitResource = new FormSubmitResource(formSubmitRepository);
        this.restFormSubmitMockMvc = MockMvcBuilders.standaloneSetup(formSubmitResource)
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
    public static FormSubmit createEntity(EntityManager em) {
        FormSubmit formSubmit = new FormSubmit()
            .submitID(DEFAULT_SUBMIT_ID)
            .submitSource(DEFAULT_SUBMIT_SOURCE)
            .formID(DEFAULT_FORM_ID)
            .formName(DEFAULT_FORM_NAME)
            .submitJosn(DEFAULT_SUBMIT_JOSN)
            .userID(DEFAULT_USER_ID)
            .registerChannel(DEFAULT_REGISTER_CHANNEL)
            .submitDate(DEFAULT_SUBMIT_DATE)
            .submitTime(DEFAULT_SUBMIT_TIME)
            .dealflag(DEFAULT_DEALFLAG);
        return formSubmit;
    }

    @Before
    public void initTest() {
        formSubmit = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormSubmit() throws Exception {
        int databaseSizeBeforeCreate = formSubmitRepository.findAll().size();

        // Create the FormSubmit
        restFormSubmitMockMvc.perform(post("/api/form-submits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formSubmit)))
            .andExpect(status().isCreated());

        // Validate the FormSubmit in the database
        List<FormSubmit> formSubmitList = formSubmitRepository.findAll();
        assertThat(formSubmitList).hasSize(databaseSizeBeforeCreate + 1);
        FormSubmit testFormSubmit = formSubmitList.get(formSubmitList.size() - 1);
        assertThat(testFormSubmit.getSubmitID()).isEqualTo(DEFAULT_SUBMIT_ID);
        assertThat(testFormSubmit.getSubmitSource()).isEqualTo(DEFAULT_SUBMIT_SOURCE);
        assertThat(testFormSubmit.getFormID()).isEqualTo(DEFAULT_FORM_ID);
        assertThat(testFormSubmit.getFormName()).isEqualTo(DEFAULT_FORM_NAME);
        assertThat(testFormSubmit.getSubmitJosn()).isEqualTo(DEFAULT_SUBMIT_JOSN);
        assertThat(testFormSubmit.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testFormSubmit.getRegisterChannel()).isEqualTo(DEFAULT_REGISTER_CHANNEL);
        assertThat(testFormSubmit.getSubmitDate()).isEqualTo(DEFAULT_SUBMIT_DATE);
        assertThat(testFormSubmit.getSubmitTime()).isEqualTo(DEFAULT_SUBMIT_TIME);
        assertThat(testFormSubmit.getDealflag()).isEqualTo(DEFAULT_DEALFLAG);
    }

    @Test
    @Transactional
    public void createFormSubmitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formSubmitRepository.findAll().size();

        // Create the FormSubmit with an existing ID
        formSubmit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormSubmitMockMvc.perform(post("/api/form-submits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formSubmit)))
            .andExpect(status().isBadRequest());

        // Validate the FormSubmit in the database
        List<FormSubmit> formSubmitList = formSubmitRepository.findAll();
        assertThat(formSubmitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSubmitIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = formSubmitRepository.findAll().size();
        // set the field null
        formSubmit.setSubmitID(null);

        // Create the FormSubmit, which fails.

        restFormSubmitMockMvc.perform(post("/api/form-submits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formSubmit)))
            .andExpect(status().isBadRequest());

        List<FormSubmit> formSubmitList = formSubmitRepository.findAll();
        assertThat(formSubmitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormSubmits() throws Exception {
        // Initialize the database
        formSubmitRepository.saveAndFlush(formSubmit);

        // Get all the formSubmitList
        restFormSubmitMockMvc.perform(get("/api/form-submits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formSubmit.getId().intValue())))
            .andExpect(jsonPath("$.[*].submitID").value(hasItem(DEFAULT_SUBMIT_ID.toString())))
            .andExpect(jsonPath("$.[*].submitSource").value(hasItem(DEFAULT_SUBMIT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].formID").value(hasItem(DEFAULT_FORM_ID)))
            .andExpect(jsonPath("$.[*].formName").value(hasItem(DEFAULT_FORM_NAME.toString())))
            .andExpect(jsonPath("$.[*].submitJosn").value(hasItem(DEFAULT_SUBMIT_JOSN.toString())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].registerChannel").value(hasItem(DEFAULT_REGISTER_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].submitDate").value(hasItem(DEFAULT_SUBMIT_DATE)))
            .andExpect(jsonPath("$.[*].submitTime").value(hasItem(DEFAULT_SUBMIT_TIME)))
            .andExpect(jsonPath("$.[*].dealflag").value(hasItem(DEFAULT_DEALFLAG.toString())));
    }
    
    @Test
    @Transactional
    public void getFormSubmit() throws Exception {
        // Initialize the database
        formSubmitRepository.saveAndFlush(formSubmit);

        // Get the formSubmit
        restFormSubmitMockMvc.perform(get("/api/form-submits/{id}", formSubmit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formSubmit.getId().intValue()))
            .andExpect(jsonPath("$.submitID").value(DEFAULT_SUBMIT_ID.toString()))
            .andExpect(jsonPath("$.submitSource").value(DEFAULT_SUBMIT_SOURCE.toString()))
            .andExpect(jsonPath("$.formID").value(DEFAULT_FORM_ID))
            .andExpect(jsonPath("$.formName").value(DEFAULT_FORM_NAME.toString()))
            .andExpect(jsonPath("$.submitJosn").value(DEFAULT_SUBMIT_JOSN.toString()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.registerChannel").value(DEFAULT_REGISTER_CHANNEL.toString()))
            .andExpect(jsonPath("$.submitDate").value(DEFAULT_SUBMIT_DATE))
            .andExpect(jsonPath("$.submitTime").value(DEFAULT_SUBMIT_TIME))
            .andExpect(jsonPath("$.dealflag").value(DEFAULT_DEALFLAG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormSubmit() throws Exception {
        // Get the formSubmit
        restFormSubmitMockMvc.perform(get("/api/form-submits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormSubmit() throws Exception {
        // Initialize the database
        formSubmitRepository.saveAndFlush(formSubmit);

        int databaseSizeBeforeUpdate = formSubmitRepository.findAll().size();

        // Update the formSubmit
        FormSubmit updatedFormSubmit = formSubmitRepository.findById(formSubmit.getId()).get();
        // Disconnect from session so that the updates on updatedFormSubmit are not directly saved in db
        em.detach(updatedFormSubmit);
        updatedFormSubmit
            .submitID(UPDATED_SUBMIT_ID)
            .submitSource(UPDATED_SUBMIT_SOURCE)
            .formID(UPDATED_FORM_ID)
            .formName(UPDATED_FORM_NAME)
            .submitJosn(UPDATED_SUBMIT_JOSN)
            .userID(UPDATED_USER_ID)
            .registerChannel(UPDATED_REGISTER_CHANNEL)
            .submitDate(UPDATED_SUBMIT_DATE)
            .submitTime(UPDATED_SUBMIT_TIME)
            .dealflag(UPDATED_DEALFLAG);

        restFormSubmitMockMvc.perform(put("/api/form-submits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormSubmit)))
            .andExpect(status().isOk());

        // Validate the FormSubmit in the database
        List<FormSubmit> formSubmitList = formSubmitRepository.findAll();
        assertThat(formSubmitList).hasSize(databaseSizeBeforeUpdate);
        FormSubmit testFormSubmit = formSubmitList.get(formSubmitList.size() - 1);
        assertThat(testFormSubmit.getSubmitID()).isEqualTo(UPDATED_SUBMIT_ID);
        assertThat(testFormSubmit.getSubmitSource()).isEqualTo(UPDATED_SUBMIT_SOURCE);
        assertThat(testFormSubmit.getFormID()).isEqualTo(UPDATED_FORM_ID);
        assertThat(testFormSubmit.getFormName()).isEqualTo(UPDATED_FORM_NAME);
        assertThat(testFormSubmit.getSubmitJosn()).isEqualTo(UPDATED_SUBMIT_JOSN);
        assertThat(testFormSubmit.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testFormSubmit.getRegisterChannel()).isEqualTo(UPDATED_REGISTER_CHANNEL);
        assertThat(testFormSubmit.getSubmitDate()).isEqualTo(UPDATED_SUBMIT_DATE);
        assertThat(testFormSubmit.getSubmitTime()).isEqualTo(UPDATED_SUBMIT_TIME);
        assertThat(testFormSubmit.getDealflag()).isEqualTo(UPDATED_DEALFLAG);
    }

    @Test
    @Transactional
    public void updateNonExistingFormSubmit() throws Exception {
        int databaseSizeBeforeUpdate = formSubmitRepository.findAll().size();

        // Create the FormSubmit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormSubmitMockMvc.perform(put("/api/form-submits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formSubmit)))
            .andExpect(status().isBadRequest());

        // Validate the FormSubmit in the database
        List<FormSubmit> formSubmitList = formSubmitRepository.findAll();
        assertThat(formSubmitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFormSubmit() throws Exception {
        // Initialize the database
        formSubmitRepository.saveAndFlush(formSubmit);

        int databaseSizeBeforeDelete = formSubmitRepository.findAll().size();

        // Get the formSubmit
        restFormSubmitMockMvc.perform(delete("/api/form-submits/{id}", formSubmit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FormSubmit> formSubmitList = formSubmitRepository.findAll();
        assertThat(formSubmitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormSubmit.class);
        FormSubmit formSubmit1 = new FormSubmit();
        formSubmit1.setId(1L);
        FormSubmit formSubmit2 = new FormSubmit();
        formSubmit2.setId(formSubmit1.getId());
        assertThat(formSubmit1).isEqualTo(formSubmit2);
        formSubmit2.setId(2L);
        assertThat(formSubmit1).isNotEqualTo(formSubmit2);
        formSubmit1.setId(null);
        assertThat(formSubmit1).isNotEqualTo(formSubmit2);
    }
}
