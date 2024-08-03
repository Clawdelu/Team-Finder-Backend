package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.project.IProjectService;
import com.theinnovationtrio.TeamFinderAPI.user_skill.IUserSkillService;
import com.theinnovationtrio.TeamFinderAPI.user_skill.UserSkillDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;
    private final IUserSkillService userSkillService;
    private final IProjectService projectService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        try {

            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException ex) {
            String errorMessage = "The user with ID " + userId + " was not found.";
            ErrorMessage errorResponse = new ErrorMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    }

    @GetMapping("/connected-user")
    public ResponseEntity<?> getConnectedUser() {
        return ResponseEntity.ok(userService.getConnectedUser());
    }

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllUsersFromOrganization() {
        try {

            List<UserDto> organizationUsers = userService.getOrganizationUsers();
            if (organizationUsers.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(organizationUsers);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }

    }

    @GetMapping("/unassigned")
    public ResponseEntity<?> getAllUnassignedUsers() {
        try {

            List<UserDto> unemployedUsers = userService.getAllUnassignedUsers();
            if (unemployedUsers.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(unemployedUsers);
            }
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/department-manager")
    public ResponseEntity<?> getAllAvailableDepartManagers() {
        try {

            List<UserDto> allFreeDepartManagers = userService.getAllFreeDepartManagers();
            if (allFreeDepartManagers.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(allFreeDepartManagers);
            }
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/users-from-projects-close-to-finish-in-n-weeks/{weeks}")
    public ResponseEntity<?> getAllUsersFromProjectsCloseToFinishInNWeeks(@PathVariable int weeks, @RequestParam UUID projectId) {
        try {
            List<UserDto> userDtoList = projectService.getAllUsersFromProjectCloseToFinish(weeks,projectId);
            if (userDtoList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(userDtoList);
            }
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/skills")
    public ResponseEntity<?> getAllSkillsFromUser() {

        var skills = userService.getAllUserSkills();
        if (skills.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(skills);
        }
    }

    // TREBUIE STERS; DOAR PETNTRU TEST
    @GetMapping("/SKILLS-BY-ID/{userSkillId}")
    public ResponseEntity<?> getSkillById(@PathVariable UUID userSkillId) {

        try {
            return ResponseEntity.ok(userSkillService.getUserSkillById(userSkillId));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    // TREBUIE STERS; DOAR PETNTRU TEST
    @GetMapping("/SKILLS-ALL")
    public ResponseEntity<?> getSkillById() {

        var skills = userSkillService.getAllUsersSkills();
        if (skills.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(skills);
        }
    }

    @GetMapping("/partially-available-users")
    public ResponseEntity<?> getPartiallyAvailableUsers(@RequestParam UUID projectId) {

        try {
            var users = projectService.getPartiallyAvailableUsers(projectId);
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/unavailable-users-for-project")
    public ResponseEntity<?> getUnavailableUsers(@RequestParam UUID projectId) {

        try {
            var unavailableUsers = projectService.getUnavailableUsers(projectId);
            if (unavailableUsers.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(unavailableUsers);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/fullly-available-users-for-project")
    public ResponseEntity<?> getFullyAvailableUsers(@RequestParam UUID projectId) {

        try {
            var fullyAvailableUsers = projectService.getFullyAvailableUsers(projectId);
            if (fullyAvailableUsers.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(fullyAvailableUsers);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("{userId}/assign-roles")
    public ResponseEntity<?> addRoleToUser(@PathVariable UUID userId, @RequestBody List<Role> roles) {
        try {

            userService.addRoleToUser(userId, roles);
            return ResponseEntity.ok("Roles have been added successfully!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("/assign-skill-to-user/{skillId}")
    public ResponseEntity<?> assignSkillToUser(@PathVariable UUID skillId, @RequestBody UserSkillDto userSkillDto) {
        try {

            userService.assignUserSkill(skillId, userSkillDto);
            return ResponseEntity.ok("Skill has been added successfully to user!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("update-skill/{userSkillId}")
    public ResponseEntity<?> updateUserSkill(@PathVariable UUID userSkillId, @RequestBody UserSkillDto userSkillDto) {

        try {

            var userSkill = userService.updateUserSkill(userSkillId, userSkillDto);
            return ResponseEntity.ok(userSkill);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("/remove-skill-from-user/{skillId}")
    public ResponseEntity<?> removeSkillFromUser(@PathVariable UUID skillId) {
        try {

            userService.removeUserSkillById(skillId);
            return ResponseEntity.ok("Skill has been removed successfully!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        try {

            userService.deleteUserById(userId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/{userId}/skills")
    public ResponseEntity<?> getSkillsByUserId(@PathVariable UUID userId) {
        try{
            var skills = userService.getUserSkillsByUserId(userId);
            if(skills.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok(skills);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }
}
