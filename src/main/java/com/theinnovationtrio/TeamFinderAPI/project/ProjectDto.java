package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import com.theinnovationtrio.TeamFinderAPI.projectTeamRole.ProjectTeamRoleDto;
import com.theinnovationtrio.TeamFinderAPI.technologyStack.TechnologyStackDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    @NotNull(message = "This field is null.")
    private String projectName;

    @NotNull(message = "This field is null.")
    private ProjectPeriod projectPeriod;

    @NotNull(message = "This field is null.")
    private LocalDate startDate;

    private LocalDate deadlineDate;

    @NotNull(message = "This field is null.")
    private ProjectStatus projectStatus;

    @NotNull(message = "This field is null.")
    private String generalDescription;

    @NotNull(message = "This field is null.")
    private List<TechnologyStackDto> technologyStack;

    @NotNull(message = "This field is null.")
    private List<ProjectTeamRoleDto> teamRoles;
}
