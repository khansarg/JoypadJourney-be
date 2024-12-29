package com.example.joypadjourney.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
     @Column(name = "paymentid", nullable = false)
    private String paymentId;
    @Column(name = "total_price", nullable = false)
    private double totalPrice;
    @Column(name = "status_payment", nullable = false)
    private String statusPayment; // PENDING, SUCCESS, CANCELED

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservationID", referencedColumnName = "reservationID")
    private Reservation reservation;
}
