package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.ReviewCreateDTO;
import com.capstone1.vehical_rental_system.dtos.ReviewDTO;
import com.capstone1.vehical_rental_system.entities.Review;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {

    ResponseEntity<?> addReview(final ReviewCreateDTO reviewCreateDTO);

    ResponseEntity<?> getReviews(final String registrationNumber);

    ResponseEntity<?> updateReview(final String email, final String registrationNumber, final String rating, final String feedback);

    ResponseEntity<List<ReviewDTO>> getAllReviews(final String email);

    ResponseEntity<List<ReviewDTO>> searching(final String keyword);

    ResponseEntity<List<ReviewDTO>> getReviewsByEmail(final String email);

    ResponseEntity<List<ReviewDTO>> getTopReviews();
}
