package com.theinnovationtrio.TeamFinderAPI.deallocationProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeallocationProposalDto {

    @NotNull(message = "This field is null.")
    private String deallocationReason;

    @NotNull(message = "This field is null.")
    private UUID userId;

    @NotNull(message = "This field is null.")
    private UUID projectId;

}
