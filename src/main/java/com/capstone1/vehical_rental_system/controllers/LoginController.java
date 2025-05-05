package com.capstone1.vehical_rental_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
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

import com.capstone1.vehical_rental_system.dtos.UserCreateDTO;
import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.dtos.UserUpdateDTO;
import com.capstone1.vehical_rental_system.exceptions.ResourceNotFoundException;
import com.capstone1.vehical_rental_system.services.LoginService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@Validated // Enables validation for @RequestParam and @PathVariable
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public ResponseEntity<UserDTO> getUserByEmailAndPassword(
            @RequestParam @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email,
            @RequestParam @NotBlank(message = "Password cannot be blank") String password) {
        UserDTO user = loginService.getUserByEmailAndPass(email, password);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with this email and password");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserDTO> signUp(final @Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO userDTO = loginService.storeUser(userCreateDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updatingExistingUser(
            final @PathVariable("id") int id,
            final @Valid @RequestBody UserUpdateDTO userDetailsToUpdate) {
        UserDTO updatedUser = loginService.updatingExistingUser(id, userDetailsToUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(
            @RequestParam @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email) {
        return ResponseEntity.ok(loginService.getAllUsers(email));
    }

    @GetMapping("/searching/{keyword}")
    public ResponseEntity<?> searching(
            @PathVariable("keyword") @NotBlank(message = "Keyword cannot be blank") String keyword) {
        return ResponseEntity.ok(loginService.searching(keyword));
    }

    @DeleteMapping("/delete/{adminEmail}")
    public ResponseEntity<String> deletingUserByAdmin(
            final @PathVariable("adminEmail") @NotBlank(message = "Admin email cannot be blank") @Email(message = "Invalid email format") String emailAdmin,
            @Valid @RequestBody UserDTO userToDelete) {
        if (loginService.isAdmin(emailAdmin)) {
            loginService.deletingUser(userToDelete);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            throw new AccessDeniedException("Only admin can delete the user");
        }
    }
}