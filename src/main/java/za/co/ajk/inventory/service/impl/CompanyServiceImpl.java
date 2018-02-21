package za.co.ajk.inventory.service.impl;

import za.co.ajk.inventory.service.CompanyService;
import za.co.ajk.inventory.domain.Company;
import za.co.ajk.inventory.repository.CompanyRepository;
import za.co.ajk.inventory.repository.search.CompanySearchRepository;
import za.co.ajk.inventory.service.dto.CompanyDTO;
import za.co.ajk.inventory.service.mapper.CompanyMapper;
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
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final CompanySearchRepository companySearchRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper, CompanySearchRepository companySearchRepository) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.companySearchRepository = companySearchRepository;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        CompanyDTO result = companyMapper.toDto(company);
        companySearchRepository.save(company);
        return result;
    }

    /**
     * Get all the companies.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> findAll() {
        log.debug("Request to get all Companies");
        return companyRepository.findAll().stream()
            .map(companyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CompanyDTO findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        Company company = companyRepository.findOne(id);
        return companyMapper.toDto(company);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.delete(id);
        companySearchRepository.delete(id);
    }

    /**
     * Search for the company corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> search(String query) {
        log.debug("Request to search Companies for query {}", query);
        return StreamSupport
            .stream(companySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(companyMapper::toDto)
            .collect(Collectors.toList());
    }
}
