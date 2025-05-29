package com.example.finance.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;

@Component
public class JwtUtil {
  private final SecretKey secretKey;
  private final long expiration;

  // Inject from application.properties
  public JwtUtil(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration:86400000}") long expiration) { // default 1 day
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expiration = expiration;
  }

  public String generateToken(String email) {
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }
}
