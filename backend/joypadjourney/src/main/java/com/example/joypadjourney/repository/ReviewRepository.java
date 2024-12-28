package com.example.joypadjourney.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.joypadjourney.model.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    // Mengecek apakah review sudah ada untuk reservasi tertentu
    boolean existsByReservationReservationID(String reservationID);
}
