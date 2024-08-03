package com.theinnovationtrio.TeamFinderAPI.auth;

import com.theinnovationtrio.TeamFinderAPI.config.JwtService;
import com.theinnovationtrio.TeamFinderAPI.invite.IInviteService;
import com.theinnovationtrio.TeamFinderAPI.invite.Invite;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import com.theinnovationtrio.TeamFinderAPI.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final IInviteService inviteService;


    public AuthenticationResponse register(AdminRegisterRequest request) {
        User user = userService.createUser(request);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerUser(UserRegisterRequest request, Invite invite) {
        User user = userService.createUser(request, invite.getOrganizationId());
        inviteService.updateInvite(invite.getId(), false);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userService.getUserByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
