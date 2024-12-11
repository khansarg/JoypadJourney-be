package com.example.joypadjourney.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


//BELUM TAU BAKAL DIPAKE APA NGGA
@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false, unique = true)
    private String token; // Token sesi (JWT atau UUID)

    @Column(nullable = false)
    private String username; // User yang terkait

    @Column(nullable = false)
    private String role; // Role user (CUSTOMER atau ADMIN)

    @Column(nullable = false)
    private Date createdAt; // Waktu dibuat

    @Column(nullable = false)
    private Date expiresAt; // Waktu kedaluwarsa

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private User user;
}
