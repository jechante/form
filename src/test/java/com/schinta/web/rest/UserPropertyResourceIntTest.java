package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.UserProperty;
import com.schinta.repository.UserPropertyRepository;
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
 * Test class for the UserPropertyResource REST controller.
 *
 * @see UserPropertyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class UserPropertyResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private UserPropertyRepository userPropertyRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserPropertyMockMvc;

    private UserProperty userProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserPropertyResource userPropertyResource = new UserPropertyResource(userPropertyRepository);
        this.restUserPropertyMockMvc = MockMvcBuilders.standaloneSetup(userPropertyResource)
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
    public static UserProperty createEntity(EntityManager em) {
        UserProperty userProperty = new UserProperty()
            .userID(DEFAULT_USER_ID)
            .propertyID(DEFAULT_PROPERTY_ID)
            .propertyValue(DEFAULT_PROPERTY_VALUE)
            .remark(DEFAULT_REMARK);
        return userProperty;
    }

    @Before
    public void initTest() {
        userProperty = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserProperty() throws Exception {
        int databaseSizeBeforeCreate = userPropertyRepository.findAll().size();

        // Create the UserProperty
        restUserPropertyMockMvc.perform(post("/api/user-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProperty)))
            .andExpect(status().isCreated());

        // Validate the UserProperty in the database
        List<UserProperty> userPropertyList = userPropertyRepository.findAll();
        assertThat(userPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        UserProperty testUserProperty = userPropertyList.get(userPropertyList.size() - 1);
        assertThat(testUserProperty.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserProperty.getPropertyID()).isEqualTo(DEFAULT_PROPERTY_ID);
        assertThat(testUserProperty.getPropertyValue()).isEqualTo(DEFAULT_PROPERTY_VALUE);
        assertThat(testUserProperty.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createUserPropertyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPropertyRepository.findAll().size();

        // Create the UserProperty with an existing ID
        userProperty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPropertyMockMvc.perform(post("/api/user-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProperty)))
            .andExpect(status().isBadRequest());

        // Validate the UserProperty in the database
        List<UserProperty> userPropertyList = userPropertyRepository.findAll();
        assertThat(userPropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPropertyRepository.findAll().size();
        // set the field null
        userProperty.setUserID(null);

        // Create the UserProperty, which fails.

        restUserPropertyMockMvc.perform(post("/api/user-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProperty)))
            .andExpect(status().isBadRequest());

        List<UserProperty> userPropertyList = userPropertyRepository.findAll();
        assertThat(userPropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserProperties() throws Exception {
        // Initialize the database
        userPropertyRepository.saveAndFlush(userProperty);

        // Get all the userPropertyList
        restUserPropertyMockMvc.perform(get("/api/user-properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].propertyID").value(hasItem(DEFAULT_PROPERTY_ID.toString())))
            .andExpect(jsonPath("$.[*].propertyValue").value(hasItem(DEFAULT_PROPERTY_VALUE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }
    
    @Test
    @Transactional
    public void getUserProperty() throws Exception {
        // Initialize the database
        userPropertyRepository.saveAndFlush(userProperty);

        // Get the userProperty
        restUserPropertyMockMvc.perform(get("/api/user-properties/{id}", userProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userProperty.getId().intValue()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.propertyID").value(DEFAULT_PROPERTY_ID.toString()))
            .andExpect(jsonPath("$.propertyValue").value(DEFAULT_PROPERTY_VALUE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserProperty() throws Exception {
        // Get the userProperty
        restUserPropertyMockMvc.perform(get("/api/user-properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProperty() throws Exception {
        // Initialize the database
        userPropertyRepository.saveAndFlush(userProperty);

        int databaseSizeBeforeUpdate = userPropertyRepository.findAll().size();

        // Update the userProperty
        UserProperty updatedUserProperty = userPropertyRepository.findById(userProperty.getId()).get();
        // Disconnect from session so that the updates on updatedUserProperty are not directly saved in db
        em.detach(updatedUserProperty);
        updatedUserProperty
            .userID(UPDATED_USER_ID)
            .propertyID(UPDATED_PROPERTY_ID)
            .propertyValue(UPDATED_PROPERTY_VALUE)
            .remark(UPDATED_REMARK);

        restUserPropertyMockMvc.perform(put("/api/user-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserProperty)))
            .andExpect(status().isOk());

        // Validate the UserProperty in the database
        List<UserProperty> userPropertyList = userPropertyRepository.findAll();
        assertThat(userPropertyList).hasSize(databaseSizeBeforeUpdate);
        UserProperty testUserProperty = userPropertyList.get(userPropertyList.size() - 1);
        assertThat(testUserProperty.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserProperty.getPropertyID()).isEqualTo(UPDATED_PROPERTY_ID);
        assertThat(testUserProperty.getPropertyValue()).isEqualTo(UPDATED_PROPERTY_VALUE);
        assertThat(testUserProperty.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingUserProperty() throws Exception {
        int databaseSizeBeforeUpdate = userPropertyRepository.findAll().size();

        // Create the UserProperty

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPropertyMockMvc.perform(put("/api/user-properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProperty)))
            .andExpect(status().isBadRequest());

        // Validate the UserProperty in the database
        List<UserProperty> userPropertyList = userPropertyRepository.findAll();
        assertThat(userPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserProperty() throws Exception {
        // Initialize the database
        userPropertyRepository.saveAndFlush(userProperty);

        int databaseSizeBeforeDelete = userPropertyRepository.findAll().size();

        // Get the userProperty
        restUserPropertyMockMvc.perform(delete("/api/user-properties/{id}", userProperty.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserProperty> userPropertyList = userPropertyRepository.findAll();
        assertThat(userPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProperty.class);
        UserProperty userProperty1 = new UserProperty();
        userProperty1.setId(1L);
        UserProperty userProperty2 = new UserProperty();
        userProperty2.setId(userProperty1.getId());
        assertThat(userProperty1).isEqualTo(userProperty2);
        userProperty2.setId(2L);
        assertThat(userProperty1).isNotEqualTo(userProperty2);
        userProperty1.setId(null);
        assertThat(userProperty1).isNotEqualTo(userProperty2);
    }
}
