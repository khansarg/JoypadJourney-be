package com.example.joypadjourney.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Payment")
public class Payment {

    @Id
    private String paymentID;

    private double totalPrice;
    private String statusPayment;

    @OneToOne
    @JoinColumn(name = "reservationID", referencedColumnName = "reservationID")
    private Reservation reservation;

    private LocalDateTime timestamp;

    // Getter
    public String getPaymentID(){
        return paymentID;
    }
    public double getTotalPrice(){
        return totalPrice;
    }
    public String getStatusPayment(){
        return statusPayment;
    }
    public Reservation getReservationID(){
        return reservation;
    }
    public LocalDateTime getTimeStamp(){
        return timestamp;
    }
    //setter
    public void setPaymentID(String paymentID){
        this.paymentID=paymentID;
    }
    public void setTotalPrice(double totalPrice){
        this.totalPrice=totalPrice;
    }
    public void setStatusPayment(String statusPayment){
        this.statusPayment=statusPayment;
    }
    public void setReservationID(Reservation reservation){
        this.reservation = reservation;
    }
    public void setTimeStamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
}

