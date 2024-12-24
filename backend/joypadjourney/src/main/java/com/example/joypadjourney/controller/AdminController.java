package com.example.joypadjourney.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.service.AdminService;
import com.example.joypadjourney.service.PaymentService;
import com.example.joypadjourney.service.ReservationService;


@RestController
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    private ReservationService reservationService;
    private PaymentService paymentService;
     @Autowired
    private AdminService adminService;
     @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestParam String roomName,
                                               @RequestParam String start,
                                               @RequestParam String end,
                                               Principal principal) {
        String username = principal.getName(); // Username otomatis dari user yang login

        
        Reservation reservation = reservationService.createReservation(username, roomName, start, end);
        return ResponseEntity.ok(reservation);
    }
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestParam String reservationID) {
        paymentService.confirmPayment(reservationID);
        return ResponseEntity.ok("Payment confirmed and reservation updated to PAID.");
    }
    // View all reservations
    @GetMapping("/reservations")
    public List<Reservation> viewAllReservations() {
        return adminService.viewReservation();
    }

    // Extend a reservation
    @PutMapping("/reservations/{reservationID}/extend")
    public Reservation extendReservation(
            @PathVariable String reservationID,
            @RequestParam String newStart,
            @RequestParam String newEnd) {

        LocalDateTime startDateTime = LocalDateTime.parse(newStart);
        LocalDateTime endDateTime = LocalDateTime.parse(newEnd);

        return adminService.extendReservation(reservationID, startDateTime, endDateTime);
    }

    // Delete a reservation
    @DeleteMapping("/reservations/{reservationID}")
    public void deleteReservation(@PathVariable String reservationID) {
        adminService.deleteReservation(reservationID);
    }
}
