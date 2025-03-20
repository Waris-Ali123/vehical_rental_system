package com.capstone1.vehical_rental_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review,Integer>{

    List<Review> findByVehicle(Vehicle vehicle);

    Review findByVehicleAndUser(Vehicle vehicle,User user);
}
