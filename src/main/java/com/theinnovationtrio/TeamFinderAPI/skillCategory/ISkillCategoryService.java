package com.theinnovationtrio.TeamFinderAPI.skillCategory;

import java.util.List;
import java.util.UUID;

public interface ISkillCategoryService {
    SkillCategory createSkillCategory(SkillCategoryDto skillCategoryDto);

    SkillCategory getSkillCategoryById(UUID skillCategoryId);

    List<SkillCategory> getAllSkillCategories();

    List<SkillCategory> getAllSameOrgSkillCategories();

    SkillCategory updateSkillCategory(UUID skillCategoryId, SkillCategoryDto skillCategoryDto);

    void deleteSkillCategoryById(UUID skillCategoryId);
}
