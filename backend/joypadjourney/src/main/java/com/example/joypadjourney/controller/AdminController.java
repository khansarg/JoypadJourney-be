package com.example.joypadjourney.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Review;
import com.example.joypadjourney.repository.ReviewRepository;
import com.example.joypadjourney.service.AdminService;


@RestController
@RequestMapping("api/admin")
public class AdminController {
    
     @Autowired
    private AdminService adminService;
    @Autowired 
    ReviewRepository reviewRepository;
    // View all reservations
    @GetMapping("/reservations")
    public List<Reservation> viewAllReservations() {
        return adminService.viewReservation();
    }

    // Extend a reservation
    @PutMapping("/reservations/{reservationId}/extend")
    public ResponseEntity<Reservation> extendReservation(
            @PathVariable String reservationId,
            @RequestBody Map<String, Object> requestBody) {
        LocalDateTime newEnd = LocalDateTime.parse((String) requestBody.get("newEnd"));
        String roomName = (String) requestBody.get("roomName");
        Reservation updatedReservation = adminService.extendReservation(reservationId, null, newEnd, roomName);
        return ResponseEntity.ok(updatedReservation);
    }

    
    @GetMapping("/viewReservations")
    public ResponseEntity<?> getAllReservations(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            System.out.println("Authorization Header: " + authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }
             // Decode token and log claims (optional)
            String token = authHeader.substring(7);
            System.out.println("Token: " + token);
            List<Reservation> reservations = adminService.getAllReservations();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch reservations");
        }
    }
    @GetMapping("/viewReviews")
    public ResponseEntity<List<Review>> getAllReviews(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Unauthorized access: Missing or invalid Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Review> reviews = reviewRepository.findAll();
    return ResponseEntity.ok(reviews);
}
}

