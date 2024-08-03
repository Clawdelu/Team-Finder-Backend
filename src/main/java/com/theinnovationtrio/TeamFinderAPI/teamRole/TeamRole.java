package com.theinnovationtrio.TeamFinderAPI.teamRole;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TeamRole {
    @Id
    private UUID id;
    private String roleInProject;
    private UUID createdBy;
}
