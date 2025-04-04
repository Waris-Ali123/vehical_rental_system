package com.capstone1.vehical_rental_system.repositories;

import com.capstone1.vehical_rental_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByEmail(String email);

    @Query(value = "Select U from User U where " +
            " Lower(name) like Lower(Concat('%',:keyword,'%')) Or  " +
            " email like (Concat('%',:keyword,'%')) Or " +
            " contact_number like (Concat('%',:keyword,'%')) Or " +
            " Lower(role) like Lower(Concat('%',:keyword,'%'))"
    )
    public List<User> SearchingByKeyword(String keyword);

}
