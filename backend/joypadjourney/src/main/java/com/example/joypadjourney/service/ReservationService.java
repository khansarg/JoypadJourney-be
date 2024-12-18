package com.example.joypadjourney.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Room;
import com.example.joypadjourney.model.entity.User;
import com.example.joypadjourney.repository.ReservationRepository;
import com.example.joypadjourney.repository.RoomRepository;
import com.example.joypadjourney.repository.UserRepository;

@Service
public class ReservationService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    // Menampilkan room yang tersedia
    public List<Room> getAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return roomRepository.findAvailableRooms(start, end);
    }

    // Membuat reservasi (customer)
    public void createReservation(ReservationRequest request, String username) {
        // Generate Reservation ID
        LocalDate reservationDate = request.getStartDateTime().toLocalDate();
        String reservationID = generateReservationID(reservationDate);
    
        // Cari user dan room
        User user = userRepository.findByUsername(username);
        Room room = roomRepository.findById(request.getRoomName())
            .orElseThrow(() -> new RuntimeException("Room not found"));
    
        // Buat reservasi
        Reservation reservation = new Reservation();
        reservation.setReservationID(reservationID);
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStartDateTime(request.getStartDateTime());
        reservation.setEndDateTime(request.getEndDateTime());
        reservation.setStatus("Pending");
        reservation.setPrice(calculatePrice(room.getPricePerHour(), request.getStartDateTime(), request.getEndDateTime()));
    
        reservationRepository.save(reservation);
    }


    public String generateReservationID(LocalDate reservationDate) {
        // 1. Ambil tanggal, bulan (3 huruf), dan tahun
        int day = reservationDate.getDayOfMonth();
        String month = reservationDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        int year = reservationDate.getYear();

        // 2. Hitung jumlah reservasi pada hari tersebut untuk menentukan urutan ke berapa
        long count = reservationRepository.countByStartDateTimeBetween(
            reservationDate.atStartOfDay(),
            reservationDate.plusDays(1).atStartOfDay()
        );

        // 3. Format Reservation ID
        return String.format("RNEYJ%02d%s%d%02d", day, month, year, count + 1);
    }
}
