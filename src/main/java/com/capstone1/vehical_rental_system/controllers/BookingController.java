package com.capstone1.vehical_rental_system.controllers;

import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.services.BookingService;
import com.capstone1.vehical_rental_system.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> addingBooking(
            @RequestParam("email") final String email,
            @RequestParam("registration_number") final String registration_number,
            @RequestParam("startDate") final String startDate,
            @RequestParam("endDate") final String endDate) {
        try {
            return bookingService.addBooking(email, registration_number, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<List<Booking>> getBookingHistoryByMail(@RequestParam("email") final String email) {
        return bookingService.getBookings(email);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getByRegistrationNumber")
    public ResponseEntity<?> getBookingHistoryByVeqhicle(
            @RequestParam("registration_number") final String registration_number) {
        return bookingService.getBookingsByRegistrationNumber(registration_number);
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<Booking>> getAllBookings(@RequestParam("email") final String email) {
        return bookingService.getAllBookings(email);
    }

    // Removing or canceling the bookings
    @PutMapping("/cancelBooking/{password}")
    public ResponseEntity<String> cancelBooking(
            @PathVariable("password") final String password,
            @RequestBody Booking booking) {

        try {
            String bookerEmail = booking.getUser().getEmail();

            UserDTO userAuthenticated = loginService.getUserByEmailAndPass(bookerEmail,password);

            if (userAuthenticated != null) {
                final int booking_id = booking.getBooking_id();
                return bookingService.cancelBooking(booking_id);
            } else {
                System.out.println("This user has no access to cancel the booking of another user");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong!");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<Booking>> searching(@PathVariable("keyword") final String keyword) { //made keyword final
        try {
            return bookingService.searchBookings(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }
}
