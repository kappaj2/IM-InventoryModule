package za.co.ajk.inventory.service;

import za.co.ajk.inventory.service.dto.CompanyDTO;
import java.util.List;

/**
 * Service Interface for managing Company.
 */
public interface CompanyService {

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save
     * @return the persisted entity
     */
    CompanyDTO save(CompanyDTO companyDTO);

    /**
     * Get all the companies.
     *
     * @return the list of entities
     */
    List<CompanyDTO> findAll();

    /**
     * Get the "id" company.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CompanyDTO findOne(Long id);

    /**
     * Delete the "id" company.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the company corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CompanyDTO> search(String query);
}
