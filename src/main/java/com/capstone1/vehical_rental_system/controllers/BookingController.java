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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.services.BookingService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/booking")
public class BookingController {


    @Autowired
    BookingService bookingService ;


    
    //Adding the Booking
    @PostMapping("/add")
    public ResponseEntity<String> AddingBooking(@RequestParam String email,@RequestParam String registration_number,
        @RequestParam String startDate,  @RequestParam String endDate) {
        try {
            return bookingService.addBooking(email,registration_number,startDate,endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");
        
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<List<Booking>> getBookingHistoryByMail(@RequestParam String email) {
        return bookingService.getBookings(email);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getByRegistrationNumber")
    public ResponseEntity<List<Booking>> getBookingHistoryByVeqhicle(@RequestParam String registration_number) {
        return bookingService.getBookingsByRegistrationNumber(registration_number);
    }


    @GetMapping("/getAllBookings")
    public ResponseEntity<List<Booking>> getMethodName(@RequestParam String email) {
        return bookingService.getAllBookings(email);
    }


    //removing or cancel the bgitookings.
    @PutMapping("/cancelBooking/{booking_id}")
    public ResponseEntity<String> putMethodName(@PathVariable int booking_id) {
        return bookingService.cancleBooking(booking_id);
    }


    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<Booking>> searching(@PathVariable("keyword") String keyword) {
        try{
            return bookingService.searching(keyword);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



    
}
