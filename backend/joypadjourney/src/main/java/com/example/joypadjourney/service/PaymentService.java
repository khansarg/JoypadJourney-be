package com.example.joypadjourney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.model.entity.Payment;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.repository.PaymentRepository;
import com.example.joypadjourney.repository.ReservationRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ReservationRepository reservationRepository;

   public void confirmPayment(String reservationID) {
        Payment payment = paymentRepository.findByReservation_ReservationID(reservationID)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatusPayment("PAID");
        paymentRepository.save(payment);

        Reservation reservation = payment.getReservation();
        reservation.setStatus("PAID");
        reservationRepository.save(reservation);
    }
}
