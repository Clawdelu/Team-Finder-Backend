package com.theinnovationtrio.TeamFinderAPI.assignmentProposal;

import com.theinnovationtrio.TeamFinderAPI.department.IDepartmentService;
import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
import com.theinnovationtrio.TeamFinderAPI.teamRole.ITeamRoleService;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.IUserProjectRoleService;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.UserProjectRoleDto;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.UserProjectRoleUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentProposalService implements IAssignmentProposalService{

    private final AssignmentProposalRepository assignmentProposalRepository;
    private final ITeamRoleService teamRoleService;
    private final IUserProjectRoleService userProjectRoleService;
    private final IUserService userService;
    private final IDepartmentService departmentService;

    @Override
    public AssignmentProposal createAssignment(AssignmentProposalDto assignmentProposalDto) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if(hasProjectManagerRole) {

            UserProjectRoleDto userProjectRoleDto = UserProjectRoleDto.builder()
                    .userId(assignmentProposalDto.getUserId())
                    .projectId(assignmentProposalDto.getProjectId())
                    .teamRolesIds(assignmentProposalDto.getTeamRoleIds())
                    .workHours(assignmentProposalDto.getWorkHours())
                    .build();
            userProjectRoleService.createUserProjectRole(userProjectRoleDto);

            var assign = AssignmentProposal.builder()
                    .id(UUID.randomUUID())
                    .workHours(assignmentProposalDto.getWorkHours())
                    .teamRoles(teamRoleService.getListOfTeamRoleByIds(assignmentProposalDto.getTeamRoleIds()))
                    .comments(assignmentProposalDto.getComments())
                    .userId(assignmentProposalDto.getUserId())
                    .projectId(assignmentProposalDto.getProjectId())
                    .status(ProposalStatus.WAITING)
                    .build();
            assignmentProposalRepository.save(assign);
            return assign;
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }

    @Override
    public AssignmentProposal getAssignmentById(UUID assignmentId) {
        return assignmentProposalRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment Proposal not found"));
    }

    @Override
    public List<AssignmentProposal> getAllAssignments() {
        return assignmentProposalRepository.findAll();
    }

    @Override
    public List<AssignmentProposal> getAllAssignmentsByUserID(UUID userId) {
        var assList = assignmentProposalRepository.findAllByUserId(userId);

        return assList.stream().filter(e -> e.getStatus().equals(ProposalStatus.WAITING)).toList();
    }

    @Override
    public List<AssignmentProposal> getAllAssignmentsByDepartManager() {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if(hasDepartmentManagerRole){
            List<AssignmentProposal> assignmentProposalList = new ArrayList<>();
            var depart = departmentService.getDepartmentByManager(connectedUser.getId());
            for(var user: depart.getUsers()){
                assignmentProposalList.addAll(getAllAssignmentsByUserID(user.getId()));
            }
            assignmentProposalList.addAll(getAllAssignmentsByUserID(depart.getDepartmentManager()));
            return assignmentProposalList;
        }else {
            throw new AccessDeniedException("Unauthorized access!");
        }

    }

    @Override
    public List<AssignmentProposal> getWaitingAssignmentsByProjectId(UUID projectId) {
        var listAss = getAllAssignmentsByProjectID(projectId);
        return listAss.stream().filter(e -> e.getStatus().equals(ProposalStatus.WAITING)).toList();
    }

    @Override
    public List<AssignmentProposal> getAllAssignmentsByProjectID(UUID projectId) {
        return assignmentProposalRepository.findAllByProjectId(projectId);
    }

    @Override
    public List<AssignmentProposal> getAllAssignmentsByProposalStatus(ProposalStatus proposalStatus) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if(hasProjectManagerRole){
            return assignmentProposalRepository.getAllByStatusAndOrganizationId(proposalStatus, connectedUser.getOrganizationId());
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }

    }

    @Override
    public AssignmentProposal updateAssignmentById(UUID assignmentId, AssignmentProposalDto assignmentProposalDto) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if(hasProjectManagerRole){
            AssignmentProposal assignmentProposalToUpdate = getAssignmentById(assignmentId);

            if(assignmentProposalToUpdate.getUserId().equals(assignmentProposalDto.getUserId())){

                UserProjectRoleUpdateDto userProjectRoleUpdateDto = UserProjectRoleUpdateDto.builder()
                        .teamRoles(teamRoleService.getListOfTeamRoleByIds(assignmentProposalDto.getTeamRoleIds()))
                        .statusOfMember(StatusOfMember.PROPOSED)
                        .build();
                userProjectRoleService.updateUserProjectRoleById(userProjectRoleService
                        .getUserProjectRoleByProjectIdANDUserId(assignmentProposalDto.getProjectId(),assignmentProposalDto.getUserId())
                        .getId(),userProjectRoleUpdateDto);
            } else {
                userProjectRoleService.deleteUserProjectRoleById(userProjectRoleService
                        .getUserProjectRoleByProjectIdANDUserId(assignmentProposalDto.getProjectId(),assignmentProposalDto.getUserId())
                        .getId());

                UserProjectRoleDto userProjectRoleDto = UserProjectRoleDto.builder()
                        .userId(assignmentProposalDto.getUserId())
                        .projectId(assignmentProposalDto.getProjectId())
                        .teamRolesIds(assignmentProposalDto.getTeamRoleIds())
                        .workHours(assignmentProposalDto.getWorkHours())
                        .build();
                userProjectRoleService.createUserProjectRole(userProjectRoleDto);
            }

            assignmentProposalToUpdate.setWorkHours(assignmentProposalDto.getWorkHours());
            assignmentProposalToUpdate.setComments(assignmentProposalDto.getComments());
            assignmentProposalToUpdate.setUserId(assignmentProposalDto.getUserId());
            assignmentProposalToUpdate.setProjectId(assignmentProposalDto.getProjectId());
            assignmentProposalToUpdate.setTeamRoles(teamRoleService.getListOfTeamRoleByIds(assignmentProposalDto.getTeamRoleIds()));

            return assignmentProposalRepository.save(assignmentProposalToUpdate);

        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }

    @Override
    public AssignmentProposal updateAssignmentStatusById(UUID assignmentId, ProposalStatus proposalStatus) {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if(hasDepartManagerRole){

            AssignmentProposal assignmentProposal = getAssignmentById(assignmentId);

            if(proposalStatus.equals(ProposalStatus.ACCEPTED)){

                try{
                    userService.removeAvailableHoursByUserId(assignmentProposal.getUserId(),assignmentProposal.getWorkHours());
                } catch (RuntimeException ex){
                    var userProjectRole = userProjectRoleService.getUserProjectRoleByProjectIdANDUserId(assignmentProposal.getProjectId(),
                            assignmentProposal.getUserId());
                    userProjectRoleService.deleteUserProjectRoleById(userProjectRole.getId());
                    assignmentProposal.setStatus(ProposalStatus.REJECTED);
                    assignmentProposalRepository.save(assignmentProposal);

                    String newMessage = ex.getMessage() + " The Allocation Proposal Status is now REJECTED by default. Change work hours..";
                    throw new RuntimeException(newMessage);
                }


                var userProjectRole = userProjectRoleService.getUserProjectRoleByProjectIdANDUserId(assignmentProposal.getProjectId(),
                        assignmentProposal.getUserId());

                userProjectRoleService.updateUserProjectRoleStatusById(userProjectRole.getId(),StatusOfMember.ACTIVE);

//else if (assignmentProposal.getStatus().equals(ProposalStatus.REJECTED))
            } else if (proposalStatus.equals(ProposalStatus.REJECTED)) {
                var userProjectRole = userProjectRoleService.getUserProjectRoleByProjectIdANDUserId(assignmentProposal.getProjectId(),
                        assignmentProposal.getUserId());
                userProjectRoleService.deleteUserProjectRoleById(userProjectRole.getId());
            }
            assignmentProposal.setStatus(proposalStatus);
            return assignmentProposalRepository.save(assignmentProposal);

        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }

    @Override
    public void deleteAssignmentById(UUID assignmentId) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if(hasProjectManagerRole){
            AssignmentProposal assignmentProposalToDelete = getAssignmentById(assignmentId);

            if(assignmentProposalToDelete.getStatus().equals(ProposalStatus.WAITING)){
                var userProjectRole =  userProjectRoleService.getUserProjectRoleByProjectIdANDUserId(
                        assignmentProposalToDelete.getProjectId(), assignmentProposalToDelete.getUserId()
                );
                userProjectRoleService.deleteUserProjectRoleById(userProjectRole.getId());
            }

            assignmentProposalRepository.deleteById(assignmentId);
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }
}
