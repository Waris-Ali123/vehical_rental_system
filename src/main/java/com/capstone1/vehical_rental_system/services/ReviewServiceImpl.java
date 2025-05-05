package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.ReviewCreateDTO;
import com.capstone1.vehical_rental_system.dtos.ReviewDTO;
import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.mappers.ReviewMapper;
import com.capstone1.vehical_rental_system.repositories.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private final LoginService loginService;

    @Autowired
    private final VehicleService vehicleService;

    @Autowired
    private final ReviewRepo reviewRepo;

    @Autowired
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(LoginService loginService, VehicleService vehicleService, ReviewRepo reviewRepo, ReviewMapper reviewMapper) {
        this.loginService = loginService;
        this.vehicleService = vehicleService;
        this.reviewRepo = reviewRepo;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewDTO addReview(final ReviewCreateDTO reviewCreateDTO) {
        final User user = loginService.getUserByEmail(reviewCreateDTO.getEmail());
        final Vehicle vehicle = vehicleService.getByRegistrationNumber(reviewCreateDTO.getRegistrationNumber());

        // Check if a review already exists for this user and vehicle
        final Review existingReview = reviewRepo.findByVehicleAndUser(vehicle, user);
        if (existingReview != null) {
            // Update the existing review
            existingReview.setRating(reviewCreateDTO.getRating());
            existingReview.setFeedback(reviewCreateDTO.getFeedback());
            existingReview.setReviewTime(LocalDateTime.now());

            final Review updatedReview = reviewRepo.save(existingReview);
            return reviewMapper.toDTO(updatedReview);
        }

        // Create a new review if no existing review is found
        final Review review = new Review();
        review.setUser(user);
        review.setVehicle(vehicle);
        review.setRating(reviewCreateDTO.getRating());
        review.setReviewTime(LocalDateTime.now());
        review.setFeedback(reviewCreateDTO.getFeedback());

        final Review savedReview = reviewRepo.save(review);
        user.addReview(savedReview);
        vehicle.addReview(savedReview);

        return reviewMapper.toDTO(savedReview);
    }

    @Override
    public List<ReviewDTO> getReviews(final String regNo) {
        final Vehicle vehicle = vehicleService.getByRegistrationNumber(regNo);
        final List<Review> reviews = reviewRepo.findByVehicle(vehicle);
        return reviewMapper.toDTOList(reviews);
    }

    @Override
    public List<ReviewDTO> getReviewsByEmail(final String email) {
        final User user = loginService.getUserByEmail(email);
        final List<Review> reviews = reviewRepo.findByUser(user);
        return reviewMapper.toDTOList(reviews);
    }

    @Override
    public ReviewDTO updateReview(final String email, final String regNo, final String rating, final String feedback) {
        final User user = loginService.getUserByEmail(email);
        final Vehicle vehicle = vehicleService.getByRegistrationNumber(regNo);
        final Review review = reviewRepo.findByVehicleAndUser(vehicle, user);
        user.removeReview(review);
        vehicle.removeReview(review);
        review.setFeedback(feedback);
        review.setRating(Integer.parseInt(rating));
        review.setReviewTime(LocalDateTime.now());
        final Review updatedReview = reviewRepo.save(review);
        user.addReview(updatedReview);
        vehicle.addReview(updatedReview);
        return reviewMapper.toDTO(updatedReview);
    }

    @Override
    public List<ReviewDTO> getAllReviews(final String email) {
        if (loginService.isAdmin(email)) {
            List<Review> reviews = reviewRepo.findAll();
            return reviewMapper.toDTOList(reviews);
        } else {
            throw new IllegalArgumentException("Unauthorized access. Only admin can view all reviews.");
        }
    }

    @Override
    public List<ReviewDTO> searching(final String keyword) {
        final List<Review> reviews = reviewRepo.SearchingByKeyword(keyword);
        return reviewMapper.toDTOList(reviews);
    }

    @Override
    public List<ReviewDTO> getTopReviews() {
        final List<Review> topReviews = reviewRepo.findByRatingGreaterThanEqual(5);
        return reviewMapper.toDTOList(topReviews);
    }
}