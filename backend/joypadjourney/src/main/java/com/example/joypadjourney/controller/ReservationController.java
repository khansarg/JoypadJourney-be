package com.example.joypadjourney.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Endpoint untuk menampilkan room yang tersedia
    @GetMapping("/available-rooms")
    public ResponseEntity<?> getAvailableRooms(HttpServletRequest request,
                                               @RequestParam String startDateTime,
                                               @RequestParam String endDateTime) {
    // Mendapatkan header Authorization
    System.out.println("Start DateTime: " + startDateTime);
    System.out.println("End DateTime: " + endDateTime);

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        try {
            LocalDateTime startDT = LocalDateTime.parse(startDateTime);
            LocalDateTime endDT = LocalDateTime.parse(endDateTime);

            return ResponseEntity.ok(reservationService.getAvailableRooms(startDT, endDT));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date or time format. Use 'YYYY-MM-DD' and 'HH:mm'.");
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
