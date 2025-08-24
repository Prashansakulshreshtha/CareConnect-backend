package com.careconnect.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private static final String SECRET = "careconnectsecretkeycareconnectsecretkey";
    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 10;  // 10 hours

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(Date.from(now))
                   .setExpiration(Date.from(now.plusMillis(EXPIRATION_MS)))
                   .signWith(key)
                   .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(key)
                       .setAllowedClockSkewSeconds(60)  // ← allow 60‑second skew
                       .build()
                       .parseClaimsJws(token)
                       .getBody()
                       .getSubject();
        } catch (JwtException e) {
            log.error("Failed to parse token while extracting username: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .setAllowedClockSkewSeconds(60)  // ← same skew here
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

            String tokenUsername = claims.getSubject();
            Date expiration     = claims.getExpiration();

            if (!username.equals(tokenUsername)) {
                log.warn("Token subject mismatch: token={}, expected={}", tokenUsername, username);
                return false;
            }

            if (expiration.before(new Date())) {
                log.warn("Token expired at {}", expiration);
                return false;
            }

            return true;

        } catch (ExpiredJwtException ex) {
            log.warn("Token expired at {} (now {})", ex.getClaims().getExpiration(), Instant.now());
        } catch (JwtException ex) {
            log.error("Invalid token: {}", ex.getMessage());
        }
        return false;
    }
}
