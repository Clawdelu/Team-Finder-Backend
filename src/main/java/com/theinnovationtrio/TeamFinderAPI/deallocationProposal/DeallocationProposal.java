package com.theinnovationtrio.TeamFinderAPI.deallocationProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeallocationProposal {
    @Id
    private UUID id;
    private String deallocationReason;
    private UUID userId;
    private UUID projectId;
    private ProposalStatus status;

}
