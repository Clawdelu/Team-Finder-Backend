package com.theinnovationtrio.TeamFinderAPI.teamRole;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-roles")
public class TeamRoleController {

    private final ITeamRoleService teamRoleService;

    @GetMapping()
    public ResponseEntity<List<TeamRole>> getAllTeamRoles() {

        List<TeamRole> teamRoles = teamRoleService.getAllTeamRoles();
        if (teamRoles.isEmpty()) {
            return ResponseEntity.noContent().header("Message", "There is no team role.").build();
        } else {
            return ResponseEntity.ok(teamRoles);
        }
    }

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllTeamRolesFromSameOrganization(){
        try{
            List<TeamRole> teamRoles = teamRoleService.getAllSameOrgTeamRoles();
            if (teamRoles.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(teamRoles);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/{teamRoleId}")
    public ResponseEntity<?> getTeamRoleById(@PathVariable UUID teamRoleId) {
        try {
            TeamRole teamRole = teamRoleService.getTeamRoleById(teamRoleId);
            return ResponseEntity.ok(teamRole);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> createTeamRole(@RequestBody TeamRoleDto teamRoleDto) {

        try {
            TeamRole teamRole = teamRoleService.createTeamRole(teamRoleDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(teamRole.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{teamRoleId}")
    public ResponseEntity<?> updateTeamRole(@PathVariable UUID teamRoleId, @RequestBody TeamRoleDto teamRoleDto) {
        try {
            TeamRole updatedTeamRole = teamRoleService.updateTeamRole(teamRoleId, teamRoleDto);
            return ResponseEntity.ok(updatedTeamRole);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{teamRoleId}")
    public ResponseEntity<?> deleteTeamRoleById(@PathVariable UUID teamRoleId) {
        try {
            teamRoleService.deleteTeamRole(teamRoleId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }


}
