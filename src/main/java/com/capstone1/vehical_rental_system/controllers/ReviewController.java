package com.capstone1.vehical_rental_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.dtos.ReviewCreateDTO;
import com.capstone1.vehical_rental_system.dtos.ReviewDTO;
import com.capstone1.vehical_rental_system.services.ReviewService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<?> addingReview(
            @RequestBody @Valid final ReviewCreateDTO reviewCreateDTO) {
        try {
            return reviewService.addReview(reviewCreateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getReviewsByVehicle") // Changed to PathVariable
    public ResponseEntity<?> getReviewsByVehicle(@RequestParam final String registration_number) {
        try {

            return reviewService.getReviews(registration_number);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong!");
        }
    }

    @GetMapping("/getReviewsByUser")  // Changed to PathVariable
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@RequestParam("email") final String email) {
        try {
            return reviewService.getReviewsByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")  // Changed to PathVariable
    public ResponseEntity<List<ReviewDTO>> searchingReview(@PathVariable("keyword") final String keyword) {
        try {
            return reviewService.searching(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getAllReviews") //changed to pathvariable
    public ResponseEntity<List<ReviewDTO>> getAllReviews(@RequestParam final String email) {
        return reviewService.getAllReviews(email);
    }

    @GetMapping("/getTopReviews")
    public ResponseEntity<List<ReviewDTO>> getTopReviews() {
        try {
            return reviewService.getTopReviews();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
