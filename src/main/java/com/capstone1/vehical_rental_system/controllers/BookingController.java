package com.capstone1.vehical_rental_system.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.services.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService ;

    //Adding the Booking
    @PostMapping("/add")
    public String AddingBooking(@RequestBody Booking booking) {
        try {

            bookingService.addBooking(booking);
            
            
            return "Added successfully";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Something went wrong!!";
        
    }
    
    
}
