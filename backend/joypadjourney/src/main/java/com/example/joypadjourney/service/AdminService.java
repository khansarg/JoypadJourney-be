package com.example.joypadjourney.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.model.ReservationExtender;
import com.example.joypadjourney.model.entity.Payment;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Room;
import com.example.joypadjourney.repository.PaymentRepository;
import com.example.joypadjourney.repository.ReservationRepository;
import com.example.joypadjourney.repository.RoomRepository;

@Service
public class AdminService implements ReservationExtender{

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationService reservationService; // Inject ReservationService

    @Autowired
    private RoomRepository roomRepository;

    // View all reservations
    public List<Reservation> viewReservation() {
        return reservationRepository.findAll();
    }

    // Extend reservation
    @Override
public Reservation extendReservation(String reservationId, LocalDateTime newStart, LocalDateTime newEnd, String roomName) {
    
    Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));


    // Validasi waktu
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime extendDeadline = reservation.getEndDateTime().minusMinutes(30);

    if (now.isBefore(reservation.getStartDateTime()) || now.isAfter(extendDeadline)) {
        throw new RuntimeException("You can only extend your reservation after it starts and up to 30 minutes before it ends.");
    }

    newStart = reservation.getEndDateTime();
    boolean isRoomAvailable = reservationRepository.findByRoomAndTimeRange(roomName, newStart, newEnd).isEmpty();
    if (!isRoomAvailable) {
        throw new RuntimeException("Room is not available for the extended time.");
    }

    reservation.setEndDateTime(newEnd);

    Room room = roomRepository.findById(roomName)
            .orElseThrow(() -> new RuntimeException("Room not found"));

    reservation.setRoom(room);

    // Hitung ulang harga berdasarkan waktu baru
    double newPrice = reservationService.calculateTotalPrice(newStart, newEnd, room.getPricePerHour());
    reservation.setPrice(reservation.getPrice()+newPrice);

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
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
}