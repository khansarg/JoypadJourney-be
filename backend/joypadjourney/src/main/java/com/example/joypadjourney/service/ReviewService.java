package com.example.joypadjourney.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Review;
import com.example.joypadjourney.repository.ReservationRepository;
import com.example.joypadjourney.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // Tambahkan review baru
    public void addReview(Review review, String reservationID) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Validasi reservasi
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationIDAndUserUsername(reservationID, username);

        if (optionalReservation.isEmpty()) {
            throw new IllegalStateException("Reservation not found or does not belong to the user.");
        }

        Reservation reservation = optionalReservation.get();

        //memastikan reservasi selesai
        if (!"COMPLETED".equalsIgnoreCase(reservation.getStatus())) {
            throw new IllegalStateException("Cannot review a reservation that is not completed.");
        }

        //memaastikan review belum ada untuk reservasi ini
        if (reviewRepository.existsByReservationReservationID(reservationID)) {
            throw new IllegalStateException("Review already exists for this reservation.");
        }

        // nyiimpen review
        review.setReservation(reservation);
        review.setUser(reservation.getUser());
        review.setTanggal(LocalDateTime.now());
        reviewRepository.save(review);
    }
    
}
