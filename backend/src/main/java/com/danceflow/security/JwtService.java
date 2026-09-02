package com.danceflow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMillis;

    public JwtService(@Value("${danceflow.jwt.secret}") String secret,
                      @Value("${danceflow.jwt.expire-hours:24}") long expireHours) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        }
        this.key = Keys.hmacShaKeyFor(bytes);
        this.expirationMillis = expireHours * 60 * 60 * 1000;
    }

    public String createToken(AuthUser user) {
        Date now = new Date();
        return Jwts.builder().subject(user.username()).claim("userId", user.id()).claim("role", user.role())
                .issuedAt(now).expiration(new Date(now.getTime() + expirationMillis)).signWith(key).compact();
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public long getExpirationSeconds() {
        return expirationMillis / 1000;
    }
}
