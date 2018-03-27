package za.co.ajk.inventory.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import za.co.ajk.inventory.service.RegionService;
import za.co.ajk.inventory.service.dto.RegionDTO;
import za.co.ajk.inventory.web.rest.errors.BadRequestAlertException;
import za.co.ajk.inventory.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Region.
 */
@RestController
@RequestMapping("/api")
public class RegionResource {

    private final Logger log = LoggerFactory.getLogger(RegionResource.class);

    private static final String ENTITY_NAME = "region";

    private final RegionService regionService;

    public RegionResource(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * POST  /v1/regions : Create a new region.
     *
     * @param regionDTO the regionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regionDTO, or with status 400 (Bad Request) if the region has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/v1/regions")
    @Timed
    public ResponseEntity<RegionDTO> createRegion(@Valid @RequestBody RegionDTO regionDTO) throws URISyntaxException {
        log.debug("REST request to save Region : {}", regionDTO);
        if (regionDTO.getId() != null) {
            throw new BadRequestAlertException("A new region cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegionDTO result = regionService.save(regionDTO);
        return ResponseEntity.created(new URI("/api/v1/regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /v1/regions : Updates an existing region.
     *
     * @param regionDTO the regionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regionDTO,
     * or with status 400 (Bad Request) if the regionDTO is not valid,
     * or with status 500 (Internal Server Error) if the regionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/v1/regions")
    @Timed
    public ResponseEntity<RegionDTO> updateRegion(@Valid @RequestBody RegionDTO regionDTO) throws URISyntaxException {
        log.debug("REST request to update Region : {}", regionDTO);
        if (regionDTO.getId() == null) {
            return createRegion(regionDTO);
        }
        RegionDTO result = regionService.save(regionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, regionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /v1/regions : get all the regions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of regions in body
     */
    @GetMapping("/v1/regions")
    @Timed
    public List<RegionDTO> getAllRegions() {
        log.debug("REST request to get all Regions");
        return regionService.findAll();
        }

    /**
     * GET  /v1/regions/:id : get the "id" region.
     *
     * @param id the id of the regionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/v1/regions/{id}")
    @Timed
    public ResponseEntity<RegionDTO> getRegion(@PathVariable Long id) {
        log.debug("REST request to get Region : {}", id);
        RegionDTO regionDTO = regionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(regionDTO));
    }

    /**
     * DELETE  /v1/regions/:id : delete the "id" region.
     *
     * @param id the id of the regionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/v1/regions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        log.debug("REST request to delete Region : {}", id);
        regionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/regions?query=:query : search for the region corresponding
     * to the query.
     *
     * @param query the query of the region search
     * @return the result of the search
     */
    @GetMapping("/v1/_search/regions")
    @Timed
    public List<RegionDTO> searchRegions(@RequestParam String query) {
        log.debug("REST request to search Regions for query {}", query);
        return regionService.search(query);
    }

}
