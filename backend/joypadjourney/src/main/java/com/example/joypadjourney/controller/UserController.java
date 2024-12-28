package com.example.joypadjourney.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.joypadjourney.Security.JwtUtil;
import com.example.joypadjourney.model.entity.User;
import com.example.joypadjourney.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public class UserController {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;
    @GetMapping("/api/user/me")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    String token = authHeader.substring(7);
    Claims claims = jwtUtil.validateToken(token);
    String username = claims.getSubject();

    // Ambil user dari database berdasarkan username
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}

}
