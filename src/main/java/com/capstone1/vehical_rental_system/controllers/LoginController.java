package com.capstone1.vehical_rental_system.controllers;

import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<?> getUserByEmailAndPassword(
            @RequestParam final String email,
            @RequestParam final String password) {
        try {
            final User u1 = loginService.getUserByEmailAndPass(email, password);
            if (u1 != null) {
                return ResponseEntity.ok().body(u1);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Username or Password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something Went wrong");
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(final @RequestBody User user) {
        User u1;
        try {
            u1 = loginService.storeUser(user);
            if (u1 != null) {
                return ResponseEntity.ok().body(u1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add the user");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatingExistingUser(
            final @PathVariable("id") int id,
            final @RequestBody User userDetailsToUpdate) {
        try {
            return loginService.updatingExistingUser(id, userDetailsToUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().body("Failed to update user");
    }

    // Admin Specific functionalities
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(@RequestParam final String email) {
        try {
            return loginService.getAllUsers(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().body("Something went wrong");
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")
    public ResponseEntity<?> searching(@PathVariable("keyword") final String keyword) {
        try {
            return loginService.searching(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @DeleteMapping("/delete/{adminEmail}")
    public ResponseEntity<String> deletingUserByAdmin(
            final @PathVariable("adminEmail") String emailAdmin,
            @RequestBody User userToDelete) {

        try {
            if (loginService.isAdmin(emailAdmin)) {
                return loginService.deletingUser(userToDelete);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only admin can delete the user");
            }
        }
    
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }      
    
    }

}
