package za.co.ajk.inventory.service.mapper;

import za.co.ajk.inventory.domain.*;
import za.co.ajk.inventory.service.dto.EquipmentActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EquipmentActivity and its DTO EquipmentActivityDTO.
 */
@Mapper(componentModel = "spring", uses = {EquipmentMapper.class})
public interface EquipmentActivityMapper extends EntityMapper<EquipmentActivityDTO, EquipmentActivity> {

    @Mapping(source = "equipment.id", target = "equipmentId")
    EquipmentActivityDTO toDto(EquipmentActivity equipmentActivity);

    @Mapping(source = "equipmentId", target = "equipment")
    EquipmentActivity toEntity(EquipmentActivityDTO equipmentActivityDTO);

    default EquipmentActivity fromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentActivity equipmentActivity = new EquipmentActivity();
        equipmentActivity.setId(id);
        return equipmentActivity;
    }
}
