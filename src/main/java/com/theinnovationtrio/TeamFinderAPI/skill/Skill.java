package com.theinnovationtrio.TeamFinderAPI.skill;

import com.theinnovationtrio.TeamFinderAPI.skillCategory.SkillCategory;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Skill {

    @Id
    private UUID id;

    private UUID createdBy;

    private String skillName;

    @ManyToOne
    private SkillCategory skillCategory;

    private String description;

    @ManyToMany
    private List<Department> departments;
}
