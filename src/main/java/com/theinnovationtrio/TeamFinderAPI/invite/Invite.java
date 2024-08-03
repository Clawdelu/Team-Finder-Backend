package com.theinnovationtrio.TeamFinderAPI.invite;

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
public class Invite {
    @Id
    private UUID id;
    private UUID organizationId;
    private UUID createdBy;
    private boolean available;

}
