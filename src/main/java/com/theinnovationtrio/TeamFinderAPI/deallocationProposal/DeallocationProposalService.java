package com.theinnovationtrio.TeamFinderAPI.deallocationProposal;

import com.theinnovationtrio.TeamFinderAPI.assignmentProposal.IAssignmentProposalService;
import com.theinnovationtrio.TeamFinderAPI.department.IDepartmentService;
import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.IUserProjectRoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeallocationProposalService implements IDeallocationProposalService {

    private final DeallocationProposalRepository deallocationRepository;
    private final IUserProjectRoleService userProjectRoleService;
    private final IUserService userService;
    //private final IAssignmentProposalService assignmentProposalService;
    private final IDepartmentService departmentService;


    @Override
    public DeallocationProposal createDeallocation(DeallocationProposalDto deallocationProposalDto) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if (hasProjectManagerRole) {

            var deallocation = DeallocationProposal.builder()
                    .id(UUID.randomUUID())
                    .deallocationReason(deallocationProposalDto.getDeallocationReason())
                    .userId(deallocationProposalDto.getUserId())
                    .projectId(deallocationProposalDto.getProjectId())
                    .status(ProposalStatus.WAITING)
                    .build();
            deallocationRepository.save(deallocation);
            return deallocation;
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }

    @Override
    public DeallocationProposal getDeallocationById(UUID deallocationId) {
        return deallocationRepository.findById(deallocationId)
                .orElseThrow(() -> new EntityNotFoundException("Deallocation Proposal not found"));
    }

    @Override
    public List<DeallocationProposal> getAllDealocations() {
        return deallocationRepository.findAll();
    }

    @Override
    public List<DeallocationProposal> getWaitingDeallocationsByProjectId(UUID projectId) {
        var deall = deallocationRepository.findAllByProjectId(projectId);

        return deall.stream().filter(d -> d.getStatus().equals(ProposalStatus.WAITING)).toList();
    }

    @Override
    public List<DeallocationProposal> getAllDeallocationsByUserId(UUID userId) {
        var deall = deallocationRepository.findAllByUserId(userId);

        return deall.stream().filter(d->d.getStatus().equals(ProposalStatus.WAITING)).toList();
    }

    @Override
    public List<DeallocationProposal> getAllDeallocationsByDepartManager() {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDepartmentManagerRole) {
        List<DeallocationProposal> deallocationProposalList = new ArrayList<>();
        var depart = departmentService.getDepartmentByManager(connectedUser.getId());
            for(var user: depart.getUsers()) {
                    deallocationProposalList.addAll(getAllDeallocationsByUserId(user.getId()));
            }
            deallocationProposalList.addAll(getAllDeallocationsByUserId(depart.getDepartmentManager()));
            return deallocationProposalList;
            }
         else {
            throw new AccessDeniedException("Unauthorized access!");
        }

    }

    @Override
    public List<DeallocationProposal> getAllDeallocationsByProposalStatus(ProposalStatus proposalStatus) {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if (hasProjectManagerRole) {
            deallocationRepository.getAllByStatusAndOrganizationId(proposalStatus, connectedUser.getOrganizationId());
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
        return null;
    }

    @Override
    public DeallocationProposal updateDeallocationById(UUID deallocationId, DeallocationProposalDto deallocationProposalDto) {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if (hasProjectManagerRole) {
            DeallocationProposal deallocationProposalToUpdate = getDeallocationById(deallocationId);
            deallocationProposalToUpdate.setDeallocationReason(deallocationProposalDto.getDeallocationReason());
            deallocationProposalToUpdate.setUserId(deallocationProposalDto.getUserId());
            deallocationProposalToUpdate.setProjectId(deallocationProposalDto.getProjectId());
            return deallocationRepository.save(deallocationProposalToUpdate);
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }

    @Override
    public DeallocationProposal updateDeallocationProposalStatusById(UUID deallocationId, ProposalStatus proposalStatus) {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDeparttManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDeparttManagerRole) {
            DeallocationProposal deallocationProposal = getDeallocationById(deallocationId);

            if (proposalStatus.equals(ProposalStatus.ACCEPTED)) {

                var userProjectRole = userProjectRoleService.getUserProjectRoleByProjectIdANDUserId(deallocationProposal.getProjectId(), deallocationProposal.getUserId());

                userService.addAvailableHoursByUserId(userProjectRole.getUserId(), userProjectRole.getWorkHours());

                userProjectRoleService.updateUserProjectRoleStatusById(userProjectRole.getId(), StatusOfMember.PAST);
            }

            deallocationProposal.setStatus(proposalStatus);
            return deallocationRepository.save(deallocationProposal);
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }

    @Override
    public void deleteDeallocationById(UUID deallocationId) {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if (hasProjectManagerRole) {
            getDeallocationById(deallocationId);

            deallocationRepository.deleteById(deallocationId);
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }
}
