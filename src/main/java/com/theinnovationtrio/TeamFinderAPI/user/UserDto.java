package com.theinnovationtrio.TeamFinderAPI.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.user_skill.User_Skill;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    @JsonIgnore
    private Department department;

    private String userName;

    private String email;

    private List<Role> roles;
    @JsonIgnore
    private List<User_Skill> skills;

    private int availableHours;

    private UUID organizationId;
}
