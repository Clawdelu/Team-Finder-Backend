package com.theinnovationtrio.TeamFinderAPI.auth;

import com.theinnovationtrio.TeamFinderAPI.invite.IInviteService;
import com.theinnovationtrio.TeamFinderAPI.invite.Invite;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final IUserService userService;
    private final IInviteService inviteService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody AdminRegisterRequest request
    ) {
        if (userService.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/register/{inviteId}")
    public ResponseEntity<?> registerUser(@PathVariable UUID inviteId,
                                          @RequestBody UserRegisterRequest request
    ) {
        if (!inviteService.existsById(inviteId)) {
            return new ResponseEntity<>("The invitation is not valid!", HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        Invite invite = inviteService.getInviteById(inviteId);
        if(!invite.isAvailable()){
            return new ResponseEntity<>("The invitation is not valid!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(service.registerUser(request,invite));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
