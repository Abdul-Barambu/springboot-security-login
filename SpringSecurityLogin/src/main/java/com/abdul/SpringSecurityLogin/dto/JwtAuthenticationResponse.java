package com.abdul.SpringSecurityLogin.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;
    private String refreshToken;
}
