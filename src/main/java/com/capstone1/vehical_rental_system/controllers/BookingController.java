package com.capstone1.vehical_rental_system.controllers;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Adding the Booking
    @PostMapping("/add")
    public ResponseEntity<String> addingBooking(
            final String email,
            final String registration_number,
            final String startDate,
            final String endDate) {
        try {
            return bookingService.addBooking(email, registration_number, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<List<Booking>> getBookingHistoryByMail(final String email) {
        return bookingService.getBookings(email);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getByRegistrationNumber")
    public ResponseEntity<List<Booking>> getBookingHistoryByVeqhicle(
            final String registration_number) {
        return bookingService.getBookingsByRegistrationNumber(registration_number);
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<Booking>> getAllBookings(final String email) {
        return bookingService.getAllBookings(email);
    }

    // Removing or canceling the bookings
    @PutMapping("/cancelBooking/{booking_id}")
    public ResponseEntity<String> cancelBooking(final Integer booking_id) {
        return bookingService.cancelBooking(booking_id);
    }

    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<Booking>> searching(final String keyword) {
        try {
            return bookingService.searchBookings(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
