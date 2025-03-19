package com.capstone1.vehical_rental_system.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.services.BookingService;
import com.capstone1.vehical_rental_system.services.LoginService;
import com.capstone1.vehical_rental_system.services.VehicleService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService ;
   


    //Adding the Booking
    @PostMapping("/add")
    public ResponseEntity<String> AddingBooking(@RequestParam String email,@RequestParam String registration_no,
        @RequestParam String startDate,  @RequestParam String endDate) {
        try {
            return bookingService.addBooking(email,registration_no,startDate,endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");
        
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<List<Booking>> getBookingHistoryByMail(@RequestParam String email) {
        return bookingService.getBookings(email);
    }


    @GetMapping("/getAllBookings")
    public ResponseEntity<List<Booking>> getMethodName(@RequestParam String email) {
        return bookingService.getAllBooks(email);
    }


    //Updating booking details or managing

    //removing or cancel the bookings.

    //adding the address or location of vehicles.
    
    
    
    

    //TotalRevenueGotByPurchasing
    
}
