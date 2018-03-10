package za.co.ajk.inventory.service.mapper;

import za.co.ajk.inventory.domain.*;
import za.co.ajk.inventory.service.dto.RegionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Region and its DTO RegionDTO.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {

    @Mapping(source = "country.id", target = "countryId")
    RegionDTO toDto(Region region);

    @Mapping(target = "incidents", ignore = true)
    @Mapping(source = "countryId", target = "country")
    Region toEntity(RegionDTO regionDTO);

    default Region fromId(Long id) {
        if (id == null) {
            return null;
        }
        Region region = new Region();
        region.setId(id);
        return region;
    }
}
