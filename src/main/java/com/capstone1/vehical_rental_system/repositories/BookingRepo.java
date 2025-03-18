package com.capstone1.vehical_rental_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone1.vehical_rental_system.entities.Booking;

public interface BookingRepo extends JpaRepository<Booking,Integer> {

    

    
}
