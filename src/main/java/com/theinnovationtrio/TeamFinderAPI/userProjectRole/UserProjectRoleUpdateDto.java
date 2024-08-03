package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
import com.theinnovationtrio.TeamFinderAPI.teamRole.TeamRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectRoleUpdateDto {

    private List<TeamRole> teamRoles;
    private StatusOfMember statusOfMember;
    private int workHours;
}
