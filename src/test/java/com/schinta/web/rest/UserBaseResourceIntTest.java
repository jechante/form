package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.UserBase;
import com.schinta.repository.UserBaseRepository;
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
 * Test class for the UserBaseResource REST controller.
 *
 * @see UserBaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class UserBaseResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_STATUS = "A";
    private static final String UPDATED_USER_STATUS = "B";

    private static final String DEFAULT_SEX = "A";
    private static final String UPDATED_SEX = "B";

    private static final String DEFAULT_REGISTER_CHANNEL = "A";
    private static final String UPDATED_REGISTER_CHANNEL = "B";

    private static final Integer DEFAULT_REGISTER_BROKER = 1;
    private static final Integer UPDATED_REGISTER_BROKER = 2;

    private static final Integer DEFAULT_REGISTER_DATE = 1;
    private static final Integer UPDATED_REGISTER_DATE = 2;

    private static final Integer DEFAULT_REGISTER_TIME = 1;
    private static final Integer UPDATED_REGISTER_TIME = 2;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserBaseMockMvc;

    private UserBase userBase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserBaseResource userBaseResource = new UserBaseResource(userBaseRepository);
        this.restUserBaseMockMvc = MockMvcBuilders.standaloneSetup(userBaseResource)
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
    public static UserBase createEntity(EntityManager em) {
        UserBase userBase = new UserBase()
            .userID(DEFAULT_USER_ID)
            .userName(DEFAULT_USER_NAME)
            .userStatus(DEFAULT_USER_STATUS)
            .sex(DEFAULT_SEX)
            .registerChannel(DEFAULT_REGISTER_CHANNEL)
            .registerBroker(DEFAULT_REGISTER_BROKER)
            .registerDate(DEFAULT_REGISTER_DATE)
            .registerTime(DEFAULT_REGISTER_TIME);
        return userBase;
    }

    @Before
    public void initTest() {
        userBase = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserBase() throws Exception {
        int databaseSizeBeforeCreate = userBaseRepository.findAll().size();

        // Create the UserBase
        restUserBaseMockMvc.perform(post("/api/user-bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userBase)))
            .andExpect(status().isCreated());

        // Validate the UserBase in the database
        List<UserBase> userBaseList = userBaseRepository.findAll();
        assertThat(userBaseList).hasSize(databaseSizeBeforeCreate + 1);
        UserBase testUserBase = userBaseList.get(userBaseList.size() - 1);
        assertThat(testUserBase.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserBase.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUserBase.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
        assertThat(testUserBase.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testUserBase.getRegisterChannel()).isEqualTo(DEFAULT_REGISTER_CHANNEL);
        assertThat(testUserBase.getRegisterBroker()).isEqualTo(DEFAULT_REGISTER_BROKER);
        assertThat(testUserBase.getRegisterDate()).isEqualTo(DEFAULT_REGISTER_DATE);
        assertThat(testUserBase.getRegisterTime()).isEqualTo(DEFAULT_REGISTER_TIME);
    }

    @Test
    @Transactional
    public void createUserBaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userBaseRepository.findAll().size();

        // Create the UserBase with an existing ID
        userBase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserBaseMockMvc.perform(post("/api/user-bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userBase)))
            .andExpect(status().isBadRequest());

        // Validate the UserBase in the database
        List<UserBase> userBaseList = userBaseRepository.findAll();
        assertThat(userBaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = userBaseRepository.findAll().size();
        // set the field null
        userBase.setUserID(null);

        // Create the UserBase, which fails.

        restUserBaseMockMvc.perform(post("/api/user-bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userBase)))
            .andExpect(status().isBadRequest());

        List<UserBase> userBaseList = userBaseRepository.findAll();
        assertThat(userBaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserBases() throws Exception {
        // Initialize the database
        userBaseRepository.saveAndFlush(userBase);

        // Get all the userBaseList
        restUserBaseMockMvc.perform(get("/api/user-bases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userBase.getId().intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].registerChannel").value(hasItem(DEFAULT_REGISTER_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].registerBroker").value(hasItem(DEFAULT_REGISTER_BROKER)))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(DEFAULT_REGISTER_DATE)))
            .andExpect(jsonPath("$.[*].registerTime").value(hasItem(DEFAULT_REGISTER_TIME)));
    }
    
    @Test
    @Transactional
    public void getUserBase() throws Exception {
        // Initialize the database
        userBaseRepository.saveAndFlush(userBase);

        // Get the userBase
        restUserBaseMockMvc.perform(get("/api/user-bases/{id}", userBase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userBase.getId().intValue()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.userStatus").value(DEFAULT_USER_STATUS.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.registerChannel").value(DEFAULT_REGISTER_CHANNEL.toString()))
            .andExpect(jsonPath("$.registerBroker").value(DEFAULT_REGISTER_BROKER))
            .andExpect(jsonPath("$.registerDate").value(DEFAULT_REGISTER_DATE))
            .andExpect(jsonPath("$.registerTime").value(DEFAULT_REGISTER_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingUserBase() throws Exception {
        // Get the userBase
        restUserBaseMockMvc.perform(get("/api/user-bases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserBase() throws Exception {
        // Initialize the database
        userBaseRepository.saveAndFlush(userBase);

        int databaseSizeBeforeUpdate = userBaseRepository.findAll().size();

        // Update the userBase
        UserBase updatedUserBase = userBaseRepository.findById(userBase.getId()).get();
        // Disconnect from session so that the updates on updatedUserBase are not directly saved in db
        em.detach(updatedUserBase);
        updatedUserBase
            .userID(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .userStatus(UPDATED_USER_STATUS)
            .sex(UPDATED_SEX)
            .registerChannel(UPDATED_REGISTER_CHANNEL)
            .registerBroker(UPDATED_REGISTER_BROKER)
            .registerDate(UPDATED_REGISTER_DATE)
            .registerTime(UPDATED_REGISTER_TIME);

        restUserBaseMockMvc.perform(put("/api/user-bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserBase)))
            .andExpect(status().isOk());

        // Validate the UserBase in the database
        List<UserBase> userBaseList = userBaseRepository.findAll();
        assertThat(userBaseList).hasSize(databaseSizeBeforeUpdate);
        UserBase testUserBase = userBaseList.get(userBaseList.size() - 1);
        assertThat(testUserBase.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserBase.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUserBase.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
        assertThat(testUserBase.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testUserBase.getRegisterChannel()).isEqualTo(UPDATED_REGISTER_CHANNEL);
        assertThat(testUserBase.getRegisterBroker()).isEqualTo(UPDATED_REGISTER_BROKER);
        assertThat(testUserBase.getRegisterDate()).isEqualTo(UPDATED_REGISTER_DATE);
        assertThat(testUserBase.getRegisterTime()).isEqualTo(UPDATED_REGISTER_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingUserBase() throws Exception {
        int databaseSizeBeforeUpdate = userBaseRepository.findAll().size();

        // Create the UserBase

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserBaseMockMvc.perform(put("/api/user-bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userBase)))
            .andExpect(status().isBadRequest());

        // Validate the UserBase in the database
        List<UserBase> userBaseList = userBaseRepository.findAll();
        assertThat(userBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserBase() throws Exception {
        // Initialize the database
        userBaseRepository.saveAndFlush(userBase);

        int databaseSizeBeforeDelete = userBaseRepository.findAll().size();

        // Get the userBase
        restUserBaseMockMvc.perform(delete("/api/user-bases/{id}", userBase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserBase> userBaseList = userBaseRepository.findAll();
        assertThat(userBaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserBase.class);
        UserBase userBase1 = new UserBase();
        userBase1.setId(1L);
        UserBase userBase2 = new UserBase();
        userBase2.setId(userBase1.getId());
        assertThat(userBase1).isEqualTo(userBase2);
        userBase2.setId(2L);
        assertThat(userBase1).isNotEqualTo(userBase2);
        userBase1.setId(null);
        assertThat(userBase1).isNotEqualTo(userBase2);
    }
}
