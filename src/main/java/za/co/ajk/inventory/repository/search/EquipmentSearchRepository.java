package za.co.ajk.inventory.repository.search;

import za.co.ajk.inventory.domain.Equipment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Equipment entity.
 */
public interface EquipmentSearchRepository extends ElasticsearchRepository<Equipment, Long> {
}
