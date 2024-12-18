package com.example.joypadjourney.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.joypadjourney.model.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    long countByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
