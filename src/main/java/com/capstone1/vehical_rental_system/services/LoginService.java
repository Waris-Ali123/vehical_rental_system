package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoginService {

    User getUserByEmailAndPass(final String email, final String password);

    User getUserByEmail(final String email);

    User getById(final int id);

    User storeUser(final User user);

    boolean isAdmin(final String alreadyAdminEmail);

    ResponseEntity<List<User>> getAllUsers(final String email);

    ResponseEntity<String> deletingUser(final User userToDelete);

    ResponseEntity<User> updatingExistingUser(final int id, final User userDetailstoUpdate);

    ResponseEntity<List<User>> searching(final String keyword);
}