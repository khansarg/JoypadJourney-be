package com.example.joypadjourney.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestHeader("Authorization") String authorization,@RequestBody Map<String, String> payload) {
        System.out.println("Authorization Header: " + authorization);
        String reservationID = payload.get("reservationID");
        paymentService.confirmPayment(reservationID);
       Map<String, String> response = new HashMap<>();
    response.put("message", "Payment confirmed and reservation updated to PAID.");
    response.put("status", "success");

    return ResponseEntity.ok(response);
    }

}
