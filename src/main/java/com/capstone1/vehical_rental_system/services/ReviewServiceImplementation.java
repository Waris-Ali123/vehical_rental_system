package com.capstone1.vehical_rental_system.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.repositories.ReviewRepo;

@Service
public class ReviewServiceImplementation implements ReviewService{
    
    @Autowired
    LoginService loginService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    ReviewRepo reviewRepo;


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

            Review savedReview = reviewRepo.save(review);
            user.addReview(savedReview);
            vehicle.addReview(savedReview);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedReview);
            
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
    public ResponseEntity<List<Review>> getReviewsByEmail(String email) {
        try {
            User user = loginService.getUserByEmail(email);
            List<Review> reviews = reviewRepo.findByUser(user);
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

            user.removeReview(review);
            vehicle.removeReview(review);

            review.setFeedback(feedback);
            review.setRating(Integer.parseInt(rating));
            review.setReviewTime(LocalDateTime.now());
            Review updatedReview = reviewRepo.save(review);

            user.addReview(updatedReview);
            vehicle.removeReview(updatedReview);

            return ResponseEntity.ok().body(updatedReview);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<Review>> getAllReviews(String email) {
        try {
            if(loginService.isAdmin(email)){
                return ResponseEntity.ok().body(reviewRepo.findAll());
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.internalServerError().build();
        }
    }


    @Override
    public ResponseEntity<List<Review>> searching(String keyword) {
        try {
            List<Review> reviews =  reviewRepo.SearchingByKeyword(keyword);

            //if data not found we give not found status
            if(reviews.size()<=0)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.internalServerError().build();
        }
    }



   
}
