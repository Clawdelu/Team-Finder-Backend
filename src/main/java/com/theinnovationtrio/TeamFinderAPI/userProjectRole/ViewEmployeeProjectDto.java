package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.project.Project;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewEmployeeProjectDto {

    private List<Project> currentProjects;
    private List<Project> pastProjects;
}
