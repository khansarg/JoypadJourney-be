package com.example.joypadjourney.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Room")
public class Room {

    @Id
    private String roomName;

    private double pricePerHour;
    private boolean status;

    // Getters and Setters
    public String getRoomName(){
        return roomName;
    }
    public double getPricePerHour(){
        return pricePerHour;
    }
    public boolean getStatus(){
        return status;
    }
    public void setRoomName(String roomName){
        this.roomName = roomName;
    }
    public void setPricePerHour(double pricePerHour){
        this.pricePerHour = pricePerHour;
    }
    public void setStatus(boolean status){
        this.status = status;
    }
}

