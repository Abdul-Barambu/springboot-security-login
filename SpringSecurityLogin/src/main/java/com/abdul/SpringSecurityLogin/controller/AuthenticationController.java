package com.abdul.SpringSecurityLogin.controller;

import com.abdul.SpringSecurityLogin.dto.JwtAuthenticationResponse;
import com.abdul.SpringSecurityLogin.dto.RefreshTokenRequest;
import com.abdul.SpringSecurityLogin.dto.SignInRequest;
import com.abdul.SpringSecurityLogin.dto.SignUpRequest;
import com.abdul.SpringSecurityLogin.entities.User;
import com.abdul.SpringSecurityLogin.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signin(signInRequest));
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
}
