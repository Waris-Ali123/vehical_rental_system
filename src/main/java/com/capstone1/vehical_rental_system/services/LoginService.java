package com.capstone1.vehical_rental_system.services;

import java.beans.JavaBean;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone1.vehical_rental_system.entities.User;

public interface LoginService {    
    User getUserByEmailAndPass(String email,String password);

    User getUserByEmail(String email);

    User getById(int id);

    User storeUser(User user);

    ResponseEntity<User>  creatingAdmin(User newAdmin,String alreadyAdminEmail);

    public boolean isAdmin(String alreadyAdminEmail);

    public List<User> getAll();
}
