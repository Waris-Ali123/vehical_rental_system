package com.capstone1.vehical_rental_system.entities;

import java.sql.Date;

import org.hibernate.annotations.ManyToAny;

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

    private Date startDate;

    private Date endDate;

    private double totalPrice;


    @Enumerated(EnumType.STRING)
    private BookingStatus booking_status;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id",nullable = false)
    private Vehicle vehicle;


    @Version
    private int version; 



    public Booking(){}

    public Booking(Date startDate, Date endDate, double totalPrice, BookingStatus booking_status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.booking_status = booking_status;
    }         //just for checking purpose

    public int getBooking_id() {
        return booking_id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(BookingStatus booking_status) {
        this.booking_status = booking_status;
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
                + ", totalPrice=" + totalPrice + ", booking_status=" + booking_status + "]";
    }

    

}
