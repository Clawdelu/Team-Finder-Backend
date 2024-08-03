package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.enums.Experience;
import com.theinnovationtrio.TeamFinderAPI.enums.Level;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillsDto {

    private String skillName;
    private  String skillCategory;
    private String skillDescription;
    private Experience skillExperience;
    private Level skillLevel;
}
