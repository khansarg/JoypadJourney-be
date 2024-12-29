package com.example.joypadjourney.controller;

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
    @PutMapping("/reservations/{reservationId}/extend")
    public ResponseEntity<Reservation> extendReservation(
            @PathVariable String reservationId,
            @RequestParam LocalDateTime newEnd, 
            @RequestParam String roomName) {
        Reservation updatedReservation = adminService.extendReservation(reservationId, null, newEnd, roomName);
        return ResponseEntity.ok(updatedReservation);
    }

    // Delete a reservation
    @DeleteMapping("/reservations/{reservationID}")
    public void deleteReservation(@PathVariable String reservationID) {
        adminService.deleteReservation(reservationID);
    }
}
