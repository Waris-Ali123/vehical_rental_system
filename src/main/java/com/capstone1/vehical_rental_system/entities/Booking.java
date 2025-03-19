package com.capstone1.vehical_rental_system.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;

@Entity
public class Booking {

    public enum BookingStatus {
        CONFIRMED,CANCELED;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int booking_id;

    private LocalDateTime bookingTime;

    

    private LocalDate startDate;

    private LocalDate endDate;

    private double totalPrice;


    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id",nullable = false)
    private Vehicle vehicle;


    @Version
    private int version; 


    public Booking(){}

    public Booking(LocalDate startDate, LocalDate endDate, double totalPrice, BookingStatus bookingStatus) {
        this.bookingTime = LocalDateTime.now();
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
    }         //just for checking purpose

    public int getBooking_id() {
        return booking_id;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getBooking_status() {
        return bookingStatus;
    }

    public void setBooking_status(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
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

    @Override
    public String toString() {
        return "Booking [booking_id=" + booking_id + ", startDate=" + startDate + ", endDate=" + endDate
                + ", totalPrice=" + totalPrice + ", bookingStatus=" + bookingStatus + "]";
    }

    

}
