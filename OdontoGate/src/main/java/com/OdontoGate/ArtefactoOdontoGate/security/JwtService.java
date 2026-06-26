package com.OdontoGate.ArtefactoOdontoGate.security;

import com.OdontoGate.ArtefactoOdontoGate.model.Privilege;
import com.OdontoGate.ArtefactoOdontoGate.model.Role;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String secret;
    private final long expirationMinutes;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-minutes}") long expirationMinutes) {
        this.secret = secret;
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", getRoleName(user));
        claims.put("privileges", getPrivilegeNames(user));

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMinutes * 60 * 1000);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getRoleName(User user) {
        Role role = user.getRole();
        return role == null ? null : role.getNombre();
    }

    private List<String> getPrivilegeNames(User user) {
        Role role = user.getRole();

        if (role == null) {
            return List.of();
        }

        return role.getPrivileges()
                .stream()
                .map(Privilege::getNombre)
                .toList();
    }
}
