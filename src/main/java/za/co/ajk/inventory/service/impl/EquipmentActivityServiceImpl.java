package za.co.ajk.inventory.service.impl;

import za.co.ajk.inventory.service.EquipmentActivityService;
import za.co.ajk.inventory.domain.EquipmentActivity;
import za.co.ajk.inventory.repository.EquipmentActivityRepository;
import za.co.ajk.inventory.repository.search.EquipmentActivitySearchRepository;
import za.co.ajk.inventory.service.dto.EquipmentActivityDTO;
import za.co.ajk.inventory.service.mapper.EquipmentActivityMapper;
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
 * Service Implementation for managing EquipmentActivity.
 */
@Service
@Transactional
public class EquipmentActivityServiceImpl implements EquipmentActivityService {

    private final Logger log = LoggerFactory.getLogger(EquipmentActivityServiceImpl.class);

    private final EquipmentActivityRepository equipmentActivityRepository;

    private final EquipmentActivityMapper equipmentActivityMapper;

    private final EquipmentActivitySearchRepository equipmentActivitySearchRepository;

    public EquipmentActivityServiceImpl(EquipmentActivityRepository equipmentActivityRepository, EquipmentActivityMapper equipmentActivityMapper, EquipmentActivitySearchRepository equipmentActivitySearchRepository) {
        this.equipmentActivityRepository = equipmentActivityRepository;
        this.equipmentActivityMapper = equipmentActivityMapper;
        this.equipmentActivitySearchRepository = equipmentActivitySearchRepository;
    }

    /**
     * Save a equipmentActivity.
     *
     * @param equipmentActivityDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EquipmentActivityDTO save(EquipmentActivityDTO equipmentActivityDTO) {
        log.debug("Request to save EquipmentActivity : {}", equipmentActivityDTO);
        EquipmentActivity equipmentActivity = equipmentActivityMapper.toEntity(equipmentActivityDTO);
        equipmentActivity = equipmentActivityRepository.save(equipmentActivity);
        EquipmentActivityDTO result = equipmentActivityMapper.toDto(equipmentActivity);
        equipmentActivitySearchRepository.save(equipmentActivity);
        return result;
    }

    /**
     * Get all the equipmentActivities.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<EquipmentActivityDTO> findAll() {
        log.debug("Request to get all EquipmentActivities");
        return equipmentActivityRepository.findAll().stream()
            .map(equipmentActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one equipmentActivity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EquipmentActivityDTO findOne(Long id) {
        log.debug("Request to get EquipmentActivity : {}", id);
        EquipmentActivity equipmentActivity = equipmentActivityRepository.findOne(id);
        return equipmentActivityMapper.toDto(equipmentActivity);
    }

    /**
     * Delete the equipmentActivity by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EquipmentActivity : {}", id);
        equipmentActivityRepository.delete(id);
        equipmentActivitySearchRepository.delete(id);
    }

    /**
     * Search for the equipmentActivity corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<EquipmentActivityDTO> search(String query) {
        log.debug("Request to search EquipmentActivities for query {}", query);
        return StreamSupport
            .stream(equipmentActivitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(equipmentActivityMapper::toDto)
            .collect(Collectors.toList());
    }
}
