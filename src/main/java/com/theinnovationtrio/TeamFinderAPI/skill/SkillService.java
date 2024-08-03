package com.theinnovationtrio.TeamFinderAPI.skill;

import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.skillCategory.ISkillCategoryService;
import com.theinnovationtrio.TeamFinderAPI.skillCategory.SkillCategory;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillService implements ISkillService {

    private final SkillRepository skillRepository;
    private final ISkillCategoryService skillCategoryService;

    @Override
    public Skill createSkill(SkillDto skillDto) {

        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDepartmentManagerRole) {
            Skill skill = Skill
                    .builder()
                    .id(UUID.randomUUID())
                    .skillCategory(skillCategoryService.getSkillCategoryById(skillDto.getSkillCategoryId()))
                    .skillName(skillDto.getSkillName())
                    .description(skillDto.getDescription())
                    .createdBy(departmentManagerUser.getId())
                    .build();
            if (skillDto.isAddedToDepartment()) {
                var departments = skill.getDepartments();
                departments.add(departmentManagerUser.getDepartment());
                skill.setDepartments(departments);
            }
            return skillRepository.save(skill);

        } else {
            throw new AccessDeniedException("Unauthorized access! Don't have the department manager role.");
        }
    }

    @Override
    public Skill getSkillById(UUID skillId) {

        return skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found"));
    }

    @Override
    public List<Skill> getAllSkills() {

        return skillRepository.findAll();
    }

    @Override
    public List<Skill> getAllSameOrgSkills() {

        var departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Employee));

        if (hasDepartmentManagerRole) {
            return skillRepository.findAllSameOrgById(departmentManagerUser.getId());

        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public List<Skill> getAllSameDepartmentSkills() {

        var departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDepartmentManagerRole) {
            return skillRepository.findAllBySameDepartment(departmentManagerUser.getDepartment().getId());
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public List<Skill> getAllSameSkillCategorySkills(UUID skillCategoryId) {

        var departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDepartmentManagerRole) {
            return skillRepository.findAllBySameCategory(skillCategoryId);

        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public List<Skill> getAllSkillsCreatedBy() {

        var departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDepartmentManagerRole) {
            return skillRepository.findAllBySameAuthor(departmentManagerUser.getId());

        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public void deleteSkillCategoryById(UUID skillCategoryId) {
        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SkillCategory skillCategoryToDelete = skillCategoryService.getSkillCategoryById(skillCategoryId);

        boolean createdTheSkill = departmentManagerUser.getId()
                .equals(skillCategoryToDelete.getCreatedBy());

        if (createdTheSkill) {
            List<Skill> skillsToUpdate = getAllSameSkillCategorySkills(skillCategoryId)
                    .stream()
                    .peek(skill -> skill.setSkillCategory(null))
                    .toList();
            saveAllSkills(skillsToUpdate);
            skillCategoryService.deleteSkillCategoryById(skillCategoryId);

        } else {
            throw new AccessDeniedException("Unauthorized access! You aren't the author.");
        }
    }

    @Override
    public void removeDepartmentFromAllSkills(UUID departmentId) {
        var skills = skillRepository.findAllBySameDepartment(departmentId);
        skills.forEach(skill -> {
            List<Department> departments = skill.getDepartments();
            departments.removeIf(department -> department.getId()
                    .equals(departmentId));
            skill.setDepartments(departments);
            skillRepository.save(skill);
        });
    }

    @Override
    public void saveAllSkills(List<Skill> skills) {
        skillRepository.saveAll(skills);
    }


    @Override
    public void assignSkillToDepartment(UUID skillId) {
        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));
        if (hasDepartmentManagerRole) {
            Skill skill = getSkillById(skillId);
            List<Department> departments = skill.getDepartments();
            if (departments.stream()
                    .noneMatch(department -> department.getId().equals(departmentManagerUser.getDepartment().getId()))) {
                departments.add(departmentManagerUser.getDepartment());
                skill.setDepartments(departments);
                skillRepository.save(skill);
            }
        } else {
            throw new AccessDeniedException("Unauthorized access! Don't have the department manager role.");
        }
    }

    @Override
    public void removeSkillFromDepartment(UUID skillId) {
        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));
        if (hasDepartmentManagerRole) {
            Skill skill = getSkillById(skillId);
            List<Department> departments = skill.getDepartments();
            departments.removeIf(department -> department.getId()
                    .equals(departmentManagerUser.getDepartment().getId()));

            skill.setDepartments(departments);
            skillRepository.save(skill);

        } else {
            throw new AccessDeniedException("Unauthorized access! Don't have the department manager role.");
        }
    }

    @Override
    public Skill updateSkill(UUID skillId, SkillDto skillDto) {

        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Skill skillToUpdate = getSkillById(skillId);

        boolean createdTheSkill = departmentManagerUser.getId()
                .equals(skillToUpdate.getCreatedBy());

        if (createdTheSkill) {
            skillToUpdate.setSkillCategory(skillCategoryService.getSkillCategoryById(skillDto.getSkillCategoryId()));
            skillToUpdate.setSkillName(skillDto.getSkillName());
            skillToUpdate.setDescription(skillDto.getDescription());
            if (skillDto.isAddedToDepartment()) {
                List<Department> departments = skillToUpdate.getDepartments();
                if (!departments.contains(departmentManagerUser.getDepartment())) {
                    departments.add(departmentManagerUser.getDepartment());
                }
                skillToUpdate.setDepartments(departments);
            }
            return skillRepository.save(skillToUpdate);

        } else {
            throw new AccessDeniedException("Unauthorized access! You aren't the author.");
        }
    }

    @Override
    public void deleteSkillById(UUID skillId) {
        skillRepository.deleteById(skillId);
    }
}
