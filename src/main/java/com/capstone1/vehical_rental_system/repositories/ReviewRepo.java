package com.capstone1.vehical_rental_system.repositories;

import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {

    List<Review> findByUser(User user);

    List<Review> findByVehicle(Vehicle vehicle);

    @Query("SELECT r FROM Review r WHERE r.vehicle = :vehicle AND r.user = :user")
    Review findByVehicleAndUser(Vehicle vehicle, User user);

    @Query(value = "Select R from Review R where " +
            " Lower(user.name) like Lower(Concat('%',:keyword,'%')) Or  " +
            " Lower(vehicle.name) like Lower(Concat('%',:keyword,'%')) Or " +
            " CAST(rating AS string)  = :keyword Or " +
            " Lower(feedback) like Lower(Concat('%',:keyword,'%'))"
    )
    List<Review> SearchingByKeyword(String keyword);

    List<Review> findByRatingGreaterThanEqual(int rating);
}
