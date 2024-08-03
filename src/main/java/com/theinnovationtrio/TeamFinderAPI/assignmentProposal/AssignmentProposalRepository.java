package com.theinnovationtrio.TeamFinderAPI.assignmentProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AssignmentProposalRepository extends JpaRepository<AssignmentProposal, UUID> {

    List<AssignmentProposal> findAllByUserId(UUID userId);

    List<AssignmentProposal> findAllByProjectId(UUID projectId);

    @Query("select a from AssignmentProposal a " +
            "join User u on u.id = a.userId " +
            "where u.organizationId = :organizationId and a.status = :proposalStatus")
    List<AssignmentProposal> getAllByStatusAndOrganizationId(ProposalStatus proposalStatus, UUID organizationId);
}
