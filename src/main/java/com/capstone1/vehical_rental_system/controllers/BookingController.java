package com.capstone1.vehical_rental_system.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/booking")
@Validated // Enables validation for @RequestParam and @PathVariable
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<BookingDTO> addingBooking(@RequestBody @Valid BookingCreateDTO bookingDTO) {
        BookingDTO booking = bookingService.addBooking(
                bookingDTO.getEmail(),
                bookingDTO.getRegistrationNumber(),
                bookingDTO.getStartDate().toString(),
                bookingDTO.getEndDate().toString());
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<List<BookingDTO>> getBookingHistoryByMail(
            @RequestParam @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") final String email) {
        List<BookingDTO> bookings = bookingService.getBookings(email);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/getByRegistrationNumber")
    public ResponseEntity<List<BookingDTO>> getBookingHistoryByVehicle(
            @RequestParam @NotBlank(message = "Registration number cannot be blank") final String registrationNumber) {
        List<BookingDTO> bookings = bookingService.getBookingsByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(
            @RequestParam @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") final String email) {
        List<BookingDTO> bookings = bookingService.getAllBookings(email);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/cancelBooking/{password}/{id}")
    public ResponseEntity<String> cancelBooking(
            @PathVariable("password") @NotBlank(message = "Password cannot be blank") final String password,
            @PathVariable("id") final int bookingId) {
        String response = bookingService.cancelBooking(password, bookingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<BookingDTO>> searching(
            @PathVariable("keyword") @NotBlank(message = "Keyword cannot be blank") final String keyword) {
        List<BookingDTO> bookings = bookingService.searchBookings(keyword);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/searchForUpcomingOrCurrentBookings")
    public List<BookingDTO> searchForUpcomingOrCurrentBookings(
            @RequestParam("vehicleId") int vehicleId,
            @RequestParam("endDate") String endDate,
            @RequestParam("status") String status) {
        // Logic to fetch bookings based on vehicleId, endDate, and status
        return bookingService.searchForUpcomingOrCurrentBookings(vehicleId, LocalDate.parse(endDate), status);
    }
}