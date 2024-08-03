package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectRoleDto {

    @NotNull(message = "This field is null.")
    private UUID userId;

    @NotNull(message = "This field is null.")
    private UUID projectId;

    @NotNull(message = "This field is null.")
    private List<UUID> teamRolesIds;

    @NotNull(message = "This field is null.")
    private int workHours;


}
