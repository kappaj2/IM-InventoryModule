package za.co.ajk.inventory.repository;

import za.co.ajk.inventory.domain.EquipmentActivity;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EquipmentActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentActivityRepository extends JpaRepository<EquipmentActivity, Long> {

}
