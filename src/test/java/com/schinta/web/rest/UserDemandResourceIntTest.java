package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.UserDemand;
import com.schinta.repository.UserDemandRepository;
import com.schinta.service.UserDemandService;
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
 * Test class for the UserDemandResource REST controller.
 *
 * @see UserDemandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class UserDemandResourceIntTest {

    private static final String DEFAULT_PROPERTY_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private UserDemandRepository userDemandRepository;
    
    @Autowired
    private UserDemandService userDemandService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserDemandMockMvc;

    private UserDemand userDemand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserDemandResource userDemandResource = new UserDemandResource(userDemandService);
        this.restUserDemandMockMvc = MockMvcBuilders.standaloneSetup(userDemandResource)
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
    public static UserDemand createEntity(EntityManager em) {
        UserDemand userDemand = new UserDemand()
            .propertyValue(DEFAULT_PROPERTY_VALUE)
            .remark(DEFAULT_REMARK);
        return userDemand;
    }

    @Before
    public void initTest() {
        userDemand = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserDemand() throws Exception {
        int databaseSizeBeforeCreate = userDemandRepository.findAll().size();

        // Create the UserDemand
        restUserDemandMockMvc.perform(post("/api/user-demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDemand)))
            .andExpect(status().isCreated());

        // Validate the UserDemand in the database
        List<UserDemand> userDemandList = userDemandRepository.findAll();
        assertThat(userDemandList).hasSize(databaseSizeBeforeCreate + 1);
        UserDemand testUserDemand = userDemandList.get(userDemandList.size() - 1);
        assertThat(testUserDemand.getPropertyValue()).isEqualTo(DEFAULT_PROPERTY_VALUE);
        assertThat(testUserDemand.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createUserDemandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDemandRepository.findAll().size();

        // Create the UserDemand with an existing ID
        userDemand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDemandMockMvc.perform(post("/api/user-demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDemand)))
            .andExpect(status().isBadRequest());

        // Validate the UserDemand in the database
        List<UserDemand> userDemandList = userDemandRepository.findAll();
        assertThat(userDemandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserDemands() throws Exception {
        // Initialize the database
        userDemandRepository.saveAndFlush(userDemand);

        // Get all the userDemandList
        restUserDemandMockMvc.perform(get("/api/user-demands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDemand.getId().intValue())))
            .andExpect(jsonPath("$.[*].propertyValue").value(hasItem(DEFAULT_PROPERTY_VALUE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }
    
    @Test
    @Transactional
    public void getUserDemand() throws Exception {
        // Initialize the database
        userDemandRepository.saveAndFlush(userDemand);

        // Get the userDemand
        restUserDemandMockMvc.perform(get("/api/user-demands/{id}", userDemand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userDemand.getId().intValue()))
            .andExpect(jsonPath("$.propertyValue").value(DEFAULT_PROPERTY_VALUE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserDemand() throws Exception {
        // Get the userDemand
        restUserDemandMockMvc.perform(get("/api/user-demands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserDemand() throws Exception {
        // Initialize the database
        userDemandService.save(userDemand);

        int databaseSizeBeforeUpdate = userDemandRepository.findAll().size();

        // Update the userDemand
        UserDemand updatedUserDemand = userDemandRepository.findById(userDemand.getId()).get();
        // Disconnect from session so that the updates on updatedUserDemand are not directly saved in db
        em.detach(updatedUserDemand);
        updatedUserDemand
            .propertyValue(UPDATED_PROPERTY_VALUE)
            .remark(UPDATED_REMARK);

        restUserDemandMockMvc.perform(put("/api/user-demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserDemand)))
            .andExpect(status().isOk());

        // Validate the UserDemand in the database
        List<UserDemand> userDemandList = userDemandRepository.findAll();
        assertThat(userDemandList).hasSize(databaseSizeBeforeUpdate);
        UserDemand testUserDemand = userDemandList.get(userDemandList.size() - 1);
        assertThat(testUserDemand.getPropertyValue()).isEqualTo(UPDATED_PROPERTY_VALUE);
        assertThat(testUserDemand.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingUserDemand() throws Exception {
        int databaseSizeBeforeUpdate = userDemandRepository.findAll().size();

        // Create the UserDemand

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDemandMockMvc.perform(put("/api/user-demands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDemand)))
            .andExpect(status().isBadRequest());

        // Validate the UserDemand in the database
        List<UserDemand> userDemandList = userDemandRepository.findAll();
        assertThat(userDemandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserDemand() throws Exception {
        // Initialize the database
        userDemandService.save(userDemand);

        int databaseSizeBeforeDelete = userDemandRepository.findAll().size();

        // Get the userDemand
        restUserDemandMockMvc.perform(delete("/api/user-demands/{id}", userDemand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserDemand> userDemandList = userDemandRepository.findAll();
        assertThat(userDemandList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDemand.class);
        UserDemand userDemand1 = new UserDemand();
        userDemand1.setId(1L);
        UserDemand userDemand2 = new UserDemand();
        userDemand2.setId(userDemand1.getId());
        assertThat(userDemand1).isEqualTo(userDemand2);
        userDemand2.setId(2L);
        assertThat(userDemand1).isNotEqualTo(userDemand2);
        userDemand1.setId(null);
        assertThat(userDemand1).isNotEqualTo(userDemand2);
    }
}
