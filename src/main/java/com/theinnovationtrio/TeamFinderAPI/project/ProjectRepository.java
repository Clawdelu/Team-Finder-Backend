package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findAllByCreatedBy(UUID authorId);
    List<Project> findAllByProjectPeriodAndCreatedBy(ProjectPeriod projectPeriod, UUID author);
    List<Project> findAllByProjectStatusAndCreatedBy(ProjectStatus projectStatus, UUID author);

        @Query("SELECT p.id FROM Project p " +
                "WHERE p.deadlineDate < :endDate")
    List<UUID> findAllByDeadlineDateInNextWeeks(LocalDate endDate);
}
