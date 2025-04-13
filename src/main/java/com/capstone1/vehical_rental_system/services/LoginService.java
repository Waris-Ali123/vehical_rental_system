package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.User;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    User getUserByEmailAndPass(final String email, final String password);

    User getUserByEmail(final String email);

    User getById(final int id);

    User storeUser(final User user);

    boolean isAdmin(final String alreadyAdminEmail);

    ResponseEntity<?> getAllUsers(final String email);

    ResponseEntity<String> deletingUser(final User userToDelete);

    ResponseEntity<?> updatingExistingUser(final int id, final User userDetailstoUpdate);

    ResponseEntity<?> searching(final String keyword);
}