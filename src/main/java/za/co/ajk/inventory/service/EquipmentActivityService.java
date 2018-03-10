package za.co.ajk.inventory.service;

import za.co.ajk.inventory.service.dto.EquipmentActivityDTO;
import java.util.List;

/**
 * Service Interface for managing EquipmentActivity.
 */
public interface EquipmentActivityService {

    /**
     * Save a equipmentActivity.
     *
     * @param equipmentActivityDTO the entity to save
     * @return the persisted entity
     */
    EquipmentActivityDTO save(EquipmentActivityDTO equipmentActivityDTO);

    /**
     * Get all the equipmentActivities.
     *
     * @return the list of entities
     */
    List<EquipmentActivityDTO> findAll();

    /**
     * Get the "id" equipmentActivity.
     *
     * @param id the id of the entity
     * @return the entity
     */
    EquipmentActivityDTO findOne(Long id);

    /**
     * Delete the "id" equipmentActivity.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the equipmentActivity corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<EquipmentActivityDTO> search(String query);
}
