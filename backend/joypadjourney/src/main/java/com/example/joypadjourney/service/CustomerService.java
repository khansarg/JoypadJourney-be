package com.example.joypadjourney.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.joypadjourney.Security.BCrypt;
import com.example.joypadjourney.model.Request.RegisterCustomerRequest;
import com.example.joypadjourney.model.ReservationExtender;
import com.example.joypadjourney.model.entity.Customer;
import com.example.joypadjourney.model.entity.Payment;
import com.example.joypadjourney.model.entity.Reservation;
import com.example.joypadjourney.model.entity.Role;
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
public class CustomerService implements ReservationExtender{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ValidationService validationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private RoomRepository roomRepository;

  
    private final String jwtSecret = "your-256-bit-secret-your-256-bit-secret"; 

    public String extractUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Username ada di 'sub'
    }
    @Transactional
    public void register(RegisterCustomerRequest request) {
        validationService.validate(request);

        
        if (customerRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username tersebut sudah digunakan");
        }

        
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email tersebut sudah digunakan");
        }

        // Simpan ke tabel user dulu
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);

        // Refresh user setelah disimpan
        user = userRepository.findById(user.getUsername()).orElseThrow(() -> 
            new IllegalStateException("User not found after save"));
        // Simpan ke tabel customer
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNum());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customerRepository.save(customer);
    }
    @Override
public Reservation extendReservation(String reservationId, LocalDateTime newStart, LocalDateTime newEnd, String roomName) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

    // Validasi kepemilikan reservasi
    if (!reservation.getUser().getUsername().equals(username)) {
        throw new RuntimeException("You can only extend your own reservation.");
    }

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


    
    public List<Reservation> viewMyReservation(String username) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getUser().getUsername().equals(username))
                .collect(Collectors.toList());
    }
}

