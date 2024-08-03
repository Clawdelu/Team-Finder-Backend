package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-project")
public class UserProjectRoleController {

    private final IUserProjectRoleService userProjectRoleService;

    @GetMapping
    public ResponseEntity<?> getAllUserProjectRole() {
        var userProjRole = userProjectRoleService.getAllUserProjectRoles();
        if (userProjRole.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(userProjRole);
        }
    }

    // la asta e facut ceva asemanator la Project Controller
    @GetMapping("projects-by-userId/{userId}")
    public ResponseEntity<?> getAllProjectsByUserId(@PathVariable UUID userId) {
        try {
            var projects = userProjectRoleService.getAllUserProjectRolesByUserId(userId);
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(projects);
            }

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("users-by-projectId/{projectId}")
    public ResponseEntity<?> getAllUsersByProjectId(@PathVariable UUID projectId) {
        try {
            var viewProjectTeam = userProjectRoleService.getUsersByProjectId(projectId);
            return ResponseEntity.ok(viewProjectTeam);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserPRojRole(@PathVariable UUID id) {
        try {
            userProjectRoleService.deleteUserProjectRoleById(id);
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
