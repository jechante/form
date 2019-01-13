package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.FormSubmit;
import com.schinta.repository.BaseFormRepository;
import com.schinta.repository.FormSubmitRepository;
import com.schinta.repository.WxUserRepository;
import com.schinta.service.FormSubmitService;
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
import java.time.*;
import java.util.List;


import static com.schinta.web.rest.TestUtil.sameInstant;
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

    private static final String DEFAULT_SUBMIT_JOSN = "AAAAAAAAAA";
    private static final String UPDATED_SUBMIT_JOSN = "BBBBBBBBBB";

    private static final Integer DEFAULT_SERIAL_NUMBER = 1;
    private static final Integer UPDATED_SERIAL_NUMBER = 2;

    private static final String DEFAULT_CREATOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CREATOR_NAME = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_CREATED_DATE_TIME = LocalDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.systemDefault());
    private static final LocalDateTime UPDATED_CREATED_DATE_TIME = LocalDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final LocalDateTime DEFAULT_UPDATED_DATE_TIME = LocalDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.systemDefault());
    private static final LocalDateTime UPDATED_UPDATED_DATE_TIME = LocalDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INFO_REMOTE_IP = "AAAAAAAAAA";
    private static final String UPDATED_INFO_REMOTE_IP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEALFLAG = false;
    private static final Boolean UPDATED_DEALFLAG = true;

    @Autowired
    private FormSubmitRepository formSubmitRepository;

    @Autowired
    private WxUserRepository wxUserRepository;

    @Autowired
    private BaseFormRepository baseFormRepository;
    
    @Autowired
    private FormSubmitService formSubmitService;

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
        final FormSubmitResource formSubmitResource = new FormSubmitResource(formSubmitService,baseFormRepository,wxUserRepository);
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
            .submitJosn(DEFAULT_SUBMIT_JOSN)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .creatorName(DEFAULT_CREATOR_NAME)
            .createdDateTime(DEFAULT_CREATED_DATE_TIME)
            .updatedDateTime(DEFAULT_UPDATED_DATE_TIME)
            .infoRemoteIp(DEFAULT_INFO_REMOTE_IP)
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
        assertThat(testFormSubmit.getSubmitJosn()).isEqualTo(DEFAULT_SUBMIT_JOSN);
        assertThat(testFormSubmit.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testFormSubmit.getCreatorName()).isEqualTo(DEFAULT_CREATOR_NAME);
        assertThat(testFormSubmit.getCreatedDateTime()).isEqualTo(DEFAULT_CREATED_DATE_TIME);
        assertThat(testFormSubmit.getUpdatedDateTime()).isEqualTo(DEFAULT_UPDATED_DATE_TIME);
        assertThat(testFormSubmit.getInfoRemoteIp()).isEqualTo(DEFAULT_INFO_REMOTE_IP);
        assertThat(testFormSubmit.isDealflag()).isEqualTo(DEFAULT_DEALFLAG);
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
    public void checkSubmitJosnIsRequired() throws Exception {
        int databaseSizeBeforeTest = formSubmitRepository.findAll().size();
        // set the field null
        formSubmit.setSubmitJosn(null);

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
    public void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = formSubmitRepository.findAll().size();
        // set the field null
        formSubmit.setSerialNumber(null);

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
            .andExpect(jsonPath("$.[*].submitJosn").value(hasItem(DEFAULT_SUBMIT_JOSN.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].creatorName").value(hasItem(DEFAULT_CREATOR_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdDateTime").value(hasItem(DEFAULT_CREATED_DATE_TIME)))
            .andExpect(jsonPath("$.[*].updatedDateTime").value(hasItem(DEFAULT_UPDATED_DATE_TIME)))
            .andExpect(jsonPath("$.[*].infoRemoteIp").value(hasItem(DEFAULT_INFO_REMOTE_IP.toString())))
            .andExpect(jsonPath("$.[*].dealflag").value(hasItem(DEFAULT_DEALFLAG.booleanValue())));
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
            .andExpect(jsonPath("$.submitJosn").value(DEFAULT_SUBMIT_JOSN.toString()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.creatorName").value(DEFAULT_CREATOR_NAME.toString()))
            .andExpect(jsonPath("$.createdDateTime").value(DEFAULT_CREATED_DATE_TIME))
            .andExpect(jsonPath("$.updatedDateTime").value(DEFAULT_UPDATED_DATE_TIME))
            .andExpect(jsonPath("$.infoRemoteIp").value(DEFAULT_INFO_REMOTE_IP.toString()))
            .andExpect(jsonPath("$.dealflag").value(DEFAULT_DEALFLAG.booleanValue()));
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
        formSubmitService.save(formSubmit);

        int databaseSizeBeforeUpdate = formSubmitRepository.findAll().size();

        // Update the formSubmit
        FormSubmit updatedFormSubmit = formSubmitRepository.findById(formSubmit.getId()).get();
        // Disconnect from session so that the updates on updatedFormSubmit are not directly saved in db
        em.detach(updatedFormSubmit);
        updatedFormSubmit
            .submitJosn(UPDATED_SUBMIT_JOSN)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .creatorName(UPDATED_CREATOR_NAME)
            .createdDateTime(UPDATED_CREATED_DATE_TIME)
            .updatedDateTime(UPDATED_UPDATED_DATE_TIME)
            .infoRemoteIp(UPDATED_INFO_REMOTE_IP)
            .dealflag(UPDATED_DEALFLAG);

        restFormSubmitMockMvc.perform(put("/api/form-submits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormSubmit)))
            .andExpect(status().isOk());

        // Validate the FormSubmit in the database
        List<FormSubmit> formSubmitList = formSubmitRepository.findAll();
        assertThat(formSubmitList).hasSize(databaseSizeBeforeUpdate);
        FormSubmit testFormSubmit = formSubmitList.get(formSubmitList.size() - 1);
        assertThat(testFormSubmit.getSubmitJosn()).isEqualTo(UPDATED_SUBMIT_JOSN);
        assertThat(testFormSubmit.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testFormSubmit.getCreatorName()).isEqualTo(UPDATED_CREATOR_NAME);
        assertThat(testFormSubmit.getCreatedDateTime()).isEqualTo(UPDATED_CREATED_DATE_TIME);
        assertThat(testFormSubmit.getUpdatedDateTime()).isEqualTo(UPDATED_UPDATED_DATE_TIME);
        assertThat(testFormSubmit.getInfoRemoteIp()).isEqualTo(UPDATED_INFO_REMOTE_IP);
        assertThat(testFormSubmit.isDealflag()).isEqualTo(UPDATED_DEALFLAG);
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
        formSubmitService.save(formSubmit);

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
