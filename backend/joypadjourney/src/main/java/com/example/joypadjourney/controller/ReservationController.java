package com.example.joypadjourney.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.Security.JwtUtil;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.service.ReservationService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint untuk menampilkan room yang tersedia
    @GetMapping("/available-rooms")
public ResponseEntity<?> getAvailableRooms(
        @RequestHeader(value = "Authorization", required = false) String authHeader, 
        @RequestParam String startDateTime, 
        @RequestParam String endDateTime) {
    try {
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        // Ambil token dari header
        String token = authHeader.substring(7);
        Claims claims = jwtUtil.validateToken(token);
        System.out.println("Token is valid. Claims: " + claims);

        LocalDateTime startDT = LocalDateTime.parse(startDateTime);
        LocalDateTime endDT = LocalDateTime.parse(endDateTime);
        // Logika backend lainnya
        return ResponseEntity.ok(reservationService.getAvailableRooms(startDT, endDT));
    } catch (Exception e) {
        System.err.println("Error occurred: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}

    

    // Endpoint untuk membuat reservasi
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(
        @RequestParam String roomName,
        @RequestParam String start,
        @RequestParam String end) {
        Reservation reservation = reservationService.createReservation(roomName, start, end);
        return ResponseEntity.ok(reservation);
    }

}
