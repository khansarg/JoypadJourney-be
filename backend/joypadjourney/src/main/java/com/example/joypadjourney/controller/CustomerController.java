package com.example.joypadjourney.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.Request.RegisterCustomerRequest;
import com.example.joypadjourney.model.Response.WebResponse;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.service.CustomerService;




@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public WebResponse<String> register(@RequestBody RegisterCustomerRequest request) {
        customerService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
    @GetMapping("/reservations")
    public List<Reservation> viewMyReservations(@RequestParam String username) {
        return customerService.viewMyReservation(username);
    }
    
}
