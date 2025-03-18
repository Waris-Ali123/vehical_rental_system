package com.capstone1.vehical_rental_system.services;

import java.beans.JavaBean;

import org.springframework.stereotype.Service;

import com.capstone1.vehical_rental_system.entities.User;

public interface LoginService {    
    User getUserByEmailAndPass(String email,String password);

    User storeUser(User user);

    void creatingAdmin(User newAdmin,String alreadyAdminEmail);
}
