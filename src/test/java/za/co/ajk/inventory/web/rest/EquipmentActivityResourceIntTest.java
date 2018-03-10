package za.co.ajk.inventory.web.rest;

import za.co.ajk.inventory.InventoryModuleApp;

import za.co.ajk.inventory.domain.EquipmentActivity;
import za.co.ajk.inventory.repository.EquipmentActivityRepository;
import za.co.ajk.inventory.service.EquipmentActivityService;
import za.co.ajk.inventory.repository.search.EquipmentActivitySearchRepository;
import za.co.ajk.inventory.service.dto.EquipmentActivityDTO;
import za.co.ajk.inventory.service.mapper.EquipmentActivityMapper;
import za.co.ajk.inventory.web.rest.errors.ExceptionTranslator;

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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static za.co.ajk.inventory.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EquipmentActivityResource REST controller.
 *
 * @see EquipmentActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryModuleApp.class)
public class EquipmentActivityResourceIntTest {

    private static final String DEFAULT_ACTIVITY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTIVITY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVITY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACTIVITY_BY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_BY = "BBBBBBBBBB";

    @Autowired
    private EquipmentActivityRepository equipmentActivityRepository;

    @Autowired
    private EquipmentActivityMapper equipmentActivityMapper;

    @Autowired
    private EquipmentActivityService equipmentActivityService;

    @Autowired
    private EquipmentActivitySearchRepository equipmentActivitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEquipmentActivityMockMvc;

    private EquipmentActivity equipmentActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EquipmentActivityResource equipmentActivityResource = new EquipmentActivityResource(equipmentActivityService);
        this.restEquipmentActivityMockMvc = MockMvcBuilders.standaloneSetup(equipmentActivityResource)
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
    public static EquipmentActivity createEntity(EntityManager em) {
        EquipmentActivity equipmentActivity = new EquipmentActivity()
            .activityTypeCode(DEFAULT_ACTIVITY_TYPE_CODE)
            .activityDescription(DEFAULT_ACTIVITY_DESCRIPTION)
            .activityDate(DEFAULT_ACTIVITY_DATE)
            .activityBy(DEFAULT_ACTIVITY_BY);
        return equipmentActivity;
    }

