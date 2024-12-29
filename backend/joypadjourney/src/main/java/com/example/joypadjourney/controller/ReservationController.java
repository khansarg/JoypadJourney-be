package com.example.joypadjourney.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.Security.JwtUtil;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Room;
import com.example.joypadjourney.repository.RoomRepository;
import com.example.joypadjourney.service.ReservationService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoomRepository roomRepository;

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

    
@DeleteMapping("/reservations/{reservationID}")
public ResponseEntity<?> deleteReservation(@PathVariable String reservationID) {
    try {
        reservationService.deleteReservationById(reservationID);
        return ResponseEntity.ok(Map.of("message", "Reservation deleted successfully"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
    }
}

    // Endpoint untuk membuat reservasi
    @PostMapping("/reservations")
    public ResponseEntity<Map<String, Object>> createReservation(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestBody Map<String, Object> requestBody) {
            String roomName = (String) requestBody.get("room");
            String start = (String) requestBody.get("startDateTime");
            String end = (String) requestBody.get("endDateTime");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Missing or invalid Authorization header"));
        }
       try {
        // Buat reservasi menggunakan service
        Reservation reservation = reservationService.createReservation(roomName, start, end, authHeader);
        LocalDateTime startDT = LocalDateTime.parse(start);
        LocalDateTime endDT = LocalDateTime.parse(end);
        // Bangun respons JSON
        Duration duration = Duration.between(startDT, endDT);
        String username = reservation.getUser().getUsername();
        // Konversi ke jam dan menit
        long hours = duration.toHours();

        Map<String, Object> response = new HashMap<>();
        response.put("reservationID", reservation.getReservationID());
        response.put("subtotal", reservationService.calculateTotalPrice(startDT, endDT, reservation.getRoom().getPricePerHour()));
        if (reservationService.getReservationCount(username)==10){
            response.put("discount", (reservation.getRoom().getPricePerHour() *hours)* 0.1); // Contoh diskon
        } else {
            response.put("discount", 0);
        }
        response.put("total", reservation.getPrice());

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error creating reservation: " + e.getMessage()));
    }
    }
    @GetMapping("/room-details")
    public ResponseEntity<?> getRoomDetails(@RequestHeader(value = "Authorization", required = false) String authHeader,@RequestParam String roomName) {
        System.out.println("Authorization Header: " + authHeader);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
    }
    try {
        Room room = roomRepository.findByRoomName(roomName)
            .orElseThrow(() -> new RuntimeException("Room not found"));
        return ResponseEntity.ok(room);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching room details: " + e.getMessage());
    }
}


}
