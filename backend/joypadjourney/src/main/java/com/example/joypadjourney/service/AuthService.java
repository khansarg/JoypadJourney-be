package com.example.joypadjourney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.Security.BCrypt;
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

    public ResponseEntity<LoginResponse> login(LoginRequest request){
        validationService.validate(request);
        if(request.getEmail()!=null){
            Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Akun dengan email tersebut tidak ditemukan"));
            if (!BCrypt.checkpw(request.getPassword(), customer.getUser().getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            LoginResponse response = LoginResponse.builder()
                .username(customer.getUser().getUsername())
                .role("Customer")
                .build();
            return ResponseEntity.ok(response);
        } else if (request.getUsername() != null) {
            // Admin login
            Admin admin = adminRepository.findById(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Admin dengan username atau password tersebut tidak ditemukan. Jika anda bukan admin, silakan login menggunakan email."));

            if (!BCrypt.checkpw(request.getPassword(), admin.getUser().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            LoginResponse response = LoginResponse.builder()
                    .username(admin.getUser().getUsername())
                    .role("Admin")
                    .build();
            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
