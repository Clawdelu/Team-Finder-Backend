package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewDepartmentProjectDto {

    @NotNull(message = "This field is null.")
    private List<UUID> userIds;

    private ProjectStatus projectStatus;
}
