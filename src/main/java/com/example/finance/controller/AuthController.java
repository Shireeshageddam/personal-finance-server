package com.example.finance.controller;

import com.example.finance.model.User;
import com.example.finance.repository.UserRepository;
import com.example.finance.security.JwtUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private BCryptPasswordEncoder encoder;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody AuthRequest request) {
    if (userRepo.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body("Email already in use");
    }
    User user = User.builder()
        .email(request.getEmail())
        .password(encoder.encode(request.getPassword()))
        .build();
    userRepo.save(user);
    return ResponseEntity.ok("User registered");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    User user = userRepo.findByEmail(request.getEmail()).orElse(null);
    if (user == null || !encoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }
    String token = jwtUtil.generateToken(user.getEmail());
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @Data
  static class AuthRequest {
    private String email;
    private String password;
  }

  @Data
  @AllArgsConstructor
  static class AuthResponse {
    private String token;
  }

}
