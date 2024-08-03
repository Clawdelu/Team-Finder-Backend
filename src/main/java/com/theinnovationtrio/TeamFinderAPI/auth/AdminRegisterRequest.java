package com.theinnovationtrio.TeamFinderAPI.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String organizationName;
    private String headquarterAddress;
}
