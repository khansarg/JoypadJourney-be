package com.example.joypadjourney.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Review")
public class Review {

    @Id
    private String reviewID;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reservationID", referencedColumnName = "reservationID")
    private Reservation reservation;

    private int rating;
    private String comment;
    private LocalDateTime tanggal;

    // getter
    public User getUser(){
        return user;
    }
    public String getReviewID(){
        return reviewID;
    }
    public Reservation getReservation(){
        return reservation;
    }
    public int getRating(){
        return rating;
    }
    public String getComment(){
        return comment;
    }
    public LocalDateTime getTanggal(){
        return tanggal;
    }
    //setter
    public void setUser(User user){
        this.user = user;
    }
    public void setReviewID(String reviewID){
        this.reviewID = reviewID;
    }
    public void setReservation(Reservation reservation){
        this.reservation=reservation;
    }
    public void setRating(int rating){
        this.rating = rating;
    }
    public void setComment(String comment){
        this.comment=comment;
    }
    public void setTanggal(LocalDateTime tanggal){
        this.tanggal=tanggal;
    }
}

