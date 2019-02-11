package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.UserMatch;
import com.schinta.repository.UserMatchRepository;
import com.schinta.service.UserMatchService;
import com.schinta.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.schinta.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.schinta.domain.enumeration.MatchType;
import com.schinta.domain.enumeration.PushStatus;
/**
 * Test class for the UserMatchResource REST controller.
 *
 * @see UserMatchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class UserMatchResourceIntTest {

    private static final Float DEFAULT_SCORE_ATO_B = 1F;
    private static final Float UPDATED_SCORE_ATO_B = 2F;

    private static final Float DEFAULT_SCORE_BTO_A = 1F;
    private static final Float UPDATED_SCORE_BTO_A = 2F;

    private static final Float DEFAULT_SCORE_TOTAL = 1F;
    private static final Float UPDATED_SCORE_TOTAL = 2F;

    private static final Float DEFAULT_RATIO = 1F;
    private static final Float UPDATED_RATIO = 2F;

    private static final Integer DEFAULT_RANK_A = 1;
    private static final Integer UPDATED_RANK_A = 2;

    private static final Integer DEFAULT_RANK_B = 1;
    private static final Integer UPDATED_RANK_B = 2;

    private static final MatchType DEFAULT_MATCH_TYPE = MatchType.ATOB;
    private static final MatchType UPDATED_MATCH_TYPE = MatchType.BTOA;

    private static final PushStatus DEFAULT_PUSH_STATUS = PushStatus.NEITHER;
    private static final PushStatus UPDATED_PUSH_STATUS = PushStatus.A;

    @Autowired
    private UserMatchRepository userMatchRepository;

    @Mock
    private UserMatchRepository userMatchRepositoryMock;
    

    @Mock
    private UserMatchService userMatchServiceMock;

    @Autowired
    private UserMatchService userMatchService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserMatchMockMvc;

    private UserMatch userMatch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserMatchResource userMatchResource = new UserMatchResource(userMatchService);
        this.restUserMatchMockMvc = MockMvcBuilders.standaloneSetup(userMatchResource)
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
    public static UserMatch createEntity(EntityManager em) {
        UserMatch userMatch = new UserMatch()
            .scoreAtoB(DEFAULT_SCORE_ATO_B)
            .scoreBtoA(DEFAULT_SCORE_BTO_A)
            .scoreTotal(DEFAULT_SCORE_TOTAL)
            .ratio(DEFAULT_RATIO)
            .rankA(DEFAULT_RANK_A)
            .rankB(DEFAULT_RANK_B)
            .matchType(DEFAULT_MATCH_TYPE)
            .pushStatus(DEFAULT_PUSH_STATUS);
        return userMatch;
    }

    @Before
    public void initTest() {
        userMatch = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserMatch() throws Exception {
        int databaseSizeBeforeCreate = userMatchRepository.findAll().size();

        // Create the UserMatch
        restUserMatchMockMvc.perform(post("/api/user-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMatch)))
            .andExpect(status().isCreated());

        // Validate the UserMatch in the database
        List<UserMatch> userMatchList = userMatchRepository.findAll();
        assertThat(userMatchList).hasSize(databaseSizeBeforeCreate + 1);
        UserMatch testUserMatch = userMatchList.get(userMatchList.size() - 1);
        assertThat(testUserMatch.getScoreAtoB()).isEqualTo(DEFAULT_SCORE_ATO_B);
        assertThat(testUserMatch.getScoreBtoA()).isEqualTo(DEFAULT_SCORE_BTO_A);
        assertThat(testUserMatch.getScoreTotal()).isEqualTo(DEFAULT_SCORE_TOTAL);
        assertThat(testUserMatch.getRatio()).isEqualTo(DEFAULT_RATIO);
        assertThat(testUserMatch.getRankA()).isEqualTo(DEFAULT_RANK_A);
        assertThat(testUserMatch.getRankB()).isEqualTo(DEFAULT_RANK_B);
        assertThat(testUserMatch.getMatchType()).isEqualTo(DEFAULT_MATCH_TYPE);
        assertThat(testUserMatch.getPushStatus()).isEqualTo(DEFAULT_PUSH_STATUS);
    }

    @Test
    @Transactional
    public void createUserMatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userMatchRepository.findAll().size();

        // Create the UserMatch with an existing ID
        userMatch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMatchMockMvc.perform(post("/api/user-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMatch)))
            .andExpect(status().isBadRequest());

        // Validate the UserMatch in the database
        List<UserMatch> userMatchList = userMatchRepository.findAll();
        assertThat(userMatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserMatches() throws Exception {
        // Initialize the database
        userMatchRepository.saveAndFlush(userMatch);

        // Get all the userMatchList
        restUserMatchMockMvc.perform(get("/api/user-matches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].scoreAtoB").value(hasItem(DEFAULT_SCORE_ATO_B)))
            .andExpect(jsonPath("$.[*].scoreBtoA").value(hasItem(DEFAULT_SCORE_BTO_A)))
            .andExpect(jsonPath("$.[*].scoreTotal").value(hasItem(DEFAULT_SCORE_TOTAL)))
            .andExpect(jsonPath("$.[*].ratio").value(hasItem(DEFAULT_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].rankA").value(hasItem(DEFAULT_RANK_A)))
            .andExpect(jsonPath("$.[*].rankB").value(hasItem(DEFAULT_RANK_B)))
            .andExpect(jsonPath("$.[*].matchType").value(hasItem(DEFAULT_MATCH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pushStatus").value(hasItem(DEFAULT_PUSH_STATUS.toString())));
    }
    
    public void getAllUserMatchesWithEagerRelationshipsIsEnabled() throws Exception {
        UserMatchResource userMatchResource = new UserMatchResource(userMatchServiceMock);
        when(userMatchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restUserMatchMockMvc = MockMvcBuilders.standaloneSetup(userMatchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserMatchMockMvc.perform(get("/api/user-matches?eagerload=true"))
        .andExpect(status().isOk());

        verify(userMatchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllUserMatchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        UserMatchResource userMatchResource = new UserMatchResource(userMatchServiceMock);
            when(userMatchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restUserMatchMockMvc = MockMvcBuilders.standaloneSetup(userMatchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserMatchMockMvc.perform(get("/api/user-matches?eagerload=true"))
        .andExpect(status().isOk());

            verify(userMatchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserMatch() throws Exception {
        // Initialize the database
        userMatchRepository.saveAndFlush(userMatch);

        // Get the userMatch
        restUserMatchMockMvc.perform(get("/api/user-matches/{id}", userMatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userMatch.getId().intValue()))
            .andExpect(jsonPath("$.scoreAtoB").value(DEFAULT_SCORE_ATO_B))
            .andExpect(jsonPath("$.scoreBtoA").value(DEFAULT_SCORE_BTO_A))
            .andExpect(jsonPath("$.scoreTotal").value(DEFAULT_SCORE_TOTAL))
            .andExpect(jsonPath("$.ratio").value(DEFAULT_RATIO.doubleValue()))
            .andExpect(jsonPath("$.rankA").value(DEFAULT_RANK_A))
            .andExpect(jsonPath("$.rankB").value(DEFAULT_RANK_B))
            .andExpect(jsonPath("$.matchType").value(DEFAULT_MATCH_TYPE.toString()))
            .andExpect(jsonPath("$.pushStatus").value(DEFAULT_PUSH_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserMatch() throws Exception {
        // Get the userMatch
        restUserMatchMockMvc.perform(get("/api/user-matches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserMatch() throws Exception {
        // Initialize the database
        userMatchService.save(userMatch);

        int databaseSizeBeforeUpdate = userMatchRepository.findAll().size();

        // Update the userMatch
        UserMatch updatedUserMatch = userMatchRepository.findById(userMatch.getId()).get();
        // Disconnect from session so that the updates on updatedUserMatch are not directly saved in db
        em.detach(updatedUserMatch);
        updatedUserMatch
            .scoreAtoB(UPDATED_SCORE_ATO_B)
            .scoreBtoA(UPDATED_SCORE_BTO_A)
            .scoreTotal(UPDATED_SCORE_TOTAL)
            .ratio(UPDATED_RATIO)
            .rankA(UPDATED_RANK_A)
            .rankB(UPDATED_RANK_B)
            .matchType(UPDATED_MATCH_TYPE)
            .pushStatus(UPDATED_PUSH_STATUS);

        restUserMatchMockMvc.perform(put("/api/user-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserMatch)))
            .andExpect(status().isOk());

        // Validate the UserMatch in the database
        List<UserMatch> userMatchList = userMatchRepository.findAll();
        assertThat(userMatchList).hasSize(databaseSizeBeforeUpdate);
        UserMatch testUserMatch = userMatchList.get(userMatchList.size() - 1);
        assertThat(testUserMatch.getScoreAtoB()).isEqualTo(UPDATED_SCORE_ATO_B);
        assertThat(testUserMatch.getScoreBtoA()).isEqualTo(UPDATED_SCORE_BTO_A);
        assertThat(testUserMatch.getScoreTotal()).isEqualTo(UPDATED_SCORE_TOTAL);
        assertThat(testUserMatch.getRatio()).isEqualTo(UPDATED_RATIO);
        assertThat(testUserMatch.getRankA()).isEqualTo(UPDATED_RANK_A);
        assertThat(testUserMatch.getRankB()).isEqualTo(UPDATED_RANK_B);
        assertThat(testUserMatch.getMatchType()).isEqualTo(UPDATED_MATCH_TYPE);
        assertThat(testUserMatch.getPushStatus()).isEqualTo(UPDATED_PUSH_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingUserMatch() throws Exception {
        int databaseSizeBeforeUpdate = userMatchRepository.findAll().size();

        // Create the UserMatch

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMatchMockMvc.perform(put("/api/user-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMatch)))
            .andExpect(status().isBadRequest());

        // Validate the UserMatch in the database
        List<UserMatch> userMatchList = userMatchRepository.findAll();
        assertThat(userMatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserMatch() throws Exception {
        // Initialize the database
        userMatchService.save(userMatch);

        int databaseSizeBeforeDelete = userMatchRepository.findAll().size();

        // Get the userMatch
        restUserMatchMockMvc.perform(delete("/api/user-matches/{id}", userMatch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserMatch> userMatchList = userMatchRepository.findAll();
        assertThat(userMatchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMatch.class);
        UserMatch userMatch1 = new UserMatch();
        userMatch1.setId(1L);
        UserMatch userMatch2 = new UserMatch();
        userMatch2.setId(userMatch1.getId());
        assertThat(userMatch1).isEqualTo(userMatch2);
        userMatch2.setId(2L);
        assertThat(userMatch1).isNotEqualTo(userMatch2);
        userMatch1.setId(null);
        assertThat(userMatch1).isNotEqualTo(userMatch2);
    }
}