    @Before
    public void initTest() {
        equipmentActivitySearchRepository.deleteAll();
        equipmentActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentActivity() throws Exception {
        int databaseSizeBeforeCreate = equipmentActivityRepository.findAll().size();

        // Create the EquipmentActivity
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(equipmentActivity);
        restEquipmentActivityMockMvc.perform(post("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentActivity in the database
        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentActivity testEquipmentActivity = equipmentActivityList.get(equipmentActivityList.size() - 1);
        assertThat(testEquipmentActivity.getActivityTypeCode()).isEqualTo(DEFAULT_ACTIVITY_TYPE_CODE);
        assertThat(testEquipmentActivity.getActivityDescription()).isEqualTo(DEFAULT_ACTIVITY_DESCRIPTION);
        assertThat(testEquipmentActivity.getActivityDate()).isEqualTo(DEFAULT_ACTIVITY_DATE);
        assertThat(testEquipmentActivity.getActivityBy()).isEqualTo(DEFAULT_ACTIVITY_BY);

        // Validate the EquipmentActivity in Elasticsearch
        EquipmentActivity equipmentActivityEs = equipmentActivitySearchRepository.findOne(testEquipmentActivity.getId());
        assertThat(equipmentActivityEs).isEqualToIgnoringGivenFields(testEquipmentActivity);
    }

    @Test
    @Transactional
    public void createEquipmentActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentActivityRepository.findAll().size();

        // Create the EquipmentActivity with an existing ID
        equipmentActivity.setId(1L);
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(equipmentActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentActivityMockMvc.perform(post("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentActivity in the database
        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActivityTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentActivityRepository.findAll().size();
        // set the field null
        equipmentActivity.setActivityTypeCode(null);

        // Create the EquipmentActivity, which fails.
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(equipmentActivity);

        restEquipmentActivityMockMvc.perform(post("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActivityDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentActivityRepository.findAll().size();
        // set the field null
        equipmentActivity.setActivityDescription(null);

        // Create the EquipmentActivity, which fails.
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(equipmentActivity);

        restEquipmentActivityMockMvc.perform(post("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActivityDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentActivityRepository.findAll().size();
        // set the field null
        equipmentActivity.setActivityDate(null);

        // Create the EquipmentActivity, which fails.
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(equipmentActivity);

        restEquipmentActivityMockMvc.perform(post("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActivityByIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentActivityRepository.findAll().size();
        // set the field null
        equipmentActivity.setActivityBy(null);

        // Create the EquipmentActivity, which fails.
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(equipmentActivity);

        restEquipmentActivityMockMvc.perform(post("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentActivities() throws Exception {
        // Initialize the database
        equipmentActivityRepository.saveAndFlush(equipmentActivity);

        // Get all the equipmentActivityList
        restEquipmentActivityMockMvc.perform(get("/api/equipment-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityTypeCode").value(hasItem(DEFAULT_ACTIVITY_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].activityDescription").value(hasItem(DEFAULT_ACTIVITY_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activityDate").value(hasItem(DEFAULT_ACTIVITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].activityBy").value(hasItem(DEFAULT_ACTIVITY_BY.toString())));
    }

    @Test
    @Transactional
    public void getEquipmentActivity() throws Exception {
        // Initialize the database
        equipmentActivityRepository.saveAndFlush(equipmentActivity);

        // Get the equipmentActivity
        restEquipmentActivityMockMvc.perform(get("/api/equipment-activities/{id}", equipmentActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentActivity.getId().intValue()))
            .andExpect(jsonPath("$.activityTypeCode").value(DEFAULT_ACTIVITY_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.activityDescription").value(DEFAULT_ACTIVITY_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activityDate").value(DEFAULT_ACTIVITY_DATE.toString()))
            .andExpect(jsonPath("$.activityBy").value(DEFAULT_ACTIVITY_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentActivity() throws Exception {
        // Get the equipmentActivity
        restEquipmentActivityMockMvc.perform(get("/api/equipment-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentActivity() throws Exception {
        // Initialize the database
        equipmentActivityRepository.saveAndFlush(equipmentActivity);
        equipmentActivitySearchRepository.save(equipmentActivity);
        int databaseSizeBeforeUpdate = equipmentActivityRepository.findAll().size();

        // Update the equipmentActivity
        EquipmentActivity updatedEquipmentActivity = equipmentActivityRepository.findOne(equipmentActivity.getId());
        // Disconnect from session so that the updates on updatedEquipmentActivity are not directly saved in db
        em.detach(updatedEquipmentActivity);
        updatedEquipmentActivity
            .activityTypeCode(UPDATED_ACTIVITY_TYPE_CODE)
            .activityDescription(UPDATED_ACTIVITY_DESCRIPTION)
            .activityDate(UPDATED_ACTIVITY_DATE)
            .activityBy(UPDATED_ACTIVITY_BY);
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(updatedEquipmentActivity);

        restEquipmentActivityMockMvc.perform(put("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentActivity in the database
        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeUpdate);
        EquipmentActivity testEquipmentActivity = equipmentActivityList.get(equipmentActivityList.size() - 1);
        assertThat(testEquipmentActivity.getActivityTypeCode()).isEqualTo(UPDATED_ACTIVITY_TYPE_CODE);
        assertThat(testEquipmentActivity.getActivityDescription()).isEqualTo(UPDATED_ACTIVITY_DESCRIPTION);
        assertThat(testEquipmentActivity.getActivityDate()).isEqualTo(UPDATED_ACTIVITY_DATE);
        assertThat(testEquipmentActivity.getActivityBy()).isEqualTo(UPDATED_ACTIVITY_BY);

        // Validate the EquipmentActivity in Elasticsearch
        EquipmentActivity equipmentActivityEs = equipmentActivitySearchRepository.findOne(testEquipmentActivity.getId());
        assertThat(equipmentActivityEs).isEqualToIgnoringGivenFields(testEquipmentActivity);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentActivity() throws Exception {
        int databaseSizeBeforeUpdate = equipmentActivityRepository.findAll().size();

        // Create the EquipmentActivity
        EquipmentActivityDTO equipmentActivityDTO = equipmentActivityMapper.toDto(equipmentActivity);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEquipmentActivityMockMvc.perform(put("/api/equipment-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentActivity in the database
        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEquipmentActivity() throws Exception {
        // Initialize the database
        equipmentActivityRepository.saveAndFlush(equipmentActivity);
        equipmentActivitySearchRepository.save(equipmentActivity);
        int databaseSizeBeforeDelete = equipmentActivityRepository.findAll().size();

        // Get the equipmentActivity
        restEquipmentActivityMockMvc.perform(delete("/api/equipment-activities/{id}", equipmentActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean equipmentActivityExistsInEs = equipmentActivitySearchRepository.exists(equipmentActivity.getId());
        assertThat(equipmentActivityExistsInEs).isFalse();

        // Validate the database is empty
        List<EquipmentActivity> equipmentActivityList = equipmentActivityRepository.findAll();
        assertThat(equipmentActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEquipmentActivity() throws Exception {
        // Initialize the database
        equipmentActivityRepository.saveAndFlush(equipmentActivity);
        equipmentActivitySearchRepository.save(equipmentActivity);

        // Search the equipmentActivity
        restEquipmentActivityMockMvc.perform(get("/api/_search/equipment-activities?query=id:" + equipmentActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityTypeCode").value(hasItem(DEFAULT_ACTIVITY_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].activityDescription").value(hasItem(DEFAULT_ACTIVITY_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activityDate").value(hasItem(DEFAULT_ACTIVITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].activityBy").value(hasItem(DEFAULT_ACTIVITY_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentActivity.class);
        EquipmentActivity equipmentActivity1 = new EquipmentActivity();
        equipmentActivity1.setId(1L);
        EquipmentActivity equipmentActivity2 = new EquipmentActivity();
        equipmentActivity2.setId(equipmentActivity1.getId());
        assertThat(equipmentActivity1).isEqualTo(equipmentActivity2);
        equipmentActivity2.setId(2L);
        assertThat(equipmentActivity1).isNotEqualTo(equipmentActivity2);
        equipmentActivity1.setId(null);
        assertThat(equipmentActivity1).isNotEqualTo(equipmentActivity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentActivityDTO.class);
        EquipmentActivityDTO equipmentActivityDTO1 = new EquipmentActivityDTO();
        equipmentActivityDTO1.setId(1L);
        EquipmentActivityDTO equipmentActivityDTO2 = new EquipmentActivityDTO();
        assertThat(equipmentActivityDTO1).isNotEqualTo(equipmentActivityDTO2);
        equipmentActivityDTO2.setId(equipmentActivityDTO1.getId());
        assertThat(equipmentActivityDTO1).isEqualTo(equipmentActivityDTO2);
        equipmentActivityDTO2.setId(2L);
        assertThat(equipmentActivityDTO1).isNotEqualTo(equipmentActivityDTO2);
        equipmentActivityDTO1.setId(null);
        assertThat(equipmentActivityDTO1).isNotEqualTo(equipmentActivityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(equipmentActivityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(equipmentActivityMapper.fromId(null)).isNull();
    }
}
