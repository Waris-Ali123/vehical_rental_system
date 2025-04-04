package com.capstone1.vehical_rental_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Review> addingReview(@RequestParam String email,@RequestParam String registration_number,@RequestParam String rating,@RequestParam String feedback){
        try {
            return reviewService.addReview(email, registration_number, rating, feedback);          
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getReviewsByVehicle")
    public ResponseEntity<List<Review>> getReviewsByVehicle(@RequestParam String registration_number) {
        try {

            return reviewService.getReview(registration_number);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
       
    }

    @GetMapping("/getReviewsByUser")
    public ResponseEntity<List<Review>> getReviewsByUser(@RequestParam String email) {
        try {

            return reviewService.getReviewsByEmail(email);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
       
    }

    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<Review>> searchingReview(@PathVariable("keyword") String keyword) {
        try{
            return reviewService.searching(keyword);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }


    @GetMapping("/getAllReviews")
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam String email) {
        return reviewService.getAllReviews(email);
    }
    

    @GetMapping("/getTopReviews")
    public ResponseEntity<List<Review>> getTopReviews(){
        try {
            return reviewService.getTopReviews();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
        
}
