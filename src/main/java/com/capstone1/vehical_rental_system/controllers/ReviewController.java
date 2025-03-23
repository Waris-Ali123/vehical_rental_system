package com.capstone1.vehical_rental_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.Review;
import com.capstone1.vehical_rental_system.services.ReviewService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Review> addingReview(@RequestParam String email,@RequestParam String registrationNumber,@RequestParam String rating,@RequestParam String feedback){
        try {
            return reviewService.addReview(email, registrationNumber, rating, feedback);          
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getReviews")
    public ResponseEntity<List<Review>> getReviews(@RequestParam String registrationNumber) {
        try {

            return reviewService.getReview(registrationNumber);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
       
    }


    @GetMapping("/getAllReviews")
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam String email) {
        return reviewService.getAllReviews(email);
    }
    

    @PutMapping("/update")
    public ResponseEntity<Review> putMethodName(@RequestParam String email,@RequestParam String registrationNumber, @RequestParam String rating,@RequestParam String feedback ) {
        try {
            return reviewService.updateReview(email, registrationNumber, rating, feedback);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
    

    
}
