package com.abdul.SpringSecurityLogin.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();
}
