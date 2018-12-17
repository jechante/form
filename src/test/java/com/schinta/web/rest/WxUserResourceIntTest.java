package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.WxUser;
import com.schinta.repository.WxUserRepository;
import com.schinta.service.WxUserService;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.schinta.web.rest.TestUtil.sameInstant;
import static com.schinta.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.schinta.domain.enumeration.Gender;
import com.schinta.domain.enumeration.Gender;
import com.schinta.domain.enumeration.UserStatus;
import com.schinta.domain.enumeration.RegisterChannel;
/**
 * Test class for the WxUserResource REST controller.
 *
 * @see WxUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class WxUserResourceIntTest {

    private static final String DEFAULT_WX_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WX_NICK_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_WX_GENDER = Gender.MALE;
    private static final Gender UPDATED_WX_GENDER = Gender.FEMALE;

    private static final String DEFAULT_WX_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_WX_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_WX_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_WX_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_WX_CITY = "AAAAAAAAAA";
    private static final String UPDATED_WX_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_WX_HEADIMGURL = "AAAAAAAAAA";
    private static final String UPDATED_WX_HEADIMGURL = "BBBBBBBBBB";

    private static final String DEFAULT_WX_UNIONID = "AAAAAAAAAA";
    private static final String UPDATED_WX_UNIONID = "BBBBBBBBBB";

    private static final String DEFAULT_WX_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_WX_LANGUAGE = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;
    private static final UserStatus UPDATED_USER_STATUS = UserStatus.UNACTIVE;

    private static final RegisterChannel DEFAULT_REGISTER_CHANNEL = RegisterChannel.POSTER;
    private static final RegisterChannel UPDATED_REGISTER_CHANNEL = RegisterChannel.SITE;

    private static final ZonedDateTime DEFAULT_REGISTER_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGISTER_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_PUSH_LIMIT = 1;
    private static final Integer UPDATED_PUSH_LIMIT = 2;

    @Autowired
    private WxUserRepository wxUserRepository;
    
    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWxUserMockMvc;

    private WxUser wxUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WxUserResource wxUserResource = new WxUserResource(wxUserService);
        this.restWxUserMockMvc = MockMvcBuilders.standaloneSetup(wxUserResource)
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
    public static WxUser createEntity(EntityManager em) {
        WxUser wxUser = new WxUser()
            .wxNickName(DEFAULT_WX_NICK_NAME)
            .wxGender(DEFAULT_WX_GENDER)
            .wxCountry(DEFAULT_WX_COUNTRY)
            .wxProvince(DEFAULT_WX_PROVINCE)
            .wxCity(DEFAULT_WX_CITY)
            .wxHeadimgurl(DEFAULT_WX_HEADIMGURL)
            .wxUnionid(DEFAULT_WX_UNIONID)
            .wxLanguage(DEFAULT_WX_LANGUAGE)
            .gender(DEFAULT_GENDER)
            .userStatus(DEFAULT_USER_STATUS)
            .registerChannel(DEFAULT_REGISTER_CHANNEL)
            .registerDateTime(DEFAULT_REGISTER_DATE_TIME)
            .pushLimit(DEFAULT_PUSH_LIMIT);
        return wxUser;
    }

    @Before
    public void initTest() {
        wxUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createWxUser() throws Exception {
        int databaseSizeBeforeCreate = wxUserRepository.findAll().size();

        // Create the WxUser
        restWxUserMockMvc.perform(post("/api/wx-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxUser)))
            .andExpect(status().isCreated());

        // Validate the WxUser in the database
        List<WxUser> wxUserList = wxUserRepository.findAll();
        assertThat(wxUserList).hasSize(databaseSizeBeforeCreate + 1);
        WxUser testWxUser = wxUserList.get(wxUserList.size() - 1);
        assertThat(testWxUser.getWxNickName()).isEqualTo(DEFAULT_WX_NICK_NAME);
        assertThat(testWxUser.getWxGender()).isEqualTo(DEFAULT_WX_GENDER);
        assertThat(testWxUser.getWxCountry()).isEqualTo(DEFAULT_WX_COUNTRY);
        assertThat(testWxUser.getWxProvince()).isEqualTo(DEFAULT_WX_PROVINCE);
        assertThat(testWxUser.getWxCity()).isEqualTo(DEFAULT_WX_CITY);
        assertThat(testWxUser.getWxHeadimgurl()).isEqualTo(DEFAULT_WX_HEADIMGURL);
        assertThat(testWxUser.getWxUnionid()).isEqualTo(DEFAULT_WX_UNIONID);
        assertThat(testWxUser.getWxLanguage()).isEqualTo(DEFAULT_WX_LANGUAGE);
        assertThat(testWxUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testWxUser.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
        assertThat(testWxUser.getRegisterChannel()).isEqualTo(DEFAULT_REGISTER_CHANNEL);
        assertThat(testWxUser.getRegisterDateTime()).isEqualTo(DEFAULT_REGISTER_DATE_TIME);
        assertThat(testWxUser.getPushLimit()).isEqualTo(DEFAULT_PUSH_LIMIT);
    }

    @Test
    @Transactional
    public void createWxUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wxUserRepository.findAll().size();

        // Create the WxUser with an existing ID
        wxUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWxUserMockMvc.perform(post("/api/wx-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxUser)))
            .andExpect(status().isBadRequest());

        // Validate the WxUser in the database
        List<WxUser> wxUserList = wxUserRepository.findAll();
        assertThat(wxUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWxUsers() throws Exception {
        // Initialize the database
        wxUserRepository.saveAndFlush(wxUser);

        // Get all the wxUserList
        restWxUserMockMvc.perform(get("/api/wx-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wxUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].wxNickName").value(hasItem(DEFAULT_WX_NICK_NAME.toString())))
            .andExpect(jsonPath("$.[*].wxGender").value(hasItem(DEFAULT_WX_GENDER.toString())))
            .andExpect(jsonPath("$.[*].wxCountry").value(hasItem(DEFAULT_WX_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].wxProvince").value(hasItem(DEFAULT_WX_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].wxCity").value(hasItem(DEFAULT_WX_CITY.toString())))
            .andExpect(jsonPath("$.[*].wxHeadimgurl").value(hasItem(DEFAULT_WX_HEADIMGURL.toString())))
            .andExpect(jsonPath("$.[*].wxUnionid").value(hasItem(DEFAULT_WX_UNIONID.toString())))
            .andExpect(jsonPath("$.[*].wxLanguage").value(hasItem(DEFAULT_WX_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].registerChannel").value(hasItem(DEFAULT_REGISTER_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].registerDateTime").value(hasItem(sameInstant(DEFAULT_REGISTER_DATE_TIME))))
            .andExpect(jsonPath("$.[*].pushLimit").value(hasItem(DEFAULT_PUSH_LIMIT)));
    }
    
    @Test
    @Transactional
    public void getWxUser() throws Exception {
        // Initialize the database
        wxUserRepository.saveAndFlush(wxUser);

        // Get the wxUser
        restWxUserMockMvc.perform(get("/api/wx-users/{id}", wxUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wxUser.getId().intValue()))
            .andExpect(jsonPath("$.wxNickName").value(DEFAULT_WX_NICK_NAME.toString()))
            .andExpect(jsonPath("$.wxGender").value(DEFAULT_WX_GENDER.toString()))
            .andExpect(jsonPath("$.wxCountry").value(DEFAULT_WX_COUNTRY.toString()))
            .andExpect(jsonPath("$.wxProvince").value(DEFAULT_WX_PROVINCE.toString()))
            .andExpect(jsonPath("$.wxCity").value(DEFAULT_WX_CITY.toString()))
            .andExpect(jsonPath("$.wxHeadimgurl").value(DEFAULT_WX_HEADIMGURL.toString()))
            .andExpect(jsonPath("$.wxUnionid").value(DEFAULT_WX_UNIONID.toString()))
            .andExpect(jsonPath("$.wxLanguage").value(DEFAULT_WX_LANGUAGE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.userStatus").value(DEFAULT_USER_STATUS.toString()))
            .andExpect(jsonPath("$.registerChannel").value(DEFAULT_REGISTER_CHANNEL.toString()))
            .andExpect(jsonPath("$.registerDateTime").value(sameInstant(DEFAULT_REGISTER_DATE_TIME)))
            .andExpect(jsonPath("$.pushLimit").value(DEFAULT_PUSH_LIMIT));
    }

    @Test
    @Transactional
    public void getNonExistingWxUser() throws Exception {
        // Get the wxUser
        restWxUserMockMvc.perform(get("/api/wx-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWxUser() throws Exception {
        // Initialize the database
        wxUserService.save(wxUser);

        int databaseSizeBeforeUpdate = wxUserRepository.findAll().size();

        // Update the wxUser
        WxUser updatedWxUser = wxUserRepository.findById(wxUser.getId()).get();
        // Disconnect from session so that the updates on updatedWxUser are not directly saved in db
        em.detach(updatedWxUser);
        updatedWxUser
            .wxNickName(UPDATED_WX_NICK_NAME)
            .wxGender(UPDATED_WX_GENDER)
            .wxCountry(UPDATED_WX_COUNTRY)
            .wxProvince(UPDATED_WX_PROVINCE)
            .wxCity(UPDATED_WX_CITY)
            .wxHeadimgurl(UPDATED_WX_HEADIMGURL)
            .wxUnionid(UPDATED_WX_UNIONID)
            .wxLanguage(UPDATED_WX_LANGUAGE)
            .gender(UPDATED_GENDER)
            .userStatus(UPDATED_USER_STATUS)
            .registerChannel(UPDATED_REGISTER_CHANNEL)
            .registerDateTime(UPDATED_REGISTER_DATE_TIME)
            .pushLimit(UPDATED_PUSH_LIMIT);

        restWxUserMockMvc.perform(put("/api/wx-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWxUser)))
            .andExpect(status().isOk());

        // Validate the WxUser in the database
        List<WxUser> wxUserList = wxUserRepository.findAll();
        assertThat(wxUserList).hasSize(databaseSizeBeforeUpdate);
        WxUser testWxUser = wxUserList.get(wxUserList.size() - 1);
        assertThat(testWxUser.getWxNickName()).isEqualTo(UPDATED_WX_NICK_NAME);
        assertThat(testWxUser.getWxGender()).isEqualTo(UPDATED_WX_GENDER);
        assertThat(testWxUser.getWxCountry()).isEqualTo(UPDATED_WX_COUNTRY);
        assertThat(testWxUser.getWxProvince()).isEqualTo(UPDATED_WX_PROVINCE);
        assertThat(testWxUser.getWxCity()).isEqualTo(UPDATED_WX_CITY);
        assertThat(testWxUser.getWxHeadimgurl()).isEqualTo(UPDATED_WX_HEADIMGURL);
        assertThat(testWxUser.getWxUnionid()).isEqualTo(UPDATED_WX_UNIONID);
        assertThat(testWxUser.getWxLanguage()).isEqualTo(UPDATED_WX_LANGUAGE);
        assertThat(testWxUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testWxUser.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
        assertThat(testWxUser.getRegisterChannel()).isEqualTo(UPDATED_REGISTER_CHANNEL);
        assertThat(testWxUser.getRegisterDateTime()).isEqualTo(UPDATED_REGISTER_DATE_TIME);
        assertThat(testWxUser.getPushLimit()).isEqualTo(UPDATED_PUSH_LIMIT);
    }

    @Test
    @Transactional
    public void updateNonExistingWxUser() throws Exception {
        int databaseSizeBeforeUpdate = wxUserRepository.findAll().size();

        // Create the WxUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWxUserMockMvc.perform(put("/api/wx-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxUser)))
            .andExpect(status().isBadRequest());

        // Validate the WxUser in the database
        List<WxUser> wxUserList = wxUserRepository.findAll();
        assertThat(wxUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWxUser() throws Exception {
        // Initialize the database
        wxUserService.save(wxUser);

        int databaseSizeBeforeDelete = wxUserRepository.findAll().size();

        // Get the wxUser
        restWxUserMockMvc.perform(delete("/api/wx-users/{id}", wxUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WxUser> wxUserList = wxUserRepository.findAll();
        assertThat(wxUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WxUser.class);
        WxUser wxUser1 = new WxUser();
        wxUser1.setId(1L);
        WxUser wxUser2 = new WxUser();
        wxUser2.setId(wxUser1.getId());
        assertThat(wxUser1).isEqualTo(wxUser2);
        wxUser2.setId(2L);
        assertThat(wxUser1).isNotEqualTo(wxUser2);
        wxUser1.setId(null);
        assertThat(wxUser1).isNotEqualTo(wxUser2);
    }
}
