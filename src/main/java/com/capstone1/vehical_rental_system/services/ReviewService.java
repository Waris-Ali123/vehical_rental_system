package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.ReviewCreateDTO;
import com.capstone1.vehical_rental_system.dtos.ReviewDTO;

import java.util.List;

public interface ReviewService {

    ReviewDTO addReview(final ReviewCreateDTO reviewCreateDTO);

    List<ReviewDTO> getReviews(final String registrationNumber);

    ReviewDTO updateReview(final String email, final String registrationNumber, final String rating, final String feedback);

    List<ReviewDTO> getAllReviews(final String email);

    List<ReviewDTO> searching(final String keyword);

    List<ReviewDTO> getReviewsByEmail(final String email);

    List<ReviewDTO> getTopReviews();
}