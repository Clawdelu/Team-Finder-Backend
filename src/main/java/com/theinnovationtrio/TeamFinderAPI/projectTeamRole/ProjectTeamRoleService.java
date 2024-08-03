package com.theinnovationtrio.TeamFinderAPI.projectTeamRole;

import com.theinnovationtrio.TeamFinderAPI.teamRole.ITeamRoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectTeamRoleService implements IProjectTeamRoleService {

    private final ProjectTeamRoleRepository projectTeamRoleRepository;
    private final ITeamRoleService teamRoleService;

    @Override
    public Project_TeamRole createProject_TeamRole(ProjectTeamRoleDto projectTeamRoleDto, UUID projectId) {

        Project_TeamRole projectTeamRole = Project_TeamRole.builder()
                .id(UUID.randomUUID())
                .noOfMembers(projectTeamRoleDto.getNoOfMembers())
                .projectId(projectId)
                .teamRole(teamRoleService.getTeamRoleById(projectTeamRoleDto.getTeamRoleId()))
                .build();
        return projectTeamRoleRepository.save(projectTeamRole);
    }

    @Override
    public List<Project_TeamRole> createListOfProjectTeamRole(List<ProjectTeamRoleDto> projectTeamRoleDtoList, UUID projectId) {

        List<Project_TeamRole> projectTeamRoles = new ArrayList<>();

        projectTeamRoleDtoList.forEach(projectTeamRoleDto -> projectTeamRoles.add(createProject_TeamRole(projectTeamRoleDto,projectId)));

        return projectTeamRoles;
    }

    @Override
    public Project_TeamRole getProject_TeamRoleById(UUID projectTeamRoleId) {

        return projectTeamRoleRepository.findById(projectTeamRoleId)
                .orElseThrow(() -> new EntityNotFoundException("Technology Stack not found"));
    }

    @Override
    public List<Project_TeamRole> getAllProject_TeamRole() {

        return projectTeamRoleRepository.findAll();
    }

    @Override
    public Project_TeamRole updateProject_TeamRoleById(ProjectTeamRoleUpdateDto projectTeamRoleUpdateDto) {

        Project_TeamRole projectTeamRoleToUpdate = getProject_TeamRoleById(projectTeamRoleUpdateDto.getProjectTeamRoleId());
        projectTeamRoleToUpdate.setTeamRole(teamRoleService.getTeamRoleById(projectTeamRoleUpdateDto.getTeamRoleId()));
        projectTeamRoleToUpdate.setNoOfMembers(projectTeamRoleUpdateDto.getNoOfMembers());

        return projectTeamRoleRepository.save(projectTeamRoleToUpdate);
    }

    @Override
    public void deleteProject_TeamRoleById(UUID projectTeamRoleId) {
        //TODO : trebuie sa mergi la Project si sa stergi din lista respectiva; de asemenea cand se sterge un Project sa stergem si Proj_TeamRole
        projectTeamRoleRepository.deleteById(projectTeamRoleId);
    }

    @Override
    public void deleteAllProject_TeamRoles(List<Project_TeamRole> projectTeamRoleList) {

        projectTeamRoleList.forEach(projectTeamRole -> deleteProject_TeamRoleById(projectTeamRole.getId()));
    }
}
