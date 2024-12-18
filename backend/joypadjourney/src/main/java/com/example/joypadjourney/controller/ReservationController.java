package com.example.joypadjourney.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Room;
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

        List<Room> availableRooms = reservationService.getAvailableRooms(startDateTime, endDateTime);
        return ResponseEntity.ok(availableRooms);
    }

    // Endpoint untuk membuat reservasi
    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestParam String roomName,
                                               @RequestParam String start,
                                               @RequestParam String end,
                                               Principal principal) { // Ambil user login
        String username = principal.getName(); // Username dari session

        // Buat reservasi
        Reservation reservation = reservationService.createReservation(username, roomName, start, end);
        return ResponseEntity.ok(reservation);
    }

    // Endpoint khusus untuk admin (opsional jika diperlukan logika tambahan)
    @PostMapping("/admin/create")
    public ResponseEntity<?> adminCreateReservation(@RequestParam String customerUsername,
                                                    @RequestParam String roomName,
                                                    @RequestParam String start,
                                                    @RequestParam String end,
                                                    Principal principal) {
        // Ambil username admin
        String adminUsername = principal.getName();

        // Admin membuat reservasi atas nama customer
        Reservation reservation = reservationService.adminCreateReservation(adminUsername, customerUsername, roomName, start, end);
        return ResponseEntity.ok(reservation);
    }
}

