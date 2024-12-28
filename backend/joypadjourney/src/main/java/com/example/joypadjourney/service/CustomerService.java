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
         // Ambil username dari SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Ambil reservasi berdasarkan ID
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Validasi kepemilikan reservasi
        if (!reservation.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only extend your own reservation.");
        }

        // Validasi waktu (hanya bisa diperpanjang 30 menit sebelum berakhir)
        LocalDateTime now = LocalDateTime.now();
        if (reservation.getEndDateTime().isAfter(now.plusMinutes(30))) {
            throw new RuntimeException("You can only extend 30 minutes before the reservation ends.");
        }

        // Tetapkan newStart sebagai end lama
        newStart = reservation.getEndDateTime();
        reservation.setStartDateTime(newStart);
        reservation.setEndDateTime(newEnd);
        Room room = roomRepository.findById(roomName)
                .orElseThrow(() -> new RuntimeException("Room not found")); 
        // Perbarui data reservasi
        reservation.setRoom(room);
        // Hitung ulang harga berdasarkan waktu baru
        double newPrice = reservationService.calculateTotalPrice(newStart, newEnd, reservation.getRoom().getPricePerHour());
        
        
        reservation.setPrice(newPrice);
        


        // Perbarui pembayaran terkait
        Payment payment = paymentRepository.findByReservation_ReservationID(reservationId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setTotalPrice(newPrice);

        // Simpan perubahan
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

