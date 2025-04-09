package com.capstone1.vehical_rental_system.controllers;

import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Review> addingReview(
            @RequestParam("email") final String email,
            @RequestParam("registration_number") final String registration_number,
            @RequestParam("rating") final String rating,
            @RequestParam("feedback") final String feedback) {
        try {
            return reviewService.addReview(email, registration_number, rating, feedback);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getReviewsByVehicle") // Changed to PathVariable
    public ResponseEntity<List<Review>> getReviewsByVehicle(@RequestParam final String registration_number) {
        try {

            return reviewService.getReview(registration_number);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getReviewsByUser")  // Changed to PathVariable
    public ResponseEntity<List<Review>> getReviewsByUser(@RequestParam("email") final String email) {
        try {
            return reviewService.getReviewsByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")  // Changed to PathVariable
    public ResponseEntity<List<Review>> searchingReview(@PathVariable("keyword") final String keyword) {
        try {
            return reviewService.searching(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getAllReviews") //changed to pathvariable
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam final String email) {
        return reviewService.getAllReviews(email);
    }

    @GetMapping("/getTopReviews")
    public ResponseEntity<List<Review>> getTopReviews() {
        try {
            return reviewService.getTopReviews();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
