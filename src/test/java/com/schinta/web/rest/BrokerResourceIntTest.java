package com.schinta.web.rest;

import com.schinta.FormApp;

import com.schinta.domain.Broker;
import com.schinta.repository.BrokerRepository;
import com.schinta.service.BrokerService;
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
 * Test class for the BrokerResource REST controller.
 *
 * @see BrokerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FormApp.class)
public class BrokerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private BrokerRepository brokerRepository;
    
    @Autowired
    private BrokerService brokerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrokerMockMvc;

    private Broker broker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrokerResource brokerResource = new BrokerResource(brokerService);
        this.restBrokerMockMvc = MockMvcBuilders.standaloneSetup(brokerResource)
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
    public static Broker createEntity(EntityManager em) {
        Broker broker = new Broker()
            .name(DEFAULT_NAME);
        return broker;
    }

    @Before
    public void initTest() {
        broker = createEntity(em);
    }

    @Test
    @Transactional
    public void createBroker() throws Exception {
        int databaseSizeBeforeCreate = brokerRepository.findAll().size();

        // Create the Broker
        restBrokerMockMvc.perform(post("/api/brokers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(broker)))
            .andExpect(status().isCreated());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeCreate + 1);
        Broker testBroker = brokerList.get(brokerList.size() - 1);
        assertThat(testBroker.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBrokerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerRepository.findAll().size();

        // Create the Broker with an existing ID
        broker.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerMockMvc.perform(post("/api/brokers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(broker)))
            .andExpect(status().isBadRequest());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBrokers() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get all the brokerList
        restBrokerMockMvc.perform(get("/api/brokers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(broker.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", broker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(broker.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBroker() throws Exception {
        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBroker() throws Exception {
        // Initialize the database
        brokerService.save(broker);

        int databaseSizeBeforeUpdate = brokerRepository.findAll().size();

        // Update the broker
        Broker updatedBroker = brokerRepository.findById(broker.getId()).get();
        // Disconnect from session so that the updates on updatedBroker are not directly saved in db
        em.detach(updatedBroker);
        updatedBroker
            .name(UPDATED_NAME);

        restBrokerMockMvc.perform(put("/api/brokers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBroker)))
            .andExpect(status().isOk());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeUpdate);
        Broker testBroker = brokerList.get(brokerList.size() - 1);
        assertThat(testBroker.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBroker() throws Exception {
        int databaseSizeBeforeUpdate = brokerRepository.findAll().size();

        // Create the Broker

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerMockMvc.perform(put("/api/brokers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(broker)))
            .andExpect(status().isBadRequest());

        // Validate the Broker in the database
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBroker() throws Exception {
        // Initialize the database
        brokerService.save(broker);

        int databaseSizeBeforeDelete = brokerRepository.findAll().size();

        // Get the broker
        restBrokerMockMvc.perform(delete("/api/brokers/{id}", broker.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Broker> brokerList = brokerRepository.findAll();
        assertThat(brokerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Broker.class);
        Broker broker1 = new Broker();
        broker1.setId(1L);
        Broker broker2 = new Broker();
        broker2.setId(broker1.getId());
        assertThat(broker1).isEqualTo(broker2);
        broker2.setId(2L);
        assertThat(broker1).isNotEqualTo(broker2);
        broker1.setId(null);
        assertThat(broker1).isNotEqualTo(broker2);
    }
}
