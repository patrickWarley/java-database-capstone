package com.project.back_end.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenService {

  private final DoctorRepository doctorRepository;
  private final AdminRepository adminRepository;
  private final PatientRepository patientRepository;
  private final Environment env;

  private final Logger logger = LoggerFactory.getLogger(TokenService.class);

  @Autowired
  public TokenService(DoctorRepository doctorRepository, AdminRepository adminRepository,
      PatientRepository patientRepository, Environment env) {
    this.doctorRepository = doctorRepository;
    this.adminRepository = adminRepository;
    this.patientRepository = patientRepository;
    this.env = env;
  }

  public SecretKey getSigningKey() {
    String key = env.getProperty("secret");
    return Keys.hmacShaKeyFor(key.getBytes());
  }

  public String generateToken(String identifier, String role) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiration = now.plusDays(7);

    return Jwts.builder()
        .subject(identifier)
        .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
        .expiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(getSigningKey())
        .claim("role", role)
        .compact();
  }

  public String extractEmail(String token) {
    try {

      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject();

    } catch (Exception e) {
      logger.error("An error ocurred when trying to parse the email! - " + e.getMessage());
      return null;
    }
  }

  public boolean validateToken(String role, String token) {
    try {

      Claims claims = Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();

      if (!claims.get("role", String.class).equals(role))
        return false;

      String identifier = extractEmail(token);
      Object result = null;

      switch (role) {
        case "admin":
          result = adminRepository.findByUsername(identifier);
          break;
        case "patient":
          result = patientRepository.findByEmail(identifier);
          break;
        case "doctor":
          result = doctorRepository.findByEmail(identifier);
          break;
      }
      if (result != null)
        return true;

      return false;
    } catch (Exception e) {
      logger.error("An error ocurred when validating the token - " + e.getMessage());
      return false;
    }
  }

}
