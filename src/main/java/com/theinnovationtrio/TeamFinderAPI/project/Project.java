package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.projectTeamRole.Project_TeamRole;
import com.theinnovationtrio.TeamFinderAPI.technologyStack.TechnologyStack;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {

    @Id
    private UUID id;

    private String projectName;

    private ProjectPeriod projectPeriod;

    private LocalDate startDate;

    private LocalDate deadlineDate;

    private ProjectStatus projectStatus;

    private String generalDescription;

    @OneToMany(fetch = FetchType.EAGER)
    private List<TechnologyStack> technologyStack;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Project_TeamRole> teamRoles;

    private UUID createdBy;
}
