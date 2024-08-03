package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import com.theinnovationtrio.TeamFinderAPI.userProjectRole.ViewDepartmentProjectDto;
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
@RequestMapping("/project")
public class ProjectController {

    private final IProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDto projectDto) {

        try {
            Project savedProject = projectService.createProject(projectDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedProject.getId())
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

    @GetMapping
    public ResponseEntity<?> getAllProjects() {

        List<Project> projects = projectService.getAllProjects();

        if (projects.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(projects);
        }
    }

    @GetMapping("{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable UUID projectId) {

        try {

            return ResponseEntity.ok(projectService.getProjectById(projectId));
        } catch (EntityNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/same-author")
    public ResponseEntity<?> getAllProjectsFromAuthor() {

        try{
            List<Project> projects = projectService.getAllSameAuthorProjects();
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(projects);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("filter-by-project-period")
    public ResponseEntity<?> getAllProjectsByPeriod(@RequestParam ProjectPeriod projectPeriod) {

        try{

            List<Project> projects = projectService.getAllProjectsByProjectPeriod(projectPeriod);

            if(projects.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(projects);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }
    @GetMapping("filter-by-project-status")
    public ResponseEntity<?> getAllProjectsByStatus(@RequestParam ProjectStatus projectStatus) {

        try{

            List<Project> projects = projectService.getAllProjectsByProjectStatus(projectStatus);

            if(projects.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(projects);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("view-employee-projects/{userId}")
    public ResponseEntity<?> getAllUserProjects(@PathVariable UUID userId) {

        var projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("view-departments-projects")
    public ResponseEntity<?> getAllProjectsForUserIds() {
        var projects = projectService.getProjectsForDepartmentManagerByUserIds();
        if (projects.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(projects);
        }
    }

    @PutMapping("{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable UUID projectId, @Valid @RequestBody ProjectDto projectDto) {

        try{
            return ResponseEntity.ok(projectService.updateProject(projectId,projectDto));
        }  catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<?> deleteProjectById(@PathVariable UUID projectId) {

        try{
            projectService.deleteProjectById(projectId);
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
