package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.department.IDepartmentService;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
import com.theinnovationtrio.TeamFinderAPI.projectTeamRole.IProjectTeamRoleService;
import com.theinnovationtrio.TeamFinderAPI.technologyStack.ITechnologyStackService;
import com.theinnovationtrio.TeamFinderAPI.user.*;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.IUserProjectRoleService;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.ViewEmployeeProjectDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final ITechnologyStackService technologyStackService;
    private final IProjectTeamRoleService projectTeamRoleService;
    private final IUserService userService;
    private final IUserProjectRoleService userProjectRoleService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final IDepartmentService departmentService;

    @Override
    public Project createProject(ProjectDto projectDto) {

        User projectUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjManagerRole = projectUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));
        if (hasProjManagerRole) {
            Set<ProjectStatus> allowedStatuses = EnumSet.of(ProjectStatus.NOT_STARTED, ProjectStatus.STARTING);

            Project project = Project.builder()
                    .id(UUID.randomUUID())
                    .createdBy(projectUser.getId())
                    .projectName(projectDto.getProjectName())
                    .projectPeriod(projectDto.getProjectPeriod())
                    .startDate(projectDto.getStartDate())
                    .generalDescription(projectDto.getGeneralDescription())
                    .deadlineDate(projectDto.getProjectPeriod().equals(ProjectPeriod.FIXED)
                            ? projectDto.getDeadlineDate() : null)
                    .build();

            if (allowedStatuses.contains(projectDto.getProjectStatus())) {
                project.setProjectStatus(projectDto.getProjectStatus());
            } else {
                throw new RuntimeException("The project status can be 'Not Started' or 'Starting'");
            }

            project.setTechnologyStack(technologyStackService.createListOfTechnologyStacks(projectDto.getTechnologyStack()));

            project.setTeamRoles(projectTeamRoleService.createListOfProjectTeamRole(projectDto.getTeamRoles(), project.getId()));

            return projectRepository.save(project);
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }

    @Override
    public Project getProjectById(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getAllSameAuthorProjects() {

        User projectUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjManagerRole = projectUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));
        if (hasProjManagerRole) {

            return projectRepository.findAllByCreatedBy(projectUser.getId());

        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }
    }


    @Override
    public List<Project> getAllProjectsByProjectPeriod(ProjectPeriod projectPeriod) {
        User projectUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjManagerRole = projectUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if (hasProjManagerRole) {

            return projectRepository.findAllByProjectPeriodAndCreatedBy(projectPeriod, projectUser.getId());
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }

    }

    @Override
    public List<Project> getAllProjectsByProjectStatus(ProjectStatus projectStatus) {
        User projectUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjManagerRole = projectUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if (hasProjManagerRole) {

            return projectRepository.findAllByProjectStatusAndCreatedBy(projectStatus, projectUser.getId());
        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }

    }

    @Override
    public List<UserDto> getAllUsersFromProjectCloseToFinish(int weeks, UUID projectId) {

        User projectManager = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = projectManager.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));
        var project = getProjectById(projectId);
        if (hasProjectManagerRole) {

            if (weeks > 6)
                throw new RuntimeException("The maximum deadline offset is 6 weeks, please change it and try again.");
            else if (weeks < 2)
                throw new RuntimeException("The minimum deadline offset is 2 weeks, please change it and try again.");

            List<UUID> projectIds = getAllProjectIDsByDeadLineInNWeeks(weeks);
            List<UUID> userIds =
                    userProjectRoleService.getUserIdsFromProjects(projectIds);
            List<User> userList = new ArrayList<>();
            for (UUID userId : userIds) {
                userList.add(userService.getUserById(userId));
            }
            return userService.checkMatchingSkills(project.getTechnologyStack(), userMapper.INSTANCE.mapToUserDto(userList));
        } else {
            throw new AccessDeniedException("Unauthorized access! Not have the project manager role.");
        }
    }

    @Override
    public List<UUID> getAllProjectIDsByDeadLineInNWeeks(int weeks) {

        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusWeeks(weeks);
        return projectRepository.findAllByDeadlineDateInNextWeeks(endDate);
    }

    @Override
    public Project updateProject(UUID projectId, ProjectDto projectDto) {

        User projectUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project projectToUpdate = getProjectById(projectId);
        boolean createdTheProject = projectUser.getId().equals(projectToUpdate.getCreatedBy());
        if (createdTheProject) {
            projectToUpdate.setProjectName(projectDto.getProjectName());
            projectToUpdate.setProjectPeriod(projectDto.getProjectPeriod());
            projectToUpdate.setStartDate(projectDto.getStartDate());
            projectToUpdate.setDeadlineDate(projectDto.getDeadlineDate());
            projectToUpdate.setProjectStatus(projectDto.getProjectStatus());
            projectToUpdate.setGeneralDescription(projectDto.getGeneralDescription());

            var technologyStackToDelete = projectToUpdate.getTechnologyStack();
            projectToUpdate.setTechnologyStack(technologyStackService.createListOfTechnologyStacks(projectDto.getTechnologyStack()));
            technologyStackService.deleteListOfTechnologyStacks(technologyStackToDelete);

            var teamRolesProjectToDelete = projectToUpdate.getTeamRoles();
            projectToUpdate.setTeamRoles(projectTeamRoleService.createListOfProjectTeamRole(projectDto.getTeamRoles(), projectToUpdate.getId()));
            projectTeamRoleService.deleteAllProject_TeamRoles(teamRolesProjectToDelete);

            return projectRepository.save(projectToUpdate);
        } else {
            throw new AccessDeniedException("Unauthorized access! You aren't the author.");
        }
    }

    @Override
    public void deleteProjectById(UUID projectId) {

        Project projectToDelete = getProjectById(projectId);

        Set<ProjectStatus> allowedStatuses = EnumSet.of(ProjectStatus.IN_PROGRESS, ProjectStatus.CLOSING, ProjectStatus.CLOSED);

        if (allowedStatuses.contains(projectToDelete.getProjectStatus())) {
            throw new RuntimeException("The project can't be deleted, because the project had the status " + projectToDelete.getProjectStatus());

        } else {

            var technologyStackToDelete = projectToDelete.getTechnologyStack();

            var teamRolesProjectToDelete = projectToDelete.getTeamRoles();

            projectRepository.deleteById(projectId);

            technologyStackService.deleteListOfTechnologyStacks(technologyStackToDelete);

            projectTeamRoleService.deleteAllProject_TeamRoles(teamRolesProjectToDelete);
        }

    }

    @Override
    public ViewEmployeeProjectDto getProjectsByUserId(UUID userId) {
        List<Project> currentProjects = new ArrayList<>();
        List<Project> pastProjects = new ArrayList<>();

        var userProjRoleList = userProjectRoleService.getAllUserProjectRolesByUserId(userId);

        for (var userProj : userProjRoleList) {
            if (userProj.getStatusOfMember().equals(StatusOfMember.PAST)) {
                pastProjects.add(getProjectById(userProj.getProjectId()));
            } else if (userProj.getStatusOfMember().equals(StatusOfMember.ACTIVE)) {

                var project = getProjectById(userProj.getProjectId());

                if (project.getProjectStatus().equals(ProjectStatus.CLOSED)) {
                    pastProjects.add(project);
                } else {
                    currentProjects.add(project);
                }
            }
        }

        return ViewEmployeeProjectDto.builder()
                .currentProjects(currentProjects)
                .pastProjects(pastProjects)
                .build();
    }

    @Override
    public List<ProjectMembersDto> getProjectsForDepartmentManagerByUserIds() {
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Project> projects = new ArrayList<>();
        List<ProjectMembersDto> projMem = new ArrayList<>();

        // List<UserDto> userDtoList = new ArrayList<>();
        var depart = departmentService.getDepartmentByManager(connectedUser.getId());
        for (var user : depart.getUsers()) {

            var userProjRoleList = userProjectRoleService.getAllUserProjectRolesByUserId(user.getId());
            userProjRoleList.removeIf(u -> u.getStatusOfMember().equals(StatusOfMember.PROPOSED));
            for (var userProjRole : userProjRoleList) {
                var projectToAdd = getProjectById(userProjRole.getProjectId());
                if (!projects.contains(projectToAdd)) {
                    var userProjectRoleList = userProjectRoleService.getAllUserProjectRolesByProjectId(projectToAdd.getId());
                    List<User> userProjList = new ArrayList<>();
                    for (var ob : userProjectRoleList) {
                        userProjList.add(userService.getUserById(ob.getUserId()));
                    }

                    projects.add(projectToAdd);

                    projMem.add(ProjectMembersDto.builder()
                            .project(projectToAdd)
                            .members(userMapper.INSTANCE.mapToUserDto(userProjList))
                            .build());
                }


                //userDtoList.add(userMapper.INSTANCE.mapToUserDto(user));

            }
        }

        return projMem;
        // return null;
    }

    @Override
    public List<UserDto> getPartiallyAvailableUsers(UUID projectId) {

        List<Integer> availableHours = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));
        var project = getProjectById(projectId);

        if (hasProjectManagerRole) {
            List<User> usersPartiallyAvailable = availableHours.stream()
                    .flatMap(hour -> userRepository.findAllByAvailableHoursAndOrganizationId(hour, connectedUser.getOrganizationId()).stream())
                    .toList();

            var filteredList = usersPartiallyAvailable.stream().filter(user -> user.getId().equals(connectedUser.getId())).toList();
            return userService.checkMatchingSkills(project.getTechnologyStack(), userMapper.INSTANCE.mapToUserDto(filteredList));

        } else {
            throw new AccessDeniedException("Unauthorized access! Not have the project manager role.");
        }
    }

    @Override
    public List<UserDto> getUnavailableUsers(UUID projectId) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));
        var project = getProjectById(projectId);

        if (hasProjectManagerRole) {

            List<User> unavailableUsers = userRepository.findAllByAvailableHoursAndOrganizationId(0, connectedUser.getOrganizationId());

            return userService.checkMatchingSkills(project.getTechnologyStack(), userMapper.INSTANCE.mapToUserDto(unavailableUsers));

        } else {
            throw new AccessDeniedException("Unauthorized access! Not have the project manager role.");
        }

    }

    @Override
    public List<UserDto> getFullyAvailableUsers(UUID projectId) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));
        var project = getProjectById(projectId);
        if (hasProjectManagerRole) {

            List<User> fullyAvailableUsers = userRepository.findAllByAvailableHoursAndOrganizationId(8, connectedUser.getOrganizationId());
            return userService.checkMatchingSkills(project.getTechnologyStack(), userMapper.INSTANCE.mapToUserDto(fullyAvailableUsers));

        } else {
            throw new AccessDeniedException("Unauthorized access! Not have the project manager role.");
        }
    }
}
