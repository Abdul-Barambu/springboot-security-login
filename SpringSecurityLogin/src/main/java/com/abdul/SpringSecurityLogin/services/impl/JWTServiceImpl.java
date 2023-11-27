package com.abdul.SpringSecurityLogin.services.impl;

import com.abdul.SpringSecurityLogin.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {

// 0.   generate token method
    public String generateToken(UserDetails userDetails){  // create this method in JWTService
        return Jwts.builder().setSubject(userDetails.getUsername()) //get username
                .setIssuedAt(new Date(System.currentTimeMillis())) // set issue date of the token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // set expiration date
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // get the token sign key
                .compact();
    }

//    25. generate refresh token
public String generateRefreshToken(Map<String, Object> extractClaims, UserDetails userDetails){  // create this method in JWTService
    return Jwts.builder().setClaims(extractClaims).setSubject(userDetails.getUsername()) //get username
            .setIssuedAt(new Date(System.currentTimeMillis())) // set issue date of the token
            .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // set expiration date
            .signWith(getSignKey(), SignatureAlgorithm.HS256) // get the token sign key
            .compact();
}

    //    4. extract the username
    public String extractUserName(String token){  // create this method in JWTService
        return extractClaim(token, Claims::getSubject);
    }


//    2. extract claim
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

//    3. extractAllClaims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
//        it will return all the claims
    }

    //    1. create getSignKey method
    private Key getSignKey(){
        byte[] key = Decoders.BASE64.decode("413F4428472B4B6250655268566D5970337336763979244226452948404D6351");
        return Keys.hmacShaKeyFor(key);
    }

//    14.       check token validation
    public boolean isTokenValid(String token, UserDetails userDetails){  // create this method in JWTService
//        extract username from token
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }

//    15. check if the token is expired
    private boolean isTokenExpire(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date()); // it will get expiration from our token and check if expiration is before current date
    }

}
