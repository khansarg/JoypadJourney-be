package com.example.joypadjourney.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> getAvailableRooms(@RequestParam String date,
                                               @RequestParam String startTime,
                                               @RequestParam String endTime) {
        LocalDateTime startDateTime = LocalDateTime.parse(date + "T" + startTime);
        LocalDateTime endDateTime = LocalDateTime.parse(date + "T" + endTime);

        return ResponseEntity.ok(reservationService.getAvailableRooms(startDateTime, endDateTime));
    }

    // Endpoint untuk membuat reservasi
    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestParam String roomName,
                                               @RequestParam String start,
                                               @RequestParam String end,
                                               Principal principal) {
        String username = principal.getName(); // Username otomatis dari user yang login

        
        Reservation reservation = reservationService.createReservation(username, roomName, start, end);
        return ResponseEntity.ok(reservation);
    }
    
}
