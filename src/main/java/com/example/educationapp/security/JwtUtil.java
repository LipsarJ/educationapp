package com.example.educationapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUtil {

    private static final int expireHourToken = 24;

    private static final String SECRET = "FBA898697394CDBC534E7ED86A97AA59F627FE6B309E0A21EEC6C9B130E0369C";

    public static String createAccessToken(String username, String issuer, List<String> roles) {
        Instant now = Instant.now();
        Date expirationDate = Date.from(now.plusSeconds(expireHourToken * 3600));

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .claim("roles", roles)
                .setExpiration(expirationDate)
                .setIssuedAt(Date.from(now))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public static UsernamePasswordAuthenticationToken parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        List<SimpleGrantedAuthority> authorities = roles == null ? null : roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}