package com.example.joypadjourney.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Payment")
public class Payment {

    @Id
    private String paymentId;

    private double totalPrice;

    private String statusPayment; // PENDING, SUCCESS, CANCELED

    private String qrisUrl; // URL atau kode QRIS dinamis yang digenerate

    private LocalDateTime timestamp; // Waktu pembayaran dibuat

    private LocalDateTime expiryTime; // Waktu expired pembayaran

    @OneToOne
    @JoinColumn(name = "reservationID", referencedColumnName = "reservationID")
    private Reservation reservation;
}
