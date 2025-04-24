package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.UserCreateDTO;
import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.dtos.UserUpdateDTO;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.mappers.UserMapper;
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

    private final UserMapper userMapper;

    public LoginServiceImplementation(PasswordEncoder passwordEncoder, UserRepo userRepo,UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUserByEmailAndPass(final String email, final String password) {
        final User user = userRepo.findUserByEmail(email).orElse(null);
        // System.out.println(user);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return userMapper.toDto(user);
        }
        if(!passwordEncoder.matches(password, user.getPassword()))
            System.out.println("password mismatch");
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
    public UserDTO storeUser(final UserCreateDTO dto) {
        //always setting the incoming user as user thus no one can create admin by changing the frontend code
        
        
        User user = userMapper.toEntity(dto);
        user.setRole(User.Role.USER);
        
        // Explicitly encode password here
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        User savedUser = userRepo.save(user);
        return userMapper.toDto(savedUser);
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
                
                return ResponseEntity.ok(userMapper.toDtoList(users));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only admin can see all users");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong while fetching all users");
        }
    }

    public ResponseEntity<String> deletingUser(UserDTO userToDelete) {
        try {
            User user = userRepo.findById(userToDelete.getUserId()).get();
            userRepo.delete(user);
            return ResponseEntity.ok("User deleted Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to delete the user");
        }
    }

    public ResponseEntity<?> updatingExistingUser(final int id, final UserUpdateDTO userDetailsToUpdate) {
        try {
            final User user = getById(id);
            if(user == null){
                new IllegalArgumentException(
                    "Invalid Id for user"
                );
            }
                userMapper.updateUserFromDto(userDetailsToUpdate, user);
            final User updatedUser = userRepo.save(user);
            UserDTO userDTO = userMapper.toDto(updatedUser);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to update existing user");
        }
    }

    @Override
    public ResponseEntity<?> searching(final String keyword) {
        try {

            final List<User> users = userRepo.SearchingByKeyword(keyword);

            return ResponseEntity.ok(userMapper.toDtoList(users));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Nothing found");
        }
    }
}