package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Booking;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookingService {

    public ResponseEntity<String> addBooking(final String email, final String registrationNumber, final String startDate, final String endDate);

    public ResponseEntity<List<Booking>> getBookings(final String email);

    public ResponseEntity<List<Booking>> getBookingsByRegistrationNumber(final String registrationNumber);

    public ResponseEntity<List<Booking>> getAllBookings(final String email);

    public ResponseEntity<String> cancelBooking(final int booking_id);

    public ResponseEntity<List<Booking>> searchBookings(final String keyword);
}
