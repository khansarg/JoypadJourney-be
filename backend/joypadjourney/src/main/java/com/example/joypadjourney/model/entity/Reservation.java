package com.example.joypadjourney.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Reservation")
public class Reservation {

    @Id
    private String reservationID;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "roomName", referencedColumnName = "roomName")
    private Room room;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status;
    private double price;

    //getter
    public String getReservationID(){
        return reservationID;
    }
    public User getUser(){
        return user;
    }
    public Room getRoom(){
        return room;
    }
    public LocalDateTime getStartDateTime(){
        return startDateTime;
    }
    public LocalDateTime getEndDateTime(){
        return endDateTime;
    }
    public String getStatus(){
        return status;
    }
    public double getPrice(){
        return price;
    }
    //Setter
    public void setReservationID(String reservationID){
        this.reservationID = reservationID;
    }
    public void setUser(User user){
        this.user=user;
    }
    public void setRoom(Room room){
        this.room=room;
    }
    public void setStartDateTime(LocalDateTime startDateTime){
        this.startDateTime=startDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime){
        this.endDateTime=endDateTime;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setPrice(double price){
        this.price=price;
    }
}

