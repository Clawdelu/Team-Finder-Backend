package com.theinnovationtrio.TeamFinderAPI.skillCategory;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, UUID> {

    @Query("select s from SkillCategory s " +
            "join User u on s.createdBy = u.id " +
            "where u.organizationId = (select u1.organizationId from User u1 where u1.id = :userId)")
    List<SkillCategory> findAllSameOrgById(UUID userId);

}
