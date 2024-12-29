package com.example.joypadjourney.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.joypadjourney.model.entity.Customer;
import com.example.joypadjourney.model.entity.Payment;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Room;
import com.example.joypadjourney.model.entity.User;
import com.example.joypadjourney.repository.CustomerRepository;
import com.example.joypadjourney.repository.PaymentRepository;
import com.example.joypadjourney.repository.ReservationRepository;
import com.example.joypadjourney.repository.RoomRepository;
import com.example.joypadjourney.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;
    private final String jwtSecret = "your-256-bit-secret-your-256-bit-secret"; // Sesuaikan dengan kunci rahasia Anda

    public String extractUsernameFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Hapus "Bearer " di awal token
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // `sub` di JWT adalah username
    }
    public Reservation createReservation(String roomName, String start, String end, String authHeader) {

        // Ambil username dari JWT melalui SecurityContextHolder
        String username = extractUsernameFromToken(authHeader); // Ambil username dari token
        System.out.println("Username from token: " + username);
        long reservationCount = getReservationCount(username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        // Parse waktu mulai dan selesai
        LocalDateTime startDateTime = LocalDateTime.parse(start);
        LocalDateTime endDateTime = LocalDateTime.parse(end);

        // Ambil Room
        Room room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Generate Reservation ID
        String reservationId = "RNEYJ" + UUID.randomUUID().toString().substring(0, 8);
        double totalH = calculateTotalPrice(startDateTime, endDateTime, room.getPricePerHour());
        //bwat promo
        
        if (reservationCount==10){
            double discount = totalH * 0.1;
            totalH = totalH - discount;
        }
        
        
        // Buat Reservasi
        Reservation reservation = new Reservation();
        reservation.setReservationID(reservationId);
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStartDateTime(startDateTime);
        reservation.setEndDateTime(endDateTime);
        reservation.setStatus("PENDING");
        reservation.setPrice(totalH);

        reservationRepository.save(reservation);

        Payment payment = new Payment();
        payment.setPaymentId("PAY-" + UUID.randomUUID().toString().substring(0, 8));
        payment.setReservation(reservation);
        payment.setTotalPrice(reservation.getPrice());
        payment.setStatusPayment("PENDING");
        
        paymentRepository.save(payment);
        scheduleReservationNotification(user.getUsername(), room.getRoomName(), reservation);
        return reservation;
    }
    public long getReservationCount(String username) {
        return reservationRepository.countByUsername(username);
    }
    // Hitung Total Harga
    public double calculateTotalPrice(LocalDateTime start, LocalDateTime end, double pricePerHour) {
        long hours = java.time.Duration.between(start, end).toHours();
        return hours * pricePerHour;
    }
    public List<Room> getAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return roomRepository.findAvailableRooms(start, end);
    }
    // Kirim Reminder Email H-1 jam
    @Scheduled(fixedRate = 3600000) // Cek setiap 1 jam
    public void sendReservationReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(1);

        // Ambil reservasi yang akan dimulai dalam 1 jam
        List<Reservation> reservations = reservationRepository
                .findByStartDateTimeBetween(now, reminderTime);

        for (Reservation reservation : reservations) {
            // Ambil customer berdasarkan user
            Customer customer = customerRepository.findByUser(reservation.getUser())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Kirim email
            String email = customer.getEmail();
            String subject = "Reminder: Your Reservation is in 1 Hour!";
            String body = "Hi " + customer.getUsername() + ",\n\n"
                    + "This is a friendly reminder that your reservation for room "
                    + reservation.getRoom().getRoomName() + " will start at "
                    + reservation.getStartDateTime() + ".\n\n"
                    + "Thank you!";

            emailService.sendReminderEmail(email, subject, body);
            System.out.println("Reminder sent to: " + email);
        }

    }
    private void scheduleReservationNotification(String username, String roomName, Reservation reservation) {
        // Waktu notifikasi: 30 menit sebelum reservasi berakhir
        LocalDateTime notificationTime = reservation.getEndDateTime().minusMinutes(35);
    
        // Pesan notifikasi
        String message = "Your reservation for room " + roomName
                + " will end at " + reservation.getEndDateTime()
                + ". Please extend it if needed.";
    
        // Jadwalkan pengiriman notifikasi menggunakan NotificationService
        notificationService.scheduleNotification(username, message, notificationTime);
    }
    // Periksa reservasi yang selesai setiap 1 jam
    @Scheduled(fixedRate = 3600000) // Setiap 1 jam
    @Transactional
    public void updateCompletedReservations() {
        LocalDateTime now = LocalDateTime.now();

        // Ambil semua reservasi yang berakhir sebelum sekarang dan belum memiliki status COMPLETED
        List<Reservation> reservations = reservationRepository.findByEndDateTimeBeforeAndStatusNot(now, "COMPLETED");

        for (Reservation reservation : reservations) {
            // Perbarui status menjadi COMPLETED
            reservation.setStatus("COMPLETED");
            reservationRepository.save(reservation);
        }

        System.out.println("Updated " + reservations.size() + " reservations to COMPLETED status.");
    }
    public void deleteReservationById(String reservationID) {
        paymentRepository.deleteByReservationId(reservationID);
        reservationRepository.deleteById(reservationID);
    }
    
}
