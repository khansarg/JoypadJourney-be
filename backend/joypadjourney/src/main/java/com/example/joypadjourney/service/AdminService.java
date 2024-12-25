package com.example.joypadjourney.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.model.entity.Payment;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.repository.PaymentRepository;
import com.example.joypadjourney.repository.ReservationRepository;

@Service
public class AdminService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationService reservationService; // Inject ReservationService

    // View all reservations
    public List<Reservation> viewReservation() {
        return reservationRepository.findAll();
    }

    // Extend reservation
    public Reservation extendReservation(String reservationId, LocalDateTime newStart, LocalDateTime newEnd) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Validasi waktu reservasi (hari ini dan masih ada waktu 30 menit)
        LocalDateTime now = LocalDateTime.now();
        if (!now.toLocalDate().equals(reservation.getStartDateTime().toLocalDate()) ||
            reservation.getEndDateTime().isBefore(now.plusMinutes(30))) {
            throw new RuntimeException("Extension not allowed. Reservation must be today and within 30 minutes.");
        }

        // Update waktu reservasi
        reservation.setStartDateTime(newStart);
        reservation.setEndDateTime(newEnd);

        // Hitung ulang harga menggunakan ReservationService
        double newPrice = reservationService.calculateTotalPrice(newStart, newEnd, reservation.getRoom().getPricePerHour());
        reservation.setPrice(newPrice);

        // Update status pembayaran jika ada
        Payment payment = paymentRepository.findByReservation_ReservationID(reservationId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setTotalPrice(newPrice);

        reservationRepository.save(reservation);
        paymentRepository.save(payment);

        return reservation;
    }

    // Delete reservation
    public void deleteReservation(String reservationID) {
        Reservation reservation = reservationRepository.findById(reservationID)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservationRepository.delete(reservation);
    }
}