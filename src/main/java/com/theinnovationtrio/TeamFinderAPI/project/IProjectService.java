package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import com.theinnovationtrio.TeamFinderAPI.user.UserDto;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.ViewDepartmentProjectDto;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.ViewEmployeeProjectDto;

import java.util.List;
import java.util.UUID;

public interface IProjectService {

    Project createProject(ProjectDto projectDto);

    Project getProjectById(UUID projectId);

    List<Project> getAllProjects();

    List<Project> getAllSameAuthorProjects();

    List<Project> getAllProjectsByProjectPeriod(ProjectPeriod projectPeriod);

    List<Project> getAllProjectsByProjectStatus(ProjectStatus projectStatus);
    List<UserDto> getAllUsersFromProjectCloseToFinish(int weeks, UUID projectId);

    List<UUID> getAllProjectIDsByDeadLineInNWeeks(int weeks);

    Project updateProject(UUID projectId, ProjectDto projectDto);

    void deleteProjectById(UUID projectId);

    ViewEmployeeProjectDto getProjectsByUserId(UUID userId);

    List<ProjectMembersDto> getProjectsForDepartmentManagerByUserIds();

    List<UserDto> getPartiallyAvailableUsers(UUID projectId);

    List<UserDto> getUnavailableUsers(UUID projectId);

    List<UserDto> getFullyAvailableUsers(UUID projectId);

}
