package com.capstone1.vehical_rental_system.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.capstone1.vehical_rental_system.entities.Booking;

public interface BookingService {

    public ResponseEntity<String> addBooking(String email, String registration_no ,String startDate,String endDate);

    public ResponseEntity<List<Booking>> getBookings(String email);
    
    public ResponseEntity<List<Booking>> getAllBookings(String email);

    public ResponseEntity<Booking> updateBooking(String bookingId,Booking bookingModified);

    public ResponseEntity<String> cancleBooking(int booking_id);

    
}
