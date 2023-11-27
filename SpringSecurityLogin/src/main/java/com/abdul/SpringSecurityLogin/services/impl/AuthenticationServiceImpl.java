package com.abdul.SpringSecurityLogin.services.impl;

import com.abdul.SpringSecurityLogin.dto.JwtAuthenticationResponse;
import com.abdul.SpringSecurityLogin.dto.RefreshTokenRequest;
import com.abdul.SpringSecurityLogin.dto.SignInRequest;
import com.abdul.SpringSecurityLogin.dto.SignUpRequest;
import com.abdul.SpringSecurityLogin.entities.Role;
import com.abdul.SpringSecurityLogin.entities.User;
import com.abdul.SpringSecurityLogin.repository.UserRepository;
import com.abdul.SpringSecurityLogin.services.AuthenticationService;
import com.abdul.SpringSecurityLogin.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    //    sign up
    @Override
    public User signup(SignUpRequest signUpRequest) {
        User user = new User();

        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }

    //    sign in
    @Override
    public JwtAuthenticationResponse signin(SignInRequest signInRequest) {
//        validate user email and password using Auth manager
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

//        generate token
        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
                () -> new IllegalStateException("invalid email")
        );
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

//        create jwtResponseAuth object and set the token and refresh token and return it
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }


    // refresh token
    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());

        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {

//            generate new refresh token
            var jwt = jwtService.generateToken(user);


//              send the response
            //        create jwtResponseAuth object and set the token and refresh token and return it
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }

        return null;
    }

}
