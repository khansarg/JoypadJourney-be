package com.example.joypadjourney.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.Security.JwtUtil;
import com.example.joypadjourney.model.Request.RegisterCustomerRequest;
import com.example.joypadjourney.model.Response.WebResponse;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.repository.ReservationRepository;
import com.example.joypadjourney.service.CustomerService;
//import com.example.joypadjourney.service.NotificationCacheService;




@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    //@Autowired
    //private NotificationCacheService notificationCacheService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/reservations/completedAndNotReviewed")
    public ResponseEntity<List<Reservation>> getCompletedAndNotReviewedReservations(
            @RequestHeader("Authorization") String authorizationHeader) {

        // Ekstrak token dari header
        if (!authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String token = authorizationHeader.substring(7); // Hapus "Bearer " dari header

        // Dapatkan username dari token
        String username = jwtUtil.getUsernameFromToken(token);

        // Query database untuk mencari reservasi yang sesuai
        List<Reservation> reservations = reservationRepository.findCompletedAndNotReviewedReservations(username);
        return ResponseEntity.ok(reservations);
    }
    @PostMapping("/register")
    public WebResponse<String> register(@RequestBody RegisterCustomerRequest request) {
        customerService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
    @GetMapping("/reservations")
public ResponseEntity<?> viewMyReservations(@RequestHeader(value = "Authorization", required = false) String authHeader) {
    try {
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        // Decode token to extract username
        String token = authHeader.substring(7);
        String username = customerService.extractUsernameFromToken(token);
        System.out.println("Username from token: " + username);

        // Get reservations for the username
        List<Reservation> reservations = customerService.viewMyReservation(username);
        return ResponseEntity.ok(reservations);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch reservations");
    }
}


    /*@GetMapping("/notifications/{username}")
    public List<String> getNotifications(@RequestHeader(value = "Authorization", required = false) String authHeader,@PathVariable String username) {
        return notificationCacheService.getNotifications(username);
    }*/
    @PutMapping("/reservations/{reservationId}/extend")
public ResponseEntity<Reservation> extendReservation(
    @PathVariable String reservationId,
    @RequestBody Map<String, Object> requestBody) {

    LocalDateTime newEnd = LocalDateTime.parse((String) requestBody.get("newEnd"));
    String roomName = (String) requestBody.get("roomName");

    Reservation updatedReservation = customerService.extendReservation(reservationId, null, newEnd, roomName);
    return ResponseEntity.ok(updatedReservation);
}



    
}
