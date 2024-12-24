package com.example.joypadjourney.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.joypadjourney.model.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    long countByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.username = :username")
    long countByUsername(@Param("username") String username);
    List<Reservation> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByEndDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
