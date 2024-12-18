package com.example.joypadjourney.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.service.PaymentService;
import com.example.joypadjourney.service.ReservationService;


@RestController
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    private ReservationService reservationService;
    private PaymentService paymentService;
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
}
