package za.co.ajk.inventory.service.mapper;

import za.co.ajk.inventory.domain.*;
import za.co.ajk.inventory.service.dto.EquipmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Equipment and its DTO EquipmentDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface EquipmentMapper extends EntityMapper<EquipmentDTO, Equipment> {

    @Mapping(source = "company.id", target = "companyId")
    EquipmentDTO toDto(Equipment equipment);

    @Mapping(target = "equipmentActivities", ignore = true)
    @Mapping(source = "companyId", target = "company")
    Equipment toEntity(EquipmentDTO equipmentDTO);

    default Equipment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }
}
