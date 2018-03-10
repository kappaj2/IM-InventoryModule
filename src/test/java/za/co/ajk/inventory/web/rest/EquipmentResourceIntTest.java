package za.co.ajk.inventory.web.rest;

import za.co.ajk.inventory.InventoryModuleApp;

import za.co.ajk.inventory.domain.Equipment;
import za.co.ajk.inventory.repository.EquipmentRepository;
import za.co.ajk.inventory.service.EquipmentService;
import za.co.ajk.inventory.repository.search.EquipmentSearchRepository;
import za.co.ajk.inventory.service.dto.EquipmentDTO;
import za.co.ajk.inventory.service.mapper.EquipmentMapper;
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
 * Test class for the EquipmentResource REST controller.
 *
 * @see EquipmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryModuleApp.class)
public class EquipmentResourceIntTest {

    private static final Integer DEFAULT_EQUIPMENT_ID = 1;
    private static final Integer UPDATED_EQUIPMENT_ID = 2;

    private static final String DEFAULT_EQUIPMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_GROUP = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CURRENTLY_AVAILABLE = false;
    private static final Boolean UPDATED_CURRENTLY_AVAILABLE = true;

    private static final Instant DEFAULT_DATE_ADDED_TO_STOCK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ADDED_TO_STOCK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_REMOVED_FROM_STOCK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REMOVED_FROM_STOCK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REMOVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_REMOVED_BY = "BBBBBBBBBB";

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentSearchRepository equipmentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EquipmentResource equipmentResource = new EquipmentResource(equipmentService);
        this.restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
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
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment()
            .equipmentId(DEFAULT_EQUIPMENT_ID)
            .equipmentName(DEFAULT_EQUIPMENT_NAME)
            .equipmentGroup(DEFAULT_EQUIPMENT_GROUP)
            .equipmentSerialNumber(DEFAULT_EQUIPMENT_SERIAL_NUMBER)
            .equipmentBarcode(DEFAULT_EQUIPMENT_BARCODE)
            .equipmentDescription(DEFAULT_EQUIPMENT_DESCRIPTION)
            .currentlyAvailable(DEFAULT_CURRENTLY_AVAILABLE)
            .dateAddedToStock(DEFAULT_DATE_ADDED_TO_STOCK)
            .addedBy(DEFAULT_ADDED_BY)
            .dateRemovedFromStock(DEFAULT_DATE_REMOVED_FROM_STOCK)
            .removedBy(DEFAULT_REMOVED_BY);
        return equipment;
    }

    @Before
    public void initTest() {
        equipmentSearchRepository.deleteAll();
        equipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentId()).isEqualTo(DEFAULT_EQUIPMENT_ID);
        assertThat(testEquipment.getEquipmentName()).isEqualTo(DEFAULT_EQUIPMENT_NAME);
        assertThat(testEquipment.getEquipmentGroup()).isEqualTo(DEFAULT_EQUIPMENT_GROUP);
        assertThat(testEquipment.getEquipmentSerialNumber()).isEqualTo(DEFAULT_EQUIPMENT_SERIAL_NUMBER);
        assertThat(testEquipment.getEquipmentBarcode()).isEqualTo(DEFAULT_EQUIPMENT_BARCODE);
        assertThat(testEquipment.getEquipmentDescription()).isEqualTo(DEFAULT_EQUIPMENT_DESCRIPTION);
        assertThat(testEquipment.isCurrentlyAvailable()).isEqualTo(DEFAULT_CURRENTLY_AVAILABLE);
        assertThat(testEquipment.getDateAddedToStock()).isEqualTo(DEFAULT_DATE_ADDED_TO_STOCK);
        assertThat(testEquipment.getAddedBy()).isEqualTo(DEFAULT_ADDED_BY);
        assertThat(testEquipment.getDateRemovedFromStock()).isEqualTo(DEFAULT_DATE_REMOVED_FROM_STOCK);
        assertThat(testEquipment.getRemovedBy()).isEqualTo(DEFAULT_REMOVED_BY);

        // Validate the Equipment in Elasticsearch
        Equipment equipmentEs = equipmentSearchRepository.findOne(testEquipment.getId());
        assertThat(equipmentEs).isEqualToIgnoringGivenFields(testEquipment);
    }

    @Test
    @Transactional
    public void createEquipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment with an existing ID
        equipment.setId(1L);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEquipmentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setEquipmentId(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setEquipmentName(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setEquipmentGroup(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentlyAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setCurrentlyAvailable(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateAddedToStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setDateAddedToStock(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setAddedBy(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentId").value(hasItem(DEFAULT_EQUIPMENT_ID)))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentGroup").value(hasItem(DEFAULT_EQUIPMENT_GROUP.toString())))
            .andExpect(jsonPath("$.[*].equipmentSerialNumber").value(hasItem(DEFAULT_EQUIPMENT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].equipmentBarcode").value(hasItem(DEFAULT_EQUIPMENT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].equipmentDescription").value(hasItem(DEFAULT_EQUIPMENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].currentlyAvailable").value(hasItem(DEFAULT_CURRENTLY_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAddedToStock").value(hasItem(DEFAULT_DATE_ADDED_TO_STOCK.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY.toString())))
            .andExpect(jsonPath("$.[*].dateRemovedFromStock").value(hasItem(DEFAULT_DATE_REMOVED_FROM_STOCK.toString())))
            .andExpect(jsonPath("$.[*].removedBy").value(hasItem(DEFAULT_REMOVED_BY.toString())));
    }

    @Test
    @Transactional
    public void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.equipmentId").value(DEFAULT_EQUIPMENT_ID))
            .andExpect(jsonPath("$.equipmentName").value(DEFAULT_EQUIPMENT_NAME.toString()))
            .andExpect(jsonPath("$.equipmentGroup").value(DEFAULT_EQUIPMENT_GROUP.toString()))
            .andExpect(jsonPath("$.equipmentSerialNumber").value(DEFAULT_EQUIPMENT_SERIAL_NUMBER.toString()))
            .andExpect(jsonPath("$.equipmentBarcode").value(DEFAULT_EQUIPMENT_BARCODE.toString()))
            .andExpect(jsonPath("$.equipmentDescription").value(DEFAULT_EQUIPMENT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.currentlyAvailable").value(DEFAULT_CURRENTLY_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.dateAddedToStock").value(DEFAULT_DATE_ADDED_TO_STOCK.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY.toString()))
            .andExpect(jsonPath("$.dateRemovedFromStock").value(DEFAULT_DATE_REMOVED_FROM_STOCK.toString()))
            .andExpect(jsonPath("$.removedBy").value(DEFAULT_REMOVED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        equipmentSearchRepository.save(equipment);
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findOne(equipment.getId());
        // Disconnect from session so that the updates on updatedEquipment are not directly saved in db
        em.detach(updatedEquipment);
        updatedEquipment
            .equipmentId(UPDATED_EQUIPMENT_ID)
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .equipmentGroup(UPDATED_EQUIPMENT_GROUP)
            .equipmentSerialNumber(UPDATED_EQUIPMENT_SERIAL_NUMBER)
            .equipmentBarcode(UPDATED_EQUIPMENT_BARCODE)
            .equipmentDescription(UPDATED_EQUIPMENT_DESCRIPTION)
            .currentlyAvailable(UPDATED_CURRENTLY_AVAILABLE)
            .dateAddedToStock(UPDATED_DATE_ADDED_TO_STOCK)
            .addedBy(UPDATED_ADDED_BY)
            .dateRemovedFromStock(UPDATED_DATE_REMOVED_FROM_STOCK)
            .removedBy(UPDATED_REMOVED_BY);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(updatedEquipment);

        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentId()).isEqualTo(UPDATED_EQUIPMENT_ID);
        assertThat(testEquipment.getEquipmentName()).isEqualTo(UPDATED_EQUIPMENT_NAME);
        assertThat(testEquipment.getEquipmentGroup()).isEqualTo(UPDATED_EQUIPMENT_GROUP);
        assertThat(testEquipment.getEquipmentSerialNumber()).isEqualTo(UPDATED_EQUIPMENT_SERIAL_NUMBER);
        assertThat(testEquipment.getEquipmentBarcode()).isEqualTo(UPDATED_EQUIPMENT_BARCODE);
        assertThat(testEquipment.getEquipmentDescription()).isEqualTo(UPDATED_EQUIPMENT_DESCRIPTION);
        assertThat(testEquipment.isCurrentlyAvailable()).isEqualTo(UPDATED_CURRENTLY_AVAILABLE);
        assertThat(testEquipment.getDateAddedToStock()).isEqualTo(UPDATED_DATE_ADDED_TO_STOCK);
        assertThat(testEquipment.getAddedBy()).isEqualTo(UPDATED_ADDED_BY);
        assertThat(testEquipment.getDateRemovedFromStock()).isEqualTo(UPDATED_DATE_REMOVED_FROM_STOCK);
        assertThat(testEquipment.getRemovedBy()).isEqualTo(UPDATED_REMOVED_BY);

        // Validate the Equipment in Elasticsearch
        Equipment equipmentEs = equipmentSearchRepository.findOne(testEquipment.getId());
        assertThat(equipmentEs).isEqualToIgnoringGivenFields(testEquipment);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        equipmentSearchRepository.save(equipment);
        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Get the equipment
        restEquipmentMockMvc.perform(delete("/api/equipment/{id}", equipment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean equipmentExistsInEs = equipmentSearchRepository.exists(equipment.getId());
        assertThat(equipmentExistsInEs).isFalse();

        // Validate the database is empty
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        equipmentSearchRepository.save(equipment);

        // Search the equipment
        restEquipmentMockMvc.perform(get("/api/_search/equipment?query=id:" + equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentId").value(hasItem(DEFAULT_EQUIPMENT_ID)))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentGroup").value(hasItem(DEFAULT_EQUIPMENT_GROUP.toString())))
            .andExpect(jsonPath("$.[*].equipmentSerialNumber").value(hasItem(DEFAULT_EQUIPMENT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].equipmentBarcode").value(hasItem(DEFAULT_EQUIPMENT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].equipmentDescription").value(hasItem(DEFAULT_EQUIPMENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].currentlyAvailable").value(hasItem(DEFAULT_CURRENTLY_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAddedToStock").value(hasItem(DEFAULT_DATE_ADDED_TO_STOCK.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY.toString())))
            .andExpect(jsonPath("$.[*].dateRemovedFromStock").value(hasItem(DEFAULT_DATE_REMOVED_FROM_STOCK.toString())))
            .andExpect(jsonPath("$.[*].removedBy").value(hasItem(DEFAULT_REMOVED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipment.class);
        Equipment equipment1 = new Equipment();
        equipment1.setId(1L);
        Equipment equipment2 = new Equipment();
        equipment2.setId(equipment1.getId());
        assertThat(equipment1).isEqualTo(equipment2);
        equipment2.setId(2L);
        assertThat(equipment1).isNotEqualTo(equipment2);
        equipment1.setId(null);
        assertThat(equipment1).isNotEqualTo(equipment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentDTO.class);
        EquipmentDTO equipmentDTO1 = new EquipmentDTO();
        equipmentDTO1.setId(1L);
        EquipmentDTO equipmentDTO2 = new EquipmentDTO();
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO2.setId(equipmentDTO1.getId());
        assertThat(equipmentDTO1).isEqualTo(equipmentDTO2);
        equipmentDTO2.setId(2L);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO1.setId(null);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(equipmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(equipmentMapper.fromId(null)).isNull();
    }
}
