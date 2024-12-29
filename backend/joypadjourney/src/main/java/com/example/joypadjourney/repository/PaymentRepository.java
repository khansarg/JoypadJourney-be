package com.example.joypadjourney.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.joypadjourney.model.entity.Payment;

import jakarta.transaction.Transactional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p WHERE p.reservation.reservationID = :reservationID")
    void deleteByReservationId(@Param("reservationID") String reservationID);
    Optional<Payment> findByReservation_ReservationID(String reservationID);
    List<Payment> findByStatusPayment(String statusPayment);
}
