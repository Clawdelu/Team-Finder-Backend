package com.theinnovationtrio.TeamFinderAPI.deallocationProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DeallocationProposalRepository extends JpaRepository<DeallocationProposal, UUID> {

    @Query("select d from DeallocationProposal d " +
            "join User u on u.id = d.userId " +
            "where u.organizationId = :organizationId and d.status = :proposalStatus")
    List<DeallocationProposal> getAllByStatusAndOrganizationId(ProposalStatus proposalStatus, UUID organizationId);

    List<DeallocationProposal> findAllByProjectId(UUID projectId);

    List<DeallocationProposal> findAllByUserId(UUID userId);
}
