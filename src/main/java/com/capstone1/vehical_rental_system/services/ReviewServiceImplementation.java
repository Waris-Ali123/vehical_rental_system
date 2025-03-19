package com.capstone1.vehical_rental_system.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.capstone1.vehical_rental_system.controllers.BookingController;
import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.repositories.BookingRepo;
import com.capstone1.vehical_rental_system.repositories.ReviewRepo;

@Service
public class ReviewServiceImplementation implements ReviewService{

    private final BookingRepo bookingRepo;

    private final BookingController bookingController;
    
    @Autowired
    LoginService loginService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    ReviewRepo reviewRepo;

    ReviewServiceImplementation(BookingController bookingController, BookingRepo bookingRepo) {
        this.bookingController = bookingController;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public ResponseEntity<Review> addReview(String email, String registrationNumber, String rating, String feedback) {
        try {

                //fetching the User;
            User user = loginService.getUserByEmail(email);

            //fetching the Vehicle
            Vehicle vehicle = vehicleService.getByRegistrationNumber(registrationNumber);

            //Creating a Review Object;
            Review review  = new Review();
            review.setUser(user);
            review.setVehicle(vehicle);
            review.setRating(Integer.parseInt(rating));
            review.setReviewTime(LocalDateTime.now());
            review.setFeedback(feedback);

            Review saved = reviewRepo.save(review);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(saved);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
    }

    @Override
    public ResponseEntity<List<Review>> getReview(String registrationNumber) {
        try {
            Vehicle vehicle = vehicleService.getByRegistrationNumber(registrationNumber);
            List<Review> reviews = reviewRepo.findByVehicle(vehicle);
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    
    }

    @Override
    public ResponseEntity<Review> updateReview(String email,String registrationNumber,String rating,String feedback){
        try{
            User user = loginService.getUserByEmail(email);
            Vehicle vehicle = vehicleService.getByRegistrationNumber(registrationNumber);
            Review review = reviewRepo.findByVehicleAndUser(vehicle, user);

            review.setFeedback(feedback);
            review.setRating(Integer.parseInt(rating));
            review.setReviewTime(LocalDateTime.now());
            Review updatedReview = reviewRepo.save(review);
            return ResponseEntity.ok().body(updatedReview);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

   
}
