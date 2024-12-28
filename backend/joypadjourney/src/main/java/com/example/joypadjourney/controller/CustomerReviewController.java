package com.example.joypadjourney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.entity.Review;
import com.example.joypadjourney.service.ReviewService;

@RestController
@RequestMapping("/customer/reviews")
public class CustomerReviewController {

    @Autowired
    private ReviewService reviewService;

    // Endpoint untuk menambahkan review
    @PostMapping("/{reservationID}")
    public ResponseEntity<String> addReview(
            @PathVariable String reservationID,
            @RequestBody Review review) {
        try {
            reviewService.addReview(review, reservationID);
            return ResponseEntity.ok("Review added successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
