package com.theinnovationtrio.TeamFinderAPI.userProjectRole;

import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
import com.theinnovationtrio.TeamFinderAPI.teamRole.TeamRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User_Project_Role {
    @Id
    private UUID id;

    private UUID userId;

    private UUID projectId;

    @ManyToMany
    private List<TeamRole> teamRoles;

    private StatusOfMember statusOfMember;

    private int workHours;
}
