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
import za.co.ajk.inventory.service.EquipmentService;
import za.co.ajk.inventory.service.dto.EquipmentDTO;
import za.co.ajk.inventory.web.rest.errors.BadRequestAlertException;
import za.co.ajk.inventory.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Equipment.
 */
@RestController
@RequestMapping("/api")
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);

    private static final String ENTITY_NAME = "equipment";

    private final EquipmentService equipmentService;

    public EquipmentResource(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * POST  /v1/equipment : Create a new equipment.
     *
     * @param equipmentDTO the equipmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipmentDTO, or with status 400 (Bad Request) if the equipment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/v1/equipment")
    @Timed
    public ResponseEntity<EquipmentDTO> createEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.created(new URI("/api/v1/equipment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /v1/equipment : Updates an existing equipment.
     *
     * @param equipmentDTO the equipmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipmentDTO,
     * or with status 400 (Bad Request) if the equipmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the equipmentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/v1/equipment")
    @Timed
    public ResponseEntity<EquipmentDTO> updateEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() == null) {
            return createEquipment(equipmentDTO);
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, equipmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /v1/equipment : get all the equipment.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of equipment in body
     */
    @GetMapping("/v1/equipment")
    @Timed
    public List<EquipmentDTO> getAllEquipment() {
        log.debug("REST request to get all Equipment");
        return equipmentService.findAll();
        }

    /**
     * GET  /v1/equipment/:id : get the "id" equipment.
     *
     * @param id the id of the equipmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipmentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/v1/equipment/{id}")
    @Timed
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        EquipmentDTO equipmentDTO = equipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(equipmentDTO));
    }

    /**
     * DELETE  /v1/equipment/:id : delete the "id" equipment.
     *
     * @param id the id of the equipmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/v1/equipment/{id}")
    @Timed
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /v1/_search/equipment?query=:query : search for the equipment corresponding
     * to the query.
     *
     * @param query the query of the equipment search
     * @return the result of the search
     */
    @GetMapping("/v1/_search/equipment")
    @Timed
    public List<EquipmentDTO> searchEquipment(@RequestParam String query) {
        log.debug("REST request to search Equipment for query {}", query);
        return equipmentService.search(query);
    }

}
