package com.theinnovationtrio.TeamFinderAPI.teamRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TeamRoleRepository extends JpaRepository<TeamRole, UUID> {

    @Query("select t from TeamRole t " +
            "join User u on t.createdBy = u.id " +
            "where u.organizationId = (select u1.organizationId from User u1 where u1.id = :userId)")
    List<TeamRole> findAllSameOrgBy(UUID userId);
}
