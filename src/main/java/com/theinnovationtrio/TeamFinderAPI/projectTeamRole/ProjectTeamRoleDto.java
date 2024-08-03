package com.theinnovationtrio.TeamFinderAPI.projectTeamRole;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamRoleDto {

    @NotNull(message = "This field is null.")
    private UUID teamRoleId;

    @NotNull(message = "This field is null.")
    private int noOfMembers;
}
