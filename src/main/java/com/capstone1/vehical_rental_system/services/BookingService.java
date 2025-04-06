package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Booking;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookingService {

    ResponseEntity<String> addBooking(final String email, final String registrationNumber, final String startDate, final String endDate);

    ResponseEntity<List<Booking>> getBookings(final String email);

    ResponseEntity<List<Booking>> getBookingsByRegistrationNumber(final String registrationNumber);

    ResponseEntity<List<Booking>> getAllBookings(final String email);

    ResponseEntity<String> cancelBooking(final int booking_id);

    ResponseEntity<List<Booking>> searchBookings(final String keyword);
}
