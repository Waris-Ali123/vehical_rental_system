package com.capstone1.vehical_rental_system.dtos;

import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDTO {
    private int bookingId;
    private LocalDateTime bookingTime;
    @NotNull(message = "Start date is required.")
    @FutureOrPresent(message = "Start date must be today or in the future.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    @FutureOrPresent(message = "End date must be today or in the future.")
    private LocalDate endDate;

    @Min(value = 0, message = "Total price must be positive.")
    private double totalPrice;

    @NotNull(message = "Booking status is required.")
    private BookingStatus bookingStatus;

    @NotNull(message = "User is required.")
    private UserDTO user;

    @NotNull(message = "Vehicle is required.")
    private VehicleDTO vehicle;
    
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
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
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }
    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
    public VehicleDTO getVehicle() {
        return vehicle;
    }
    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    // Getters and Setters

    
}
