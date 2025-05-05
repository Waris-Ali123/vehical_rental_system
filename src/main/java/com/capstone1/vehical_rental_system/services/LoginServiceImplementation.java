package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.UserCreateDTO;
import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.dtos.UserUpdateDTO;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.mappers.UserMapper;
import com.capstone1.vehical_rental_system.repositories.UserRepo;
import com.capstone1.vehical_rental_system.exceptions.ResourceNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class LoginServiceImplementation implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImplementation.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public LoginServiceImplementation(PasswordEncoder passwordEncoder, UserRepo userRepo, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUserByEmailAndPass(final String email, final String password) {
        logger.info("Attempting to authenticate user with email: {}", email);
        final User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new ResourceNotFoundException("User not found with email: " + email);
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Invalid password for user with email: {}", email);
            throw new IllegalArgumentException("Invalid password");
        }

        logger.info("User authenticated successfully with email: {}", email);
        return userMapper.toDto(user);
    }

    public User getUserByEmail(final String email) { // Restored method
        logger.info("Fetching user by email: {}", email);
        return userRepo.findUserByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new ResourceNotFoundException("User not found with email: " + email);
                });
    }

    @Override
    public UserDTO storeUser(final UserCreateDTO dto) {
        logger.info("Storing new user with email: {}", dto.getEmail());
        User user = userMapper.toEntity(dto);
        user.setRole(User.Role.USER);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepo.save(user);
        logger.info("User stored successfully with email: {}", dto.getEmail());
        return userMapper.toDto(savedUser);
    }

    @Override
    public boolean isAdmin(final String alreadyAdminEmail) {
        logger.info("Checking if user with email: {} is an admin", alreadyAdminEmail);
        final User alreadyAdmin = userRepo.findUserByEmail(alreadyAdminEmail)
                .orElseThrow(() -> {
                    logger.error("Admin not found with email: {}", alreadyAdminEmail);
                    return new ResourceNotFoundException("Admin not found with email: " + alreadyAdminEmail);
                });
        boolean isAdmin = alreadyAdmin.getRole() == User.Role.ADMIN;
        logger.info("User with email: {} isAdmin: {}", alreadyAdminEmail, isAdmin);
        return isAdmin;
    }

    @Override
    public List<UserDTO> getAllUsers(final String email) {
        logger.info("Fetching all users for admin with email: {}", email);
        if (!isAdmin(email)) {
            logger.warn("Access denied for user with email: {}. Only admin can see all users.", email);
            throw new IllegalArgumentException("Only admin can see all users");
        }
        final List<User> users = userRepo.findAll();
        logger.info("Successfully fetched all users for admin with email: {}", email);
        return userMapper.toDtoList(users);
    }

    @Override
    public void deletingUser(final UserDTO userToDelete) {
        logger.info("Attempting to delete user with ID: {}", userToDelete.getUserId());

        // Fetch the user from the database
        final User user = userRepo.findById(userToDelete.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userToDelete.getUserId());
                    return new ResourceNotFoundException("User not found with ID: " + userToDelete.getUserId());
                });

        // Check if the user is an admin
        if (user.getRole() == User.Role.ADMIN) {
            logger.warn("Attempt to delete admin user with ID: {}", userToDelete.getUserId());
            throw new IllegalArgumentException("Admin cannot be deleted.");
        }

        // Proceed with deletion if the user is not an admin
        userRepo.delete(user);
        logger.info("User deleted successfully with ID: {}", userToDelete.getUserId());
    }

    @Override
    public UserDTO updatingExistingUser(final int id, final UserUpdateDTO userDetailsToUpdate) {
        logger.info("Attempting to update user with ID: {}", id);
        final User user = userRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        userMapper.updateUserFromDto(userDetailsToUpdate, user);
        final User updatedUser = userRepo.save(user);
        logger.info("User updated successfully with ID: {}", id);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public List<UserDTO> searching(final String keyword) {
        logger.info("Searching users with keyword: {}", keyword);
        final List<User> users = userRepo.SearchingByKeyword(keyword);
        logger.info("Found {} users matching keyword: {}", users.size(), keyword);
        return userMapper.toDtoList(users);
    }
}