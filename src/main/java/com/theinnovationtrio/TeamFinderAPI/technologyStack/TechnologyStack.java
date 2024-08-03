package com.theinnovationtrio.TeamFinderAPI.technologyStack;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TechnologyStack {

    @Id
    private UUID id;

    private String technologyName;
}
