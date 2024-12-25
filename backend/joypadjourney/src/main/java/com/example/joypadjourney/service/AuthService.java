package com.example.joypadjourney.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.Security.BCrypt;
import com.example.joypadjourney.Security.JwtUtil;
import com.example.joypadjourney.model.Request.LoginRequest;
import com.example.joypadjourney.model.Response.LoginResponse;
import com.example.joypadjourney.model.entity.Admin;
import com.example.joypadjourney.model.entity.Customer;
import com.example.joypadjourney.repository.AdminRepository;
import com.example.joypadjourney.repository.CustomerRepository;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> login(LoginRequest request) {
        validationService.validate(request);

        if (request.getEmail() == null && request.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email atau Username harus diisi."));
        }

        String token;
        try {
            if (request.getEmail() != null) {
                Customer customer = customerRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Akun dengan email tersebut tidak ditemukan"));

                if (!BCrypt.checkpw(request.getPassword(), customer.getUser().getPassword())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "Password salah."));
                }

                token = jwtUtil.generateToken(customer.getUser().getUsername(), "Customer");

                LoginResponse response = LoginResponse.builder()
                        .username(customer.getUser().getUsername())
                        .role("Customer")
                        .token(token)
                        .build();

                return ResponseEntity.ok(response);

            } else if (request.getUsername() != null) {
                Admin admin = adminRepository.findById(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Admin dengan username tersebut tidak ditemukan"));

                if (!BCrypt.checkpw(request.getPassword(), admin.getUser().getPassword())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "Password salah."));
                }

                token = jwtUtil.generateToken(admin.getUser().getUsername(), "Admin");

                LoginResponse response = LoginResponse.builder()
                        .username(admin.getUser().getUsername())
                        .role("Admin")
                        .token(token)
                        .build();

                return ResponseEntity.ok(response);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Permintaan tidak valid."));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
