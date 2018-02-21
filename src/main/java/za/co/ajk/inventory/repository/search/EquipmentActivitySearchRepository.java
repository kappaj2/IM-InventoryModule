package za.co.ajk.inventory.repository.search;

import za.co.ajk.inventory.domain.EquipmentActivity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EquipmentActivity entity.
 */
public interface EquipmentActivitySearchRepository extends ElasticsearchRepository<EquipmentActivity, Long> {
}
