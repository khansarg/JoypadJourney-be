package com.example.joypadjourney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Endpoint untuk mendapatkan detail pembayaran dan QRIS
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestParam String reservationID) {
        paymentService.confirmPayment(reservationID);
        return ResponseEntity.ok("Payment confirmed and reservation updated to PAID.");
    }

}
