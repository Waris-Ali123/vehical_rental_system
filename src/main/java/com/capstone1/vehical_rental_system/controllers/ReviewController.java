package com.capstone1.vehical_rental_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/review")
@Validated // Enables validation for @RequestParam and @PathVariable
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<ReviewDTO> addingReview(
            @RequestBody @Valid final ReviewCreateDTO reviewCreateDTO) {
        ReviewDTO review = reviewService.addReview(reviewCreateDTO);
        return ResponseEntity.ok(review);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getReviewsByVehicle")
    public ResponseEntity<List<ReviewDTO>> getReviewsByVehicle(
            @RequestParam @NotBlank(message = "Registration number cannot be blank") final String registrationNumber) {
        List<ReviewDTO> reviews = reviewService.getReviews(registrationNumber);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/getReviewsByUser")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(
            @RequestParam @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") final String email) {
        List<ReviewDTO> reviews = reviewService.getReviewsByEmail(email);
        return ResponseEntity.ok(reviews);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<ReviewDTO>> searchingReview(
            @PathVariable("keyword") @NotBlank(message = "Keyword cannot be blank") final String keyword) {
        List<ReviewDTO> reviews = reviewService.searching(keyword);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/getAllReviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews(
            @RequestParam @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") final String email) {
        List<ReviewDTO> reviews = reviewService.getAllReviews(email);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/getTopReviews")
    public ResponseEntity<List<ReviewDTO>> getTopReviews() {
        List<ReviewDTO> reviews = reviewService.getTopReviews();
        return ResponseEntity.ok(reviews);
    }
}