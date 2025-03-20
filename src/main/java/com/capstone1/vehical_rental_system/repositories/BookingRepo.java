package com.capstone1.vehical_rental_system.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
@Repository
public interface BookingRepo extends JpaRepository<Booking,Integer> {

    List<Booking> findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndBookingStatus(Vehicle vehicle,LocalDate endDate,LocalDate startDate,BookingStatus bookingStatus);

    List<Booking> findByUser(User user);

    
}

    
