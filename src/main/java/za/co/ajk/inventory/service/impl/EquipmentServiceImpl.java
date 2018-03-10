package za.co.ajk.inventory.service.impl;

import za.co.ajk.inventory.service.EquipmentService;
import za.co.ajk.inventory.domain.Equipment;
import za.co.ajk.inventory.repository.EquipmentRepository;
import za.co.ajk.inventory.repository.search.EquipmentSearchRepository;
import za.co.ajk.inventory.service.dto.EquipmentDTO;
import za.co.ajk.inventory.service.mapper.EquipmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Equipment.
 */
@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    private final EquipmentSearchRepository equipmentSearchRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper, EquipmentSearchRepository equipmentSearchRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.equipmentSearchRepository = equipmentSearchRepository;
    }

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        log.debug("Request to save Equipment : {}", equipmentDTO);
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        EquipmentDTO result = equipmentMapper.toDto(equipment);
        equipmentSearchRepository.save(equipment);
        return result;
    }

    /**
     * Get all the equipment.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<EquipmentDTO> findAll() {
        log.debug("Request to get all Equipment");
        return equipmentRepository.findAll().stream()
            .map(equipmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one equipment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EquipmentDTO findOne(Long id) {
        log.debug("Request to get Equipment : {}", id);
        Equipment equipment = equipmentRepository.findOne(id);
        return equipmentMapper.toDto(equipment);
    }

    /**
     * Delete the equipment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Equipment : {}", id);
        equipmentRepository.delete(id);
        equipmentSearchRepository.delete(id);
    }

    /**
     * Search for the equipment corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<EquipmentDTO> search(String query) {
        log.debug("Request to search Equipment for query {}", query);
        return StreamSupport
            .stream(equipmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(equipmentMapper::toDto)
            .collect(Collectors.toList());
    }
}
