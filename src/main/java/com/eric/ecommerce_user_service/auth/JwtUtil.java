package com.eric.ecommerce_user_service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final SecretKey signingKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String username, List<String> roles) {
        return Jwts
                .builder()
                .subject(username) // Username as subject
                .claim("roles", roles) // Add roles to token claims
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(signingKey) // Secure signing
                .compact();
    }

    // Extract Claims from Token
    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract Username from Token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract Roles from Token
    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return claims.get("roles", List.class); // Get roles from claims
    }

    // Extract Specific Claim (Generic Method)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    // Validate Token
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username);
    }

    // Check if Token is Expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
