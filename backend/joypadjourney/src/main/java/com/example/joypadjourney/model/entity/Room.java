package com.example.joypadjourney.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "Room")
public class Room {

    @Id
    @NotNull
    @Size(max=50)
    private String roomName;

    @NotNull
    private double pricePerHour;
    @NotNull
    private boolean status;

}

