package com.solution.apigateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secret;

    public Optional<UUID> extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);

            String userIdStr = claims.get("userId", String.class);
            return Optional.ofNullable(userIdStr).map(UUID::fromString);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<String> extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);
            List<?> roles = claims.get("roles", List.class);
            if (roles != null) {
                return roles.stream()
                        .map(Object::toString)
                        .toList();
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
