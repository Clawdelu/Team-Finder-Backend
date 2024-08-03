package com.theinnovationtrio.TeamFinderAPI.projectTeamRole;

import java.util.List;
import java.util.UUID;

public interface IProjectTeamRoleService {

    Project_TeamRole createProject_TeamRole(ProjectTeamRoleDto projectTeamRoleDto, UUID projectId);

    List<Project_TeamRole> createListOfProjectTeamRole(List<ProjectTeamRoleDto> projectTeamRoleDtoList, UUID projectId);

    Project_TeamRole getProject_TeamRoleById(UUID projectTeamRoleId);

    List<Project_TeamRole> getAllProject_TeamRole();

    Project_TeamRole updateProject_TeamRoleById(ProjectTeamRoleUpdateDto projectTeamRoleUpdateDto);

    void deleteProject_TeamRoleById(UUID projectTeamRoleId);

    void deleteAllProject_TeamRoles(List<Project_TeamRole> projectTeamRoleList);
}
