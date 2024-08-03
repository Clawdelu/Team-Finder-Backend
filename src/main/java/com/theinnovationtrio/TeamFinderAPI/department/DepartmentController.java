package com.theinnovationtrio.TeamFinderAPI.department;

import com.theinnovationtrio.TeamFinderAPI.user.UserDto;
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
@RequestMapping("/departments")
public class DepartmentController {

    private final IDepartmentService departmentService;

    @PostMapping()
    public ResponseEntity<?> createDepartment(@Valid  @RequestBody DepartmentDto departmentDto) {
        try {
            Department savedDepartment = departmentService.createDepartment(departmentDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedDepartment.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(departments);
        }
    }
    @GetMapping("/members")
    public ResponseEntity<List<UserDto>> getAllMembersFromDepartment() {
        List<UserDto> members = departmentService.getUsersFromDepartment();
        if (members.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(members);
        }
    }

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllDepartmentsFromSameOrganization() {
        try {
            List<Department> departments = departmentService.getAllSameOrgDepartments();
            if (departments.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(departments);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }

    }

    @GetMapping("{departmentId}")
    public ResponseEntity<?> getDepartmentById(@PathVariable UUID departmentId) {
        try {
            Department department = departmentService.getDepartmentById(departmentId);
            //afisez pt test users
            System.out.println("Department ID: " + department.getId() + " Users: ");
            department.getUsers().forEach(u -> System.out.println(u.getUserName() + " User ID: " + u.getId()));
            return ResponseEntity.ok(department);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{departmentId}")
    public ResponseEntity<?> updateDepartment(@PathVariable UUID departmentId, @RequestBody DepartmentDto departmentDto) {
        try {
            Department updatedDepartment = departmentService.updateDepartment(departmentId, departmentDto);
            return ResponseEntity.ok(updatedDepartment);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("/add-members")
    public ResponseEntity<?> addMembersToDepartment(@RequestBody List<UUID> userToAssignIds) {
        try {
            departmentService.addUsersToDepartment(userToAssignIds);
            return ResponseEntity.ok("User/s have been added successfully to department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("/remove-members")
    public ResponseEntity<?> removeMembersFromDepartment(@RequestBody List<UUID> userToAssignIds) {
        try {
            departmentService.removeUsersFromDepartment(userToAssignIds);
            return ResponseEntity.ok("User/s have been removed successfully from department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{departmentId}")
    public ResponseEntity<Object> deleteDepartmentById(@PathVariable UUID departmentId) {
        try {
            departmentService.deleteDepartmentById(departmentId);
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
