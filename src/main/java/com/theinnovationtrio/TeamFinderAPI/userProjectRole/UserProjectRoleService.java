package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
import com.theinnovationtrio.TeamFinderAPI.teamRole.ITeamRoleService;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import com.theinnovationtrio.TeamFinderAPI.user.UserDto;
import com.theinnovationtrio.TeamFinderAPI.user.UserMapper;
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
public class UserProjectRoleService implements IUserProjectRoleService {

    private final UserProjectRoleRepository userProjectRoleRepository;
    private final ITeamRoleService teamRoleService;
    private final IUserService userService;
    private final UserMapper userMapper;

    @Override
    public User_Project_Role createUserProjectRole(UserProjectRoleDto userProjectRoleDto) {

        User_Project_Role user_project_role = User_Project_Role.builder()
                .id(UUID.randomUUID())
                .userId(userProjectRoleDto.getUserId())
                .projectId(userProjectRoleDto.getProjectId())
                .teamRoles(teamRoleService.getListOfTeamRoleByIds(userProjectRoleDto.getTeamRolesIds()))
                .statusOfMember(StatusOfMember.PROPOSED)
                .workHours(userProjectRoleDto.getWorkHours())
                .build();

        return userProjectRoleRepository.save(user_project_role);
    }

    @Override
    public User_Project_Role getUserProjectRoleById(UUID userProjectRoleId) {
        return userProjectRoleRepository.findById(userProjectRoleId)
                .orElseThrow(() -> new EntityNotFoundException("User from project not found!"));
    }

    @Override
    public List<User_Project_Role> getAllUserProjectRoles() {
        return userProjectRoleRepository.findAll();
    }

    @Override
    public List<User_Project_Role> getAllUserProjectRolesByProjectId(UUID projectId) {
        return userProjectRoleRepository.findAllByProjectId(projectId);
    }


    @Override
    public User_Project_Role getUserProjectRoleByProjectIdANDUserId(UUID projectId, UUID userId) {
        return userProjectRoleRepository.findByProjectIdAndUserId(projectId, userId);
    }

    @Override
    public List<UUID> getUserIdsFromProjects(List<UUID> projectIds) {
        List<UUID> userIds = new ArrayList<>();

        for (UUID projectId : projectIds) {
            var list = getAllUserProjectRolesByProjectId(projectId);
            for (User_Project_Role user_project_role : list) {
                userIds.add(user_project_role.getUserId());
            }
        }

        return userIds;
    }

    @Override
    public List<User_Project_Role> getAllUserProjectRolesByUserId(UUID userId) {

        return userProjectRoleRepository.getAllByUserId(userId);
    }

    @Override
    public User_Project_Role updateUserProjectRoleById(UUID userProjectRoleId, UserProjectRoleUpdateDto updateUserDto) {

        User connectedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProjectManagerRole = connectedUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Project_Manager));

        if (hasProjectManagerRole) {

            var userProjectRole = getUserProjectRoleById(userProjectRoleId);
            userProjectRole.setTeamRoles(updateUserDto.getTeamRoles());
            userProjectRole.setStatusOfMember(updateUserDto.getStatusOfMember());
            userProjectRole.setWorkHours(updateUserDto.getWorkHours());
            return userProjectRoleRepository.save(userProjectRole);

        } else {
            throw new AccessDeniedException("Unauthorized access! You don't have the project manager role...");
        }

    }

    @Override
    public User_Project_Role updateUserProjectRoleStatusById(UUID userProjectRoleId, StatusOfMember statusOfMember) {
        var userProjectRole = getUserProjectRoleById(userProjectRoleId);
        userProjectRole.setStatusOfMember(statusOfMember);
        return userProjectRoleRepository.save(userProjectRole);
    }

    @Override
    public void deleteListOfUserProjectRoleByIds(List<UUID> userProjectRoleIdList) {
        userProjectRoleIdList.forEach(this::deleteUserProjectRoleById);
    }

    @Override
    public void deleteUserProjectRoleById(UUID userProjectRoleId) {
        userProjectRoleRepository.deleteById(userProjectRoleId);
    }

    @Override
    public ViewProjectTeam getUsersByProjectId(UUID projectId) {

        List<User> proposedMembers = new ArrayList<>();
        List<User> activeMembers = new ArrayList<>();
        List<User> pastMembers = new ArrayList<>();

        var userProjectRoleList = getAllUserProjectRolesByProjectId(projectId);
        for (var userProjRole : userProjectRoleList) {
            if(userProjRole.getStatusOfMember().equals(StatusOfMember.PROPOSED))
                proposedMembers.add(userService.getUserById(userProjRole.getUserId()));
            else if (userProjRole.getStatusOfMember().equals(StatusOfMember.ACTIVE))
                activeMembers.add(userService.getUserById(userProjRole.getUserId()));
            else if (userProjRole.getStatusOfMember().equals(StatusOfMember.PAST))
                pastMembers.add(userService.getUserById(userProjRole.getUserId()));
        }
        return ViewProjectTeam.builder()
                .proposedMembers(userMapper.INSTANCE.mapToUserDto(proposedMembers))
                .activeMembers(userMapper.INSTANCE.mapToUserDto(activeMembers))
                .pastMembers(userMapper.INSTANCE.mapToUserDto(pastMembers))
                .build();
    }


}
