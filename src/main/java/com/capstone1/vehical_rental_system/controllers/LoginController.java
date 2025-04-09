package com.capstone1.vehical_rental_system.controllers;

import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<User> getUserByEmailAndPassword(
            @RequestParam final String email,
            @RequestParam final String password) {
        try {
            final User u1 = loginService.getUserByEmailAndPass(email, password);
            if (u1 != null) {
                return ResponseEntity.ok().body(u1);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> signUp(final @RequestBody User user) {
        User u1;
        try {
            u1 = loginService.storeUser(user);
            if (u1 != null) {
                return ResponseEntity.ok().body(u1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updatingExistingUser(
            final @PathVariable("id") int id,
            final @RequestBody User userDetailsToUpdate) {
        try {
            return loginService.updatingExistingUser(id, userDetailsToUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }

    // Admin Specific functionalities
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam final String email) {
        try {
            return loginService.getAllUsers(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<User>> searching(@PathVariable("keyword") final String keyword) {
        try {
            return loginService.searching(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/delete/{adminEmail}")
    public ResponseEntity<String> deletingUserByAdmin(
            final @PathVariable("adminEmail") String emailAdmin,
            @RequestBody User userToDelete) {
        if (loginService.isAdmin(emailAdmin)) {


            return loginService.deletingUser(userToDelete);
        }
        return ResponseEntity.internalServerError().build();
    }

}
