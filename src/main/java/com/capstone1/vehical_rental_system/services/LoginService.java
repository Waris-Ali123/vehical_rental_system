package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.UserCreateDTO;
import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.dtos.UserUpdateDTO;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.mappers.UserMapper;

import org.springframework.http.ResponseEntity;

public interface LoginService {

    UserDTO getUserByEmailAndPass(final String email, final String password);

    User getUserByEmail(final String email);

    User getById(final int id);

    UserDTO storeUser(final UserCreateDTO userMapper);

    boolean isAdmin(final String alreadyAdminEmail);

    ResponseEntity<?> getAllUsers(final String email);

    ResponseEntity<String> deletingUser(final UserDTO userToDelete);

    ResponseEntity<?> updatingExistingUser(final int id, final UserUpdateDTO userDetailstoUpdate);

    ResponseEntity<?> searching(final String keyword);
}