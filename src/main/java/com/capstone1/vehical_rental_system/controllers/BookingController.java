package com.capstone1.vehical_rental_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.dtos.BookingCreateDTO;
import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.services.BookingService;
import com.capstone1.vehical_rental_system.services.LoginService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private LoginService loginService;

    // Adding the Booking
    @PostMapping("/add")
    public ResponseEntity<?> addingBooking(
            @RequestBody @Valid BookingCreateDTO bookingDTO) {
        try {
            return bookingService.addBooking(
            bookingDTO.getEmail(),
            bookingDTO.getRegistrationNumber(),
            bookingDTO.getStartDate().toString(),
            bookingDTO.getEndDate().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<?> getBookingHistoryByMail(@RequestParam("email") final String email) {
        return bookingService.getBookings(email);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getByRegistrationNumber")
    public ResponseEntity<?> getBookingHistoryByVeqhicle(
            @RequestParam("registration_number") final String registration_number) {
        return bookingService.getBookingsByRegistrationNumber(registration_number);
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(@RequestParam("email") final String email) {
        return bookingService.getAllBookings(email);
    }

    // Removing or canceling the bookings
    @PutMapping("/cancelBooking/{password}/{id}")
    public ResponseEntity<String> cancelBooking(
            @PathVariable("password") final String password,
            @PathVariable("id") final int bookingId) {

        try {
            
            return bookingService.cancelBooking(password, bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong!");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<BookingDTO>> searching(@PathVariable("keyword") final String keyword) { //made keyword final
        try {
            return bookingService.searchBookings(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }
}
