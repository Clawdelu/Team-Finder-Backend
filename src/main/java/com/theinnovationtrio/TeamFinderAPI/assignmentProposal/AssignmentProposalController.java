package com.theinnovationtrio.TeamFinderAPI.assignmentProposal;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignment-proposal")
public class AssignmentProposalController {

    private final IAssignmentProposalService assignmentProposalService;

    @PostMapping
    public ResponseEntity<?> createAssignmentProposal(@Valid @RequestBody AssignmentProposalDto assignmentProposalDto) {

        try {
            AssignmentProposal assignmentProposal = assignmentProposalService.createAssignment(assignmentProposalDto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(assignmentProposal.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("{assignmentProposalId}")
    public ResponseEntity<?> getAssignmentProposalById(@PathVariable UUID assignmentProposalId) {
        try {

            return ResponseEntity.ok(assignmentProposalService.getAssignmentById(assignmentProposalId));
        } catch (EntityNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }
    @GetMapping("/department-manager")
    public ResponseEntity<?> getAssignmentProposalByDepartManager() {
        try {

            return ResponseEntity.ok(assignmentProposalService.getAllAssignmentsByDepartManager());
        } catch (EntityNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }
    @GetMapping("/byuserid/{userId}")
    public ResponseEntity<?> getAssignmentProposalByuserId(@PathVariable UUID userId) {
        try {

            return ResponseEntity.ok(assignmentProposalService.getAllAssignmentsByUserID(userId));
        } catch (EntityNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAssignmentProposals() {
        List<AssignmentProposal> assignmentProposalList = assignmentProposalService.getAllAssignments();
        if (assignmentProposalList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.ok(assignmentProposalList);
    }
    @GetMapping("/byprojectid/{projectId}")
    public ResponseEntity<?> getAllAssignmentProposalsByProjectId(@PathVariable UUID projectId) {
        List<AssignmentProposal> assignmentProposalList = assignmentProposalService.getWaitingAssignmentsByProjectId(projectId);
        if (assignmentProposalList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.ok(assignmentProposalList);
    }

    @GetMapping("filter-by-proposal-status")
    public ResponseEntity<?> getAllAssignmentProposalsByProposalStatus(@RequestParam ProposalStatus proposalStatus) {
        try {
            List<AssignmentProposal> assignmentProposalList = assignmentProposalService.getAllAssignmentsByProposalStatus(proposalStatus);
            if (assignmentProposalList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else
                return ResponseEntity.ok(assignmentProposalList);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{assignmentProposalId}")
    public ResponseEntity<?> updateAssignmentProposal(@PathVariable UUID assignmentProposalId, @Valid @RequestBody AssignmentProposalDto assignmentProposalDto) {

        try {
            return ResponseEntity.ok(assignmentProposalService.updateAssignmentById(assignmentProposalId, assignmentProposalDto));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("/{assignmentProposalId}/status")
    public ResponseEntity<?> updateAssignmentStatus(@PathVariable UUID assignmentProposalId, @RequestParam ProposalStatus proposalStatus) {

        try {
            return ResponseEntity.ok(assignmentProposalService.updateAssignmentStatusById(assignmentProposalId,proposalStatus));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{assignmentProposalId}")
    public ResponseEntity<?> deleteAssignmentProposalById(@PathVariable UUID assignmentProposalId) {
        try {
            assignmentProposalService.deleteAssignmentById(assignmentProposalId);
            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

}
