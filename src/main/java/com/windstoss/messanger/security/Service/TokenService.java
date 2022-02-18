package com.windstoss.messanger.security.Service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class TokenService {

    private static final Key TOKEN_KEY = Keys.hmacShaKeyFor("Surely a production encryption key. Ah yes-yes.".getBytes());
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, TOKEN_KEY)
                .compact();
    }

    public String getUsernameFromToken(String jwt) {
        return Jwts.parser()
                .setSigningKey(TOKEN_KEY)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
