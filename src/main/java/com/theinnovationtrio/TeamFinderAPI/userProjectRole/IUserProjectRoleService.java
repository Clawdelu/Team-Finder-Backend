package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;

import java.util.List;
import java.util.UUID;

public interface IUserProjectRoleService {

    User_Project_Role createUserProjectRole(UserProjectRoleDto userProjectRoleDto);

    User_Project_Role getUserProjectRoleById(UUID userProjectRoleId);

    List<User_Project_Role> getAllUserProjectRoles();

    List<User_Project_Role> getAllUserProjectRolesByProjectId(UUID projectId);

    User_Project_Role getUserProjectRoleByProjectIdANDUserId(UUID projectId, UUID userId);

    List<UUID> getUserIdsFromProjects(List<UUID> projectIds);

    List<User_Project_Role> getAllUserProjectRolesByUserId(UUID userId);

    User_Project_Role updateUserProjectRoleById(UUID userProjectRoleId, UserProjectRoleUpdateDto userProjectRoleDto);

    User_Project_Role updateUserProjectRoleStatusById(UUID userProjectRoleId, StatusOfMember statusOfMember);

    void deleteListOfUserProjectRoleByIds(List<UUID> userProjectRoleIdList);

    void deleteUserProjectRoleById(UUID userProjectRoleId);

    ViewProjectTeam getUsersByProjectId(UUID projectId);

}
