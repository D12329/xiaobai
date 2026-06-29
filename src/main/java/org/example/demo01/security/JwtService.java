package org.example.demo01.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret:MySecretKeyForJWTTokenGenerationAndVerification2024}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private Long expirationTime;

    private static final long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String refreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String username = claims.getSubject();
            long issuedAt = claims.getIssuedAt().getTime();
            long now = System.currentTimeMillis();

            if (now - issuedAt > REFRESH_EXPIRATION_TIME) {
                return null;
            }

            return generateToken(username);
        } catch (Exception e) {
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
