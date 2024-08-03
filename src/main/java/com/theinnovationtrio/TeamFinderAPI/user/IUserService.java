package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth.UserRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.technologyStack.TechnologyStack;
import com.theinnovationtrio.TeamFinderAPI.user_skill.UserSkillDto;
import com.theinnovationtrio.TeamFinderAPI.user_skill.User_Skill;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    User createUser(AdminRegisterRequest adminRegisterRequest);

    User createUser(UserRegisterRequest userRegisterRequest, UUID organizationId);

    User getUserById(UUID userId);

    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    List<UserDto> getAllUsers();

    List<UserDto> getOrganizationUsers();

    UserDto getConnectedUser();

    List<UserDto> getAllUnassignedUsers();

    List<UserDto> getAllFreeDepartManagers();

    void addRoleToUser(UUID userId, List<Role> roles);

    void removeUserSkillById(UUID userSkillId);

    void assignUserSkill(UUID skillId, UserSkillDto userSkillDto);

    List<User_Skill> getAllUserSkills();

    User removeDepartmentFromUser(UUID userToRemoveId);

    User addDepartmentToUser(UUID userToAssignId, Department department);

    User_Skill updateUserSkill(UUID userSkillId, UserSkillDto userSkillDto);

    void deleteUserById(UUID userId);

    void deleteSkillById(UUID skillId);







    List<UserDto> checkMatchingSkills(List<TechnologyStack> technologyStack, List<UserDto> userDtoList);

    void removeAvailableHoursByUserId(UUID userId, int workHours);

    void addAvailableHoursByUserId(UUID userId, int workHours);

    List<UserSkillsDto> getUserSkillsByUserId(UUID userId);

}
