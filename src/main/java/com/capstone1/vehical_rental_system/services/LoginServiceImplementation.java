package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImplementation implements LoginService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    @Autowired
    public LoginServiceImplementation(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public User getUserByEmailAndPass(final String email, final String password) {
        final User user = userRepo.findUserByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserByEmail(final String email) {
        return userRepo.findUserByEmail(email).orElse(null);
    }

    @Override
    public User getById(final int id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User storeUser(final User user) {
        //always setting the incoming user as user thus no one can create admin by changing the frontend code
        user.setRole(User.Role.USER);
        //Encoding the password before storing
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public boolean isAdmin(final String alreadyAdminEmail) {
        try {
            final User alreadyAdmin = getUserByEmail(alreadyAdminEmail);
            return alreadyAdmin != null && alreadyAdmin.getRole() == User.Role.ADMIN;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<?> getAllUsers(final String email) {
        try {
            if (isAdmin(email)) {
                final List<User> users = userRepo.findAll();
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only admin can see all users");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong while fetching all users");
        }
    }

    public ResponseEntity<String> deletingUser(User userToDelete) {
        try {
            userRepo.delete(userToDelete);
            return ResponseEntity.ok("User deleted Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to delete the user");
        }
    }

    public ResponseEntity<?> updatingExistingUser(final int id, final User userDetailsToUpdate) {
        try {
            final User user = getById(id);
            user.setName(userDetailsToUpdate.getName());
            user.setEmail(userDetailsToUpdate.getEmail());
            user.setContactNumber(userDetailsToUpdate.getContactNumber());
            final User updatedUser = userRepo.save(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to update existing user");
        }
    }

    @Override
    public ResponseEntity<?> searching(final String keyword) {
        try {

            final List<User> users = userRepo.SearchingByKeyword(keyword);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Nothing found");
        }
    }
}