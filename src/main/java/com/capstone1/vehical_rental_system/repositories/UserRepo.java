package com.capstone1.vehical_rental_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone1.vehical_rental_system.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    Optional<User> findUserByEmailAndPassword(String email,String password);

    Optional<User> findUserByEmail(String email);
    
}
