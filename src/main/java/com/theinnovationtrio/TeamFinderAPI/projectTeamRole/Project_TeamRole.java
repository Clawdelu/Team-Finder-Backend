package com.theinnovationtrio.TeamFinderAPI.projectTeamRole;

import com.theinnovationtrio.TeamFinderAPI.teamRole.TeamRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project_TeamRole {

    @Id
    private UUID id;

    @ManyToOne
    private TeamRole teamRole;

    private int noOfMembers;

    private UUID projectId;
}
