package com.example.joypadjourney.controller;

import com.example.joypadjourney.model.Request.LoginRequest;
import com.example.joypadjourney.model.Response.LoginResponse;
import com.example.joypadjourney.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
