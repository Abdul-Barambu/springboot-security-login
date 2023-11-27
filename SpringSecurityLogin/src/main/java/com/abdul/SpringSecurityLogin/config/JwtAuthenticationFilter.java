package com.abdul.SpringSecurityLogin.config;

import com.abdul.SpringSecurityLogin.services.JWTService;
import com.abdul.SpringSecurityLogin.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 5.
    private final JWTService jwtService;

    // 6.
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

// 7.        get authorization header using request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

//  8.      check if authHeader is empty and starts with bearer using StringUtil
//          before you can check for bearer, add a dependency of "commons-lang", org.apache.commons"
        if(StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

//  9.      get token and store it in jwt & get email and pass the jwt token stored in jw
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserName(jwt);

//  10.      check if the user email is not empty and check for security contextHolder
        if (io.micrometer.common.util.StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
//  11.      create object of userDetails and load username using UserDetailService.loadUserByUsername
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

//  16.     check for the isTokenValid create empty security context
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

//  17.      create object for username and password auth token
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

//  18.     accept the token and setDetails to object of WebAuthDetailSource
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


//  19.     setAuthentication using securityContext and set the Security context using security context holder
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);

            }


        }

        filterChain.doFilter(request, response);
    }
}
