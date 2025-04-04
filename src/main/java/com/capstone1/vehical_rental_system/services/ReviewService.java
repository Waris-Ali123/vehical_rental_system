package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Review;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {

    public ResponseEntity<Review> addReview(final String email, final String registrationNumber, final String rating, final String feedback);

    public ResponseEntity<List<Review>> getReview(final String registrationNumber);

    public ResponseEntity<Review> updateReview(final String email, final String registrationNumber, final String rating, final String feedback);

    public ResponseEntity<List<Review>> getAllReviews(final String email);

    public ResponseEntity<List<Review>> searching(final String keyword);

    public ResponseEntity<List<Review>> getReviewsByEmail(final String email);

    public ResponseEntity<List<Review>> getTopReviews();
}
