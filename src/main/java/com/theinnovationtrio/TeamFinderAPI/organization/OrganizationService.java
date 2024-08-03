package com.theinnovationtrio.TeamFinderAPI.organization;

import com.theinnovationtrio.TeamFinderAPI.invite.IInviteService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final IInviteService inviteService;

    @Override
    public Organization createOrganization(OrganizationDto organizationDto, User user) {
        Organization organization = organizationMapper.INSTANCE.mappToOrganization(organizationDto);
        organization.setId(UUID.randomUUID());
        organization.setUsers(Arrays.asList(user));
        organization.setCreatedBy(user.getId());
        return organizationRepository.save(organization);
    }

    @Override
    public List<Organization> getAllOrganizations() {

        return organizationRepository.findAll();
    }

    @Override
    public Organization getOrganizationByInvite(UUID inviteId) {

        return getOrganizationById(inviteService.getInviteById(inviteId).getOrganizationId());
    }

    @Override
    public Organization getOrganizationById(UUID organizationId) {
        return organizationRepository.findById(organizationId).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
    }

    @Override
    public Organization updateOrganization(UUID organizationId, OrganizationDto organizationDto) {
        Organization organization = getOrganizationById(organizationId);
        Organization updatedOrganization = organizationMapper.INSTANCE.mappToOrganization(organizationDto);
        updatedOrganization.setId(organization.getId());
        updatedOrganization.setUsers(organization.getUsers());
        updatedOrganization.setCreatedBy(organization.getCreatedBy());
        return organizationRepository.save(updatedOrganization);
    }

    @Override
    public void deleteOrganizationById(UUID organizationId) {
        getOrganizationById(organizationId);
        organizationRepository.deleteById(organizationId);
    }
}
