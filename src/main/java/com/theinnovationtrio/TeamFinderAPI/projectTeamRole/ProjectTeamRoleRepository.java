package com.theinnovationtrio.TeamFinderAPI.projectTeamRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectTeamRoleRepository extends JpaRepository<Project_TeamRole, UUID> {
}
