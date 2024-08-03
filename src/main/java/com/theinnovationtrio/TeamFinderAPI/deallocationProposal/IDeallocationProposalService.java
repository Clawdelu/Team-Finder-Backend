package com.theinnovationtrio.TeamFinderAPI.deallocationProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;

import java.util.List;
import java.util.UUID;

public interface IDeallocationProposalService {

    DeallocationProposal createDeallocation(DeallocationProposalDto deallocationProposalDto);

    DeallocationProposal getDeallocationById(UUID deallocationId);

    List<DeallocationProposal> getAllDealocations();

    List<DeallocationProposal> getWaitingDeallocationsByProjectId(UUID projectId);

    List<DeallocationProposal> getAllDeallocationsByUserId(UUID userId);

    List<DeallocationProposal> getAllDeallocationsByDepartManager();

    List<DeallocationProposal> getAllDeallocationsByProposalStatus(ProposalStatus proposalStatus);

    DeallocationProposal updateDeallocationById(UUID deallocationId,DeallocationProposalDto deallocationProposalDto);

    DeallocationProposal updateDeallocationProposalStatusById(UUID deallocationId, ProposalStatus proposalStatus);

    void deleteDeallocationById(UUID deallocationId);
}
