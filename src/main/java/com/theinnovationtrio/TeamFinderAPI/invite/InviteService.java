package com.theinnovationtrio.TeamFinderAPI.invite;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService implements IInviteService {

    private final InviteRepository inviteRepository;


    @Override
    public Invite createInvite() {

        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if (hasAdminRole) {
            Invite invite = new Invite();
            invite.setId(UUID.randomUUID());
            invite.setOrganizationId(adminUser.getOrganizationId());
            invite.setCreatedBy(adminUser.getId());
            invite.setAvailable(true);
            return inviteRepository.save(invite);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }

    }

    @Override
    public boolean existsById(UUID inviteId) {

        return inviteRepository.existsById(inviteId);
    }

    @Override
    public Invite getInviteById(UUID inviteId) {

        return inviteRepository.findById(inviteId)
                .orElseThrow(() -> new EntityNotFoundException("Invite not found"));
    }


    @Override
    public List<Invite> getAllInvites() {

        return inviteRepository.findAll();
    }

    @Override
    public Invite updateInvite(UUID inviteId, boolean available) {

        Invite invite = getInviteById(inviteId);
        invite.setAvailable(available);
        return inviteRepository.save(invite);
    }

    @Override
    public void deleteInviteById(UUID inviteId) {

        getInviteById(inviteId);
        inviteRepository.deleteById(inviteId);
    }
}
