package com.theinnovationtrio.TeamFinderAPI.organization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Organization {
    @Id
    private UUID id;
    private UUID createdBy;
    private String organizationName;
    private String headquarterAddress;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> users;

}
