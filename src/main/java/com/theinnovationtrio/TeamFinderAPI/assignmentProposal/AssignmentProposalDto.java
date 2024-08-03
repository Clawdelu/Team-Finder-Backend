package com.theinnovationtrio.TeamFinderAPI.assignmentProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import com.theinnovationtrio.TeamFinderAPI.teamRole.TeamRole;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentProposalDto {

    @NotNull(message = "This field is null.")
    private int workHours;

    @NotNull(message = "This field is null.")
    private List<UUID> teamRoleIds;

    @NotNull(message = "This field is null.")
    private String comments;

    @NotNull(message = "This field is null.")
    private UUID userId;

    @NotNull(message = "This field is null.")
    private UUID projectId;
}
