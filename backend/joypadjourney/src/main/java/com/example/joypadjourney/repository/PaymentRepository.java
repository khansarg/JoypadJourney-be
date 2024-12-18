package com.example.joypadjourney.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.joypadjourney.model.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByReservation_ReservationID(String reservationID);
    List<Payment> findByStatusPayment(String statusPayment);
}
