package com.theinnovationtrio.TeamFinderAPI.user_skill;

import com.theinnovationtrio.TeamFinderAPI.skill.Skill;

import java.util.List;
import java.util.UUID;

public interface IUserSkillService {

    User_Skill createUserSkill(UserSkillDto userSkillDto, UUID skillId);

    User_Skill getUserSkillById(UUID userSkillId);

    List<User_Skill> getAllUsersSkills();

    User_Skill updateUserSkill(UUID userSkillId, UserSkillDto userSkillDto);

    List<User_Skill> getAllUserSkillsBySkill(Skill skill);

    void deleteUserSkillById(UUID userSkillId);

}
