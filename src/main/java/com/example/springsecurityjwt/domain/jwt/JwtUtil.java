package com.example.springsecurityjwt.domain.jwt;

import com.example.springsecurityjwt.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private String secret = "mysecretkeyforsigningjwtverysecure123";
    private long expirationMs = 3600000;

    private Key signingKey;

    @PostConstruct
    protected void init(){
        byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username, UserRole role){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (JwtException|IllegalArgumentException exception) {
            return false;
        }
    }

    public String getUsername(String token){
        return getClaims(token).getSubject();
    }

    public String getRole(String token){
        return getClaims(token).get("role", String.class);
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
