package com.theinnovationtrio.TeamFinderAPI.organization;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    Organization mappToOrganization(OrganizationDto organizationDto);
}
