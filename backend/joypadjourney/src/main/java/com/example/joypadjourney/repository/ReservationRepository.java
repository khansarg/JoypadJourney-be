package com.example.joypadjourney.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.joypadjourney.model.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query("SELECT r FROM Reservation r WHERE r.reservationID = :reservationID AND r.user.username = :username")
    Optional<Reservation> findByReservationIDAndUserUsername(@Param("reservationID") String reservationID, @Param("username") String username);
    long countByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.username = :username")
    long countByUsername(@Param("username") String username);
    List<Reservation> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByEndDateTimeBetween(LocalDateTime start, LocalDateTime end);
    // Cari reservasi yang berakhir sebelum waktu tertentu dan statusnya bukan COMPLETED
    @Query("SELECT r FROM Reservation r WHERE r.endDateTime < :currentDateTime AND r.status <> :status")
    List<Reservation> findByEndDateTimeBeforeAndStatusNot(LocalDateTime currentDateTime, String status);
}
