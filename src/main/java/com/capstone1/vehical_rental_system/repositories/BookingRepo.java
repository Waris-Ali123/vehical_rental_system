package com.capstone1.vehical_rental_system.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;

public interface BookingRepo extends JpaRepository<Booking,Integer> {

    List<Booking> findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Vehicle vehicle,LocalDate endDate,LocalDate startDate);

    List<Booking> findByUser(User user);

    
}

    
