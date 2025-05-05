package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.UserCreateDTO;
import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.dtos.UserUpdateDTO;
import com.capstone1.vehical_rental_system.entities.User;

import java.util.List;

public interface LoginService {

    UserDTO getUserByEmailAndPass(final String email, final String password);

    User getUserByEmail(final String email);

    UserDTO storeUser(final UserCreateDTO userMapper);

    boolean isAdmin(final String alreadyAdminEmail);

    List<UserDTO> getAllUsers(final String email);

    void deletingUser(final UserDTO userToDelete);

    UserDTO updatingExistingUser(final int id, final UserUpdateDTO userDetailstoUpdate);

    List<UserDTO> searching(final String keyword);
}