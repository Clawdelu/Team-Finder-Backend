package com.theinnovationtrio.TeamFinderAPI.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    @Query("select s from Skill s " +
            "join User u on s.createdBy = u.id " +
            "where u.organizationId = (select u1.organizationId from User u1 where u1.id = :userId)")
    List<Skill> findAllSameOrgById(UUID userId);

    @Query("select s from Skill s " +
            "join fetch s.departments d " +
            "where d.id = :departmentId")
    List<Skill> findAllBySameDepartment(UUID departmentId);


    @Query("select s from Skill s " +
            "where s.skillCategory.id = :skillCategoryId")
    List<Skill> findAllBySameCategory(UUID skillCategoryId);

    @Query("select s from Skill s " +
            "where s.createdBy = :authorId")
    List<Skill> findAllBySameAuthor(UUID authorId);

    @Query("delete from User_Skill us " +
            "where us.skill.id = :skillId")
    void deleteSkillFromUser_SkillBySkillId(UUID skillId);


}
