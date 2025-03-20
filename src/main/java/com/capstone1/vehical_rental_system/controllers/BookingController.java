package com.capstone1.vehical_rental_system.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.services.BookingService;


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
        return bookingService.getAllBookings(email);
    }


    //Updating booking details or managing
    @PutMapping("/updatingBooking/{booking_id}")
    public ResponseEntity<Booking> putMethodName(@PathVariable("booking_id") String booking_id,@RequestBody Booking bookingModified) {
        bookingService.updateBooking(booking_id,bookingModified);
        
        return ResponseEntity.ok().build();
    }

    //removing or cancel the bgitookings.
    @PutMapping("/cancelBooking/{booking_id}")
    public ResponseEntity<String> putMethodName(@PathVariable int booking_id) {
        return bookingService.cancleBooking(booking_id);
    }

    

    //adding the address or location of vehicles.
    
    
    
    

    //TotalRevenueGotByPurchasing
    
}
