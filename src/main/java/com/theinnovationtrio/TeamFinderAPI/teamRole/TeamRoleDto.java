package com.theinnovationtrio.TeamFinderAPI.teamRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamRoleDto {

    @NotNull(message = "This field is null.")
    private String roleInProject;
}
