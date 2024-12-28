package com.example.joypadjourney.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.Request.RegisterCustomerRequest;
import com.example.joypadjourney.model.Response.WebResponse;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.service.CustomerService;
import com.example.joypadjourney.service.NotificationCacheService;




@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private NotificationCacheService notificationCacheService;

    @PostMapping("/register")
    public WebResponse<String> register(@RequestBody RegisterCustomerRequest request) {
        customerService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
    @GetMapping("/reservations")
    public List<Reservation> viewMyReservations(@RequestParam String username) {
        return customerService.viewMyReservation(username);
    }

    @GetMapping("/notifications/{username}")
    public List<String> getNotifications(@PathVariable String username) {
        return notificationCacheService.getNotifications(username);
    }
    @PutMapping("/reservations/{reservationId}/extend")
    public ResponseEntity<Reservation> extendReservation(
        @PathVariable String reservationId,
        @RequestParam LocalDateTime newEnd, 
        @RequestParam String roomName ) {
        Reservation updatedReservation = customerService.extendReservation(reservationId, null, newEnd, roomName);
        return ResponseEntity.ok(updatedReservation);
    }


    
}
