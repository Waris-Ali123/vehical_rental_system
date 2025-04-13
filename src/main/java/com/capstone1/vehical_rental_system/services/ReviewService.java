package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Review;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {

    ResponseEntity<?> addReview(final String email, final String registrationNumber, final String rating,
                                final String feedback);

    ResponseEntity<?> getReview(final String registrationNumber);

    ResponseEntity<Review> updateReview(final String email, final String registrationNumber, final String rating, final String feedback);

    ResponseEntity<List<Review>> getAllReviews(final String email);

    ResponseEntity<List<Review>> searching(final String keyword);

    ResponseEntity<List<Review>> getReviewsByEmail(final String email);

    ResponseEntity<List<Review>> getTopReviews();
}
