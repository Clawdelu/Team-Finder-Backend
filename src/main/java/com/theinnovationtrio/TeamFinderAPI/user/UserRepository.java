package com.theinnovationtrio.TeamFinderAPI.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByOrganizationId(UUID organizationId);

    @Query("select u from User u " +
            "where u.organizationId = :organizationId " +
            "and u.department is null")
    List<User> findAllUnassignedEmp(@Param("organizationId") UUID organizationId);

    @Query("select u from User u " +
            "join fetch u.skills s " +
            "where s.id = :userSkillId")
    List<User> findAllByUserSkillId(UUID userSkillId);

    List<User> findAllByAvailableHoursAndOrganizationId(int availableHours, UUID organizationId);

}
