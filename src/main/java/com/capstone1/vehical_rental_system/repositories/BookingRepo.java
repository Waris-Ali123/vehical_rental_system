package com.capstone1.vehical_rental_system.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
@Repository
public interface BookingRepo extends JpaRepository<Booking,Integer> {

    List<Booking> findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndBookingStatus(Vehicle vehicle,LocalDate endDate,LocalDate startDate,BookingStatus bookingStatus);

    List<Booking> findByUser(User user);

    @Query(value = "Select B from Booking B where "+
                    " Lower(user.name) like Lower(Concat('%',:keyword,'%')) Or  " + 
                    " Lower(vehicle.name) like Lower(Concat('%',:keyword,'%')) Or " + 
                    " cast(B.totalPrice AS string) like CONCAT('%', :keyword, '%') Or  " + 
                    " Lower(bookingStatus) like Lower(Concat('%',:keyword,'%'))" 
        )
    public List<Booking> SearchingByKeyword(String keyword);

    
}

    
