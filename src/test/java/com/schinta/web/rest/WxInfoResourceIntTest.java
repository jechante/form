package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.WxInfo;
import com.schinta.repository.WxInfoRepository;
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
 * Test class for the WxInfoResource REST controller.
 *
 * @see WxInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class WxInfoResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_WX_OPEN_ID = "AAAAAAAAAA";
    private static final String UPDATED_WX_OPEN_ID = "BBBBBBBBBB";

    @Autowired
    private WxInfoRepository wxInfoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWxInfoMockMvc;

    private WxInfo wxInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WxInfoResource wxInfoResource = new WxInfoResource(wxInfoRepository);
        this.restWxInfoMockMvc = MockMvcBuilders.standaloneSetup(wxInfoResource)
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
    public static WxInfo createEntity(EntityManager em) {
        WxInfo wxInfo = new WxInfo()
            .userID(DEFAULT_USER_ID)
            .wxOpenID(DEFAULT_WX_OPEN_ID);
        return wxInfo;
    }

    @Before
    public void initTest() {
        wxInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createWxInfo() throws Exception {
        int databaseSizeBeforeCreate = wxInfoRepository.findAll().size();

        // Create the WxInfo
        restWxInfoMockMvc.perform(post("/api/wx-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxInfo)))
            .andExpect(status().isCreated());

        // Validate the WxInfo in the database
        List<WxInfo> wxInfoList = wxInfoRepository.findAll();
        assertThat(wxInfoList).hasSize(databaseSizeBeforeCreate + 1);
        WxInfo testWxInfo = wxInfoList.get(wxInfoList.size() - 1);
        assertThat(testWxInfo.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testWxInfo.getWxOpenID()).isEqualTo(DEFAULT_WX_OPEN_ID);
    }

    @Test
    @Transactional
    public void createWxInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wxInfoRepository.findAll().size();

        // Create the WxInfo with an existing ID
        wxInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWxInfoMockMvc.perform(post("/api/wx-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxInfo)))
            .andExpect(status().isBadRequest());

        // Validate the WxInfo in the database
        List<WxInfo> wxInfoList = wxInfoRepository.findAll();
        assertThat(wxInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWxInfos() throws Exception {
        // Initialize the database
        wxInfoRepository.saveAndFlush(wxInfo);

        // Get all the wxInfoList
        restWxInfoMockMvc.perform(get("/api/wx-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wxInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].wxOpenID").value(hasItem(DEFAULT_WX_OPEN_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getWxInfo() throws Exception {
        // Initialize the database
        wxInfoRepository.saveAndFlush(wxInfo);

        // Get the wxInfo
        restWxInfoMockMvc.perform(get("/api/wx-infos/{id}", wxInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wxInfo.getId().intValue()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.wxOpenID").value(DEFAULT_WX_OPEN_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWxInfo() throws Exception {
        // Get the wxInfo
        restWxInfoMockMvc.perform(get("/api/wx-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWxInfo() throws Exception {
        // Initialize the database
        wxInfoRepository.saveAndFlush(wxInfo);

        int databaseSizeBeforeUpdate = wxInfoRepository.findAll().size();

        // Update the wxInfo
        WxInfo updatedWxInfo = wxInfoRepository.findById(wxInfo.getId()).get();
        // Disconnect from session so that the updates on updatedWxInfo are not directly saved in db
        em.detach(updatedWxInfo);
        updatedWxInfo
            .userID(UPDATED_USER_ID)
            .wxOpenID(UPDATED_WX_OPEN_ID);

        restWxInfoMockMvc.perform(put("/api/wx-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWxInfo)))
            .andExpect(status().isOk());

        // Validate the WxInfo in the database
        List<WxInfo> wxInfoList = wxInfoRepository.findAll();
        assertThat(wxInfoList).hasSize(databaseSizeBeforeUpdate);
        WxInfo testWxInfo = wxInfoList.get(wxInfoList.size() - 1);
        assertThat(testWxInfo.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testWxInfo.getWxOpenID()).isEqualTo(UPDATED_WX_OPEN_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingWxInfo() throws Exception {
        int databaseSizeBeforeUpdate = wxInfoRepository.findAll().size();

        // Create the WxInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWxInfoMockMvc.perform(put("/api/wx-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wxInfo)))
            .andExpect(status().isBadRequest());

        // Validate the WxInfo in the database
        List<WxInfo> wxInfoList = wxInfoRepository.findAll();
        assertThat(wxInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWxInfo() throws Exception {
        // Initialize the database
        wxInfoRepository.saveAndFlush(wxInfo);

        int databaseSizeBeforeDelete = wxInfoRepository.findAll().size();

        // Get the wxInfo
        restWxInfoMockMvc.perform(delete("/api/wx-infos/{id}", wxInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WxInfo> wxInfoList = wxInfoRepository.findAll();
        assertThat(wxInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WxInfo.class);
        WxInfo wxInfo1 = new WxInfo();
        wxInfo1.setId(1L);
        WxInfo wxInfo2 = new WxInfo();
        wxInfo2.setId(wxInfo1.getId());
        assertThat(wxInfo1).isEqualTo(wxInfo2);
        wxInfo2.setId(2L);
        assertThat(wxInfo1).isNotEqualTo(wxInfo2);
        wxInfo1.setId(null);
        assertThat(wxInfo1).isNotEqualTo(wxInfo2);
    }
}
