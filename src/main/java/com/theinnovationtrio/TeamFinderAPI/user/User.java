package com.theinnovationtrio.TeamFinderAPI.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.user_skill.User_Skill;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Department department;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private List<Role> roles;

    @OneToMany
    private List<User_Skill> skills;

    private int availableHours;

    private UUID organizationId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role r : roles) {
            authorities.add(new SimpleGrantedAuthority(r.name()));
        }
        return authorities;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
