package com.capstone1.vehical_rental_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.repositories.BookingRepo;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;


@Service
public class BookingServiceImplementation implements BookingService {
    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired
    BookingRepo bookingRepo;

    public void addBooking(Booking booking){
        booking.getVehicle().addBooking(booking);
        booking.getUser().addBooking(booking);
        bookingRepo.save(booking);
    }
    
}
