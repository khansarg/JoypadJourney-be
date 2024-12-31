package com.example.joypadjourney.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joypadjourney.model.entity.Review;
import com.example.joypadjourney.repository.ReviewRepository;
import com.example.joypadjourney.service.ReviewService;

@RestController
@RequestMapping("/customer/reviews")
public class CustomerReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    // Endpoint untuk menambahkan review
    @PostMapping("/{reservationID}")
public ResponseEntity<Map<String, String>> addReview(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
        @PathVariable String reservationID,
        @RequestBody Review review) {
    try {
        reviewService.addReview(review, reservationID);
        // Kembalikan JSON sebagai respons sukses
        Map<String, String> response = new HashMap<>();
        response.put("message", "Review added successfully!");
        return ResponseEntity.ok(response);
    } catch (IllegalStateException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}

    @GetMapping("/viewReviews")
    public ResponseEntity<List<Review>> getAllReviews(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        List<Review> reviews = reviewRepository.findAll();
    return ResponseEntity.ok(reviews);
}

}
