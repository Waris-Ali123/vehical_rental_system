package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.ReviewCreateDTO;
import com.capstone1.vehical_rental_system.dtos.ReviewDTO;
import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.mappers.ReviewMapper;
import com.capstone1.vehical_rental_system.repositories.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ReviewServiceImpl(LoginService loginService, VehicleService vehicleService, ReviewRepo reviewRepo,ReviewMapper reviewMapper) {
        this.loginService = loginService;
        this.vehicleService = vehicleService;
        this.reviewRepo = reviewRepo;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ResponseEntity<?> addReview(final ReviewCreateDTO reviewCreateDTO) {
        try {
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
            return ResponseEntity.status(HttpStatus.OK).body(reviewMapper.toDTO(updatedReview));
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

        return ResponseEntity.status(HttpStatus.OK).body(reviewMapper.toDTO(savedReview));
 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add review!");
        }
    }

    @Override
    public ResponseEntity<?> getReviews(final String regNo) {
        try {
            final Vehicle vehicle = vehicleService.getByRegistrationNumber(regNo);
            final List<Review> reviews = reviewRepo.findByVehicle(vehicle);

            return ResponseEntity.ok().body(reviewMapper.toDTOList(reviews));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to get all reviews for this vehicle");
        }
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getReviewsByEmail(final String email) {
        try {
            final User user = loginService.getUserByEmail(email);
            final List<Review> reviews = reviewRepo.findByUser(user);
            return ResponseEntity.ok().body(reviewMapper.toDTOList(reviews));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public ResponseEntity<ReviewDTO> updateReview(final String email, final String regNo, final String rating, final String feedback) {
        try {
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
            return ResponseEntity.ok().body(reviewMapper.toDTO(updatedReview));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getAllReviews(final String email) {
        try {
            if (loginService.isAdmin(email)) {
                List<Review> reviews = reviewRepo.findAll();
                return ResponseEntity.ok().body(reviewMapper.toDTOList(reviews));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> searching(final String keyword) {
        try {
            final List<Review> reviews = reviewRepo.SearchingByKeyword(keyword);
            return ResponseEntity.ok().body(reviewMapper.toDTOList(reviews));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getTopReviews() {
        try {
            final List<Review> topReviews = reviewRepo.findByRatingGreaterThanEqual(5);
            return ResponseEntity.ok().body(reviewMapper.toDTOList(topReviews));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}