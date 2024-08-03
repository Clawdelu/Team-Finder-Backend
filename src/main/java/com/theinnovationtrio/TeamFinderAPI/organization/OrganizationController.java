package com.theinnovationtrio.TeamFinderAPI.organization;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {
    private final IOrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        if (organizations.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(organizations);
        }
    }

    @GetMapping("/invite/{inviteId}")
    public ResponseEntity<?> getOrganizationByInviteId(@PathVariable UUID inviteId) {

        try {
            return ResponseEntity.ok(organizationService.getOrganizationByInvite(inviteId));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("{organizationId}")
    public ResponseEntity<?> getOrganizationById(@PathVariable UUID organizationId) {
        try {
            Organization organization = organizationService.getOrganizationById(organizationId);
            return ResponseEntity.ok(organization);
        } catch (EntityNotFoundException ex) {
            String errorMessage = "Organizatia cu ID-ul " + organizationId + " nu a fost gasita.";
            ErrorMessage errorResponse = new ErrorMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    }

    @DeleteMapping("{organizationId}")
    public void deleteOrganization(@PathVariable UUID organizationId) {
        organizationService.deleteOrganizationById(organizationId);
    }

    @PutMapping("{organizationId}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable UUID organizationId, @RequestBody OrganizationDto organizationDto) {
        try {
            Organization updatedOrganization = organizationService.updateOrganization(organizationId, organizationDto);
            return ResponseEntity.ok(updatedOrganization);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
