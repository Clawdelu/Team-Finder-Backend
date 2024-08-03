package com.theinnovationtrio.TeamFinderAPI.deallocationProposal;

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
@RequestMapping("deallocation-proposal")
public class DeallocationProposalController {

    private final IDeallocationProposalService deallocationProposalService;

    @PostMapping
    public ResponseEntity<?> createDeallocationProposal(@Valid @RequestBody DeallocationProposalDto deallocationProposalDto) {

        try {
            DeallocationProposal deallocationProposal = deallocationProposalService.createDeallocation(deallocationProposalDto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(deallocationProposal.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("{deallocationProposalId}")
    public ResponseEntity<?> getDeallocationProposalById(@PathVariable UUID deallocationProposalId) {
        try {

            return ResponseEntity.ok(deallocationProposalService.getDeallocationById(deallocationProposalId));
        } catch (EntityNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }
    @GetMapping("/byprojectid/{projectId}")
    public ResponseEntity<?> getWaitingDeallocationsByProjectId(@PathVariable UUID projectId) {
        try {

            return ResponseEntity.ok(deallocationProposalService.getWaitingDeallocationsByProjectId(projectId));
        } catch (EntityNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDeallocationProposals() {
        List<DeallocationProposal> deallocationProposalList = deallocationProposalService.getAllDealocations();
        if (deallocationProposalList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.ok(deallocationProposalList);
    }

    @GetMapping("filter-by-proposal-status")
    public ResponseEntity<?> getAllDeallocationProposalsByProposalStatus(@RequestParam ProposalStatus proposalStatus) {
        try {
            List<DeallocationProposal> deallocationProposalList = deallocationProposalService.getAllDeallocationsByProposalStatus(proposalStatus);
            if (deallocationProposalList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(deallocationProposalList);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{deallocationProposalId}")
    public ResponseEntity<?> updateDeallocationProposal(@PathVariable UUID deallocationProposalId, @Valid @RequestBody DeallocationProposalDto deallocationProposalDto) {

        try {
            return ResponseEntity.ok(deallocationProposalService.updateDeallocationById(deallocationProposalId, deallocationProposalDto));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("change-proposal-status/{deallocationProposalId}")
    public ResponseEntity<?> updateDeallocationProposalStatus(@PathVariable UUID deallocationProposalId, @RequestParam ProposalStatus proposalStatus) {
        try{
            return ResponseEntity.ok(deallocationProposalService.updateDeallocationProposalStatusById(deallocationProposalId,proposalStatus));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }
    @GetMapping("/department-manager")
    public ResponseEntity<?> updateDeallocationProposalStatus() {
        try{
            return ResponseEntity.ok(deallocationProposalService.getAllDeallocationsByDepartManager());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{deallocationProposalId}")
    public ResponseEntity<?> deleteDeallocationProposalById(@PathVariable UUID deallocationProposalId) {

        try {
            deallocationProposalService.deleteDeallocationById(deallocationProposalId);
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
