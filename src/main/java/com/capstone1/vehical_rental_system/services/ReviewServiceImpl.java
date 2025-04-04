package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
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

    public ReviewServiceImpl(LoginService loginService, VehicleService vehicleService, ReviewRepo reviewRepo) {
        this.loginService = loginService;
        this.vehicleService = vehicleService;
        this.reviewRepo = reviewRepo;
    }

    @Override
    public ResponseEntity<Review> addReview(final String email, final String regNo, final String rating, final String feedback) {
        try {
            final User user = loginService.getUserByEmail(email);
            final Vehicle vehicle = vehicleService.getByRegistrationNumber(regNo);
            final Review review = new Review();
            review.setUser(user);
            review.setVehicle(vehicle);
            review.setRating(Integer.parseInt(rating));
            review.setReviewTime(LocalDateTime.now());
            review.setFeedback(feedback);
            final Review savedReview = reviewRepo.save(review);
            user.addReview(savedReview);
            vehicle.addReview(savedReview);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedReview);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Review>> getReview(final String regNo) {
        try {
            final Vehicle vehicle = vehicleService.getByRegistrationNumber(regNo);
            final List<Review> reviews = reviewRepo.findByVehicle(vehicle);
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public ResponseEntity<List<Review>> getReviewsByEmail(final String email) {
        try {
            final User user = loginService.getUserByEmail(email);
            final List<Review> reviews = reviewRepo.findByUser(user);
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public ResponseEntity<Review> updateReview(final String email, final String regNo, final String rating, final String feedback) {
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
            return ResponseEntity.ok().body(updatedReview);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<Review>> getAllReviews(final String email) {
        try {
            if (loginService.isAdmin(email)) {
                return ResponseEntity.ok().body(reviewRepo.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Review>> searching(final String keyword) {
        try {
            final List<Review> reviews = reviewRepo.SearchingByKeyword(keyword);
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Review>> getTopReviews() {
        try {
            final List<Review> topReviews = reviewRepo.findByRatingGreaterThanEqual(5);
            return ResponseEntity.ok().body(topReviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}