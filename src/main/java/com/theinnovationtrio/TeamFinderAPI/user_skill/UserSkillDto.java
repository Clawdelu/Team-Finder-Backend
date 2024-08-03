package com.theinnovationtrio.TeamFinderAPI.user_skill;

import com.theinnovationtrio.TeamFinderAPI.enums.Experience;
import com.theinnovationtrio.TeamFinderAPI.enums.Level;
import com.theinnovationtrio.TeamFinderAPI.skill.Skill;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillDto {

    private Level level;

    private Experience experience;
}
