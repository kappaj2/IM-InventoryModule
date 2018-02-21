package za.co.ajk.inventory.service.mapper;

import za.co.ajk.inventory.domain.*;
import za.co.ajk.inventory.service.dto.CompanyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Company and its DTO CompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {RegionMapper.class})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {

    @Mapping(source = "region.id", target = "regionId")
    CompanyDTO toDto(Company company);

    @Mapping(target = "incidents", ignore = true)
    @Mapping(source = "regionId", target = "region")
    Company toEntity(CompanyDTO companyDTO);

    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
