package com.example.joypadjourney.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "Payment")
public class Payment {

    @Id
    @NotNull
    private String paymentid;

    private double totalPrice;
    private String statusPayment;

    @OneToOne
    @JoinColumn(name = "reservationID", referencedColumnName = "reservationID")
    private Reservation reservation;

    private LocalDateTime timestamp;

}

