package com.theinnovationtrio.TeamFinderAPI.assignmentProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;

import java.util.List;
import java.util.UUID;

public interface IAssignmentProposalService {

    AssignmentProposal createAssignment(AssignmentProposalDto assignmentProposalDto);

    AssignmentProposal getAssignmentById(UUID assignmentId);

    List<AssignmentProposal> getAllAssignments();

    List<AssignmentProposal> getAllAssignmentsByUserID(UUID userId);

    List<AssignmentProposal> getAllAssignmentsByDepartManager();

    List<AssignmentProposal> getWaitingAssignmentsByProjectId(UUID projectId);

    List<AssignmentProposal> getAllAssignmentsByProjectID(UUID projectId);

    List<AssignmentProposal> getAllAssignmentsByProposalStatus(ProposalStatus proposalStatus);

    AssignmentProposal updateAssignmentById(UUID assignmentId, AssignmentProposalDto assignmentProposalDto);

    AssignmentProposal updateAssignmentStatusById(UUID assignmentId, ProposalStatus proposalStatus);

    void deleteAssignmentById(UUID assignmentId);
}
