package com.example.joypadjourney.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
@Table(name = "Review")
public class Review {

    @Id
    @NotNull
    private String reviewid;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reservationID", referencedColumnName = "reservationID")
    private Reservation reservation;

    private int rating;
    private String comment;
    private LocalDateTime tanggal;

}

