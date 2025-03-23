package com.capstone1.vehical_rental_system.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.Vehicle;

public interface ReviewService {
    
    public ResponseEntity<Review> addReview(String email,String registrationNumber,String rating,String feedback);
    
    public ResponseEntity<List<Review>> getReview(String registrationNumber);
    
    public ResponseEntity<Review> updateReview(String email,String registrationNumber,String rating,String feedback);

    public ResponseEntity<List<Review>> getAllReviews( String email);

    public ResponseEntity<List<Review>> searching(String keyword);
}

    //will later try to update the review.
