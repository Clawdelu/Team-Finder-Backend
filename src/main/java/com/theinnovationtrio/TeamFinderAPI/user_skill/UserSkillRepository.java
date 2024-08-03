package com.theinnovationtrio.TeamFinderAPI.user_skill;

import com.theinnovationtrio.TeamFinderAPI.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserSkillRepository extends JpaRepository<User_Skill, UUID> {

    List<User_Skill> getAllBySkill(Skill skill);
}
