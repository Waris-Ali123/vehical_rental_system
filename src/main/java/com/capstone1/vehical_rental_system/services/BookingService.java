package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.entities.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookingService {

    ResponseEntity<?> addBooking(final String email, final String registrationNumber, final String startDate, final String endDate);

    ResponseEntity<?> getBookings(final String email);

    ResponseEntity<?> getBookingsByRegistrationNumber(final String registrationNumber);

    ResponseEntity<List<BookingDTO>> getAllBookings(final String email);

    ResponseEntity<String> cancelBooking(final String password,final int id);

    ResponseEntity<List<BookingDTO>> searchBookings(final String keyword);
}
