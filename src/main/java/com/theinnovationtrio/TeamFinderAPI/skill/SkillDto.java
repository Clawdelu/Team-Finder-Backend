package com.theinnovationtrio.TeamFinderAPI.skill;

import com.theinnovationtrio.TeamFinderAPI.skillCategory.SkillCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {

    @NotNull(message = "This field is null.")
    private UUID skillCategoryId;

    @NotNull(message = "This field is null.")
    private String skillName;

    private String description;

    private boolean addedToDepartment;
}
