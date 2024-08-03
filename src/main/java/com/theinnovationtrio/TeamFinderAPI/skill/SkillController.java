package com.theinnovationtrio.TeamFinderAPI.skill;

import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/skills")
//@CrossOrigin(origins = "http://localhost:3000")
public class SkillController {

    private final ISkillService skillService;
    private final IUserService userService;

    @GetMapping()
    public ResponseEntity<List<Skill>> getAllSkills() {

        List<Skill> skills = skillService.getAllSkills();
        if (skills.isEmpty()) {
            return ResponseEntity.noContent()
                    .header("Message", "There is no skill.").build();

        } else {
            return ResponseEntity.ok(skills);
        }
    }

    @GetMapping("{skillId}")
    public ResponseEntity<?> getSkillById(@PathVariable UUID skillId) {
        try {

            Skill skill = skillService.getSkillById(skillId);
            return ResponseEntity.ok(skill);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllSkillsFromSameOrganization() {
        try {

            List<Skill> skills = skillService.getAllSameOrgSkills();
            if (skills.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(skills);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/same-department")
    public ResponseEntity<?> getAllSkillsFromSameDepartment(){
        try{

            List<Skill> skills = skillService.getAllSameDepartmentSkills();
            if(skills.isEmpty()){
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(skills);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/skill-category")
    public ResponseEntity<?> getAllSkillsForCategory(@RequestParam UUID skillCategoryId){
        try{

            List<Skill> skills = skillService.getAllSameSkillCategorySkills(skillCategoryId);
            if(skills.isEmpty()){
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(skills);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/same-author")
    public ResponseEntity<?> getAllSkillsCreatedBy(){
        try{

            List<Skill> skills = skillService.getAllSkillsCreatedBy();
            if(skills.isEmpty()){
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(skills);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<?> createSkill(@RequestBody SkillDto skillDto) {
        try {

            Skill skill = skillService.createSkill(skillDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(skill.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{skillId}")
    public ResponseEntity<?> updateSkill(@PathVariable UUID skillId, @RequestBody SkillDto skillDto) {
        try {

            Skill skill = skillService.updateSkill(skillId, skillDto);
            return ResponseEntity.ok(skill);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("{skillId}/add-skill-to-department")
    public ResponseEntity<?> addSkillToDepartment(@PathVariable UUID skillId) {
        try {

            skillService.assignSkillToDepartment(skillId);
            return ResponseEntity.ok("Skill have been added successfully to department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{skillId}/remove-skill-from-department")
    public ResponseEntity<?> removeSkillFromDepartment(@PathVariable UUID skillId) {
        try {

            skillService.removeSkillFromDepartment(skillId);
            return ResponseEntity.ok("Skill have been removed successfully from department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{skillId}")
    public ResponseEntity<?> deleteSkill(@PathVariable UUID skillId) {
        try {
            userService.deleteSkillById(skillId);
            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

}
