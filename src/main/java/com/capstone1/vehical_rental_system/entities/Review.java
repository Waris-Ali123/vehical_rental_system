package com.capstone1.vehical_rental_system.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    private LocalDateTime reviewTime;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    
    // @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "vehicle_id",nullable = false)
    private Vehicle vehicle;

    @Min(value = 1, message = "Rating can be atleast one")
    @Max(value = 5 , message = "Rating can be atmost five")
    private int rating;

    private String feedback;

    public Review() {
    }

    public long getReviewId() {
        return reviewId;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Review [reviewId=" + reviewId + ", reviewTime=" + reviewTime + ", user=" + user + ", vehicle=" + vehicle
                + ", rating=" + rating + ", feedback=" + feedback + "]";
    }
  
        
}
