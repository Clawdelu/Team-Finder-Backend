package com.theinnovationtrio.TeamFinderAPI.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String name;
    private String email;
    private String password;
}
