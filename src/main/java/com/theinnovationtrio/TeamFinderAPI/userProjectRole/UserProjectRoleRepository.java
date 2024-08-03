package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserProjectRoleRepository extends JpaRepository<User_Project_Role, UUID> {
    List<User_Project_Role> findAllByProjectId(UUID projectId);
    User_Project_Role findByProjectIdAndUserId(UUID projectId, UUID userId);

    @Query("select u from User_Project_Role u " +
            "where u.userId =:userId")
    List<User_Project_Role> getAllByUserId(UUID userId);
}

