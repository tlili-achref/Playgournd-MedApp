package com.medapp.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.medapp.backend.model.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration-ms}") long accessExpirationMs,
        @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs){
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            this.accessExpirationMs = accessExpirationMs;
            this.refreshExpirationMs = refreshExpirationMs;
        }

    public String generateAccessToken(User user) {
        Date maintenant = new Date();
        Date expiration = new Date(maintenant.getTime() + accessExpirationMs);

        return Jwts.builder()
                .subject(user.getId())
                .claim("role", user.getRole().name())
                .claim("type" , "access")
                .issuedAt(maintenant)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User  user){
        Date maintenant = new Date();
        Date expiration = new Date(maintenant.getTime() + refreshExpirationMs);

        return Jwts.builder()
                .subject(user.getId())
                .claim("type" , "refresh")
                .issuedAt(maintenant)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String extractTokenType(String token){
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("type" , String.class);
    }

    public String extractUserId(String token){
        return Jwts.parser()        
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role" , String.class);
    }

    private boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isAccessTokenValid(String token){
        return isTokenValid(token) && "access".equals(extractTokenType(token));
    }

     public boolean isRefreshTokenValid(String token){
        return isTokenValid(token) && "refresh".equals(extractTokenType(token));
    }
    
}
