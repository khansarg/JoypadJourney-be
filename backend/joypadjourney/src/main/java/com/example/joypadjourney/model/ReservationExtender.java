package com.example.joypadjourney.model;

import java.time.LocalDateTime;

import com.example.joypadjourney.model.entity.Reservation;

public interface ReservationExtender {
    Reservation extendReservation(String reservationId, LocalDateTime newStart, LocalDateTime newEnd, String roomName);
}
