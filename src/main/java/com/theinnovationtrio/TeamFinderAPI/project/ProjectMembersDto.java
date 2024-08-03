package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import com.theinnovationtrio.TeamFinderAPI.projectTeamRole.ProjectTeamRoleDto;
import com.theinnovationtrio.TeamFinderAPI.technologyStack.TechnologyStackDto;
import com.theinnovationtrio.TeamFinderAPI.user.UserDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMembersDto {

   private Project project;

    private List<UserDto> members;
}
