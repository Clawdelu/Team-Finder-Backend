package com.theinnovationtrio.TeamFinderAPI.skillCategory;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
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
public class SkillCategoryService implements ISkillCategoryService {

    private final SkillCategoryRepository skillCategoryRepository;


    @Override
    public SkillCategory createSkillCategory(SkillCategoryDto skillCategoryDto) {

        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDepartmentManagerRole) {
            SkillCategory skillCategory = SkillCategory
                    .builder()
                    .id(UUID.randomUUID())
                    .skillCategoryName(skillCategoryDto.getSkillCategoryName())
                    .createdBy(departmentManagerUser.getId())
                    .build();
            return skillCategoryRepository.save(skillCategory);
        } else {
            throw new AccessDeniedException("Unauthorized access! Don't have the department manager role.");
        }
    }

    @Override
    public SkillCategory getSkillCategoryById(UUID skillCategoryId) {

        return skillCategoryRepository.findById(skillCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Skill category not found!"));
    }

    @Override
    public List<SkillCategory> getAllSkillCategories() {

        return skillCategoryRepository.findAll();
    }

    @Override
    public List<SkillCategory> getAllSameOrgSkillCategories() {

        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if (hasDepartmentManagerRole) {
            return skillCategoryRepository.findAllSameOrgById(departmentManagerUser.getId());
        } else {
            throw new AccessDeniedException("Unauthorized access! Don't have the department manager role.");
        }
    }

    @Override
    public SkillCategory updateSkillCategory(UUID skillCategoryId, SkillCategoryDto skillCategoryDto) {

        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SkillCategory skillCategoryToUpdate = getSkillCategoryById(skillCategoryId);

        boolean createdTheSkill = departmentManagerUser.getId()
                .equals(skillCategoryToUpdate.getCreatedBy());

        if (createdTheSkill) {
            skillCategoryToUpdate.setSkillCategoryName(skillCategoryDto.getSkillCategoryName());

            return skillCategoryRepository.save(skillCategoryToUpdate);

        } else {
            throw new AccessDeniedException("Unauthorized access! You aren't the author.");
        }
    }

    @Override
    public void deleteSkillCategoryById(UUID skillCategoryId) {
        skillCategoryRepository.deleteById(skillCategoryId);
    }
}
