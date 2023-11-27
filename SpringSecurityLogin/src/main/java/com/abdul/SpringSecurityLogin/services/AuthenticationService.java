package com.abdul.SpringSecurityLogin.services;

import com.abdul.SpringSecurityLogin.dto.JwtAuthenticationResponse;
import com.abdul.SpringSecurityLogin.dto.RefreshTokenRequest;
import com.abdul.SpringSecurityLogin.dto.SignInRequest;
import com.abdul.SpringSecurityLogin.dto.SignUpRequest;
import com.abdul.SpringSecurityLogin.entities.User;

public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signInRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
