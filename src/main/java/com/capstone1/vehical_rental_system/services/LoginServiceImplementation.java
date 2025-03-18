package com.capstone1.vehical_rental_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capstone1.vehical_rental_system.controllers.LoginController;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.repositories.UserRepo;

@Service
public class LoginServiceImplementation implements LoginService  {


    @Autowired
    UserRepo userRepo ;
    

    @Override
    public User getUserByEmailAndPass(String email, String password) {
        
        return userRepo.findUserByEmailAndPassword(email,password).get();
    }

    @Override
    public User storeUser(User user){
        return userRepo.save(user);
    }

    @Override
    public void creatingAdmin(User newAdmin,String alreadyAdminEmail){
        User alreadyAdmin = userRepo.findUserByEmail(alreadyAdminEmail).get();

        if(alreadyAdmin != null && alreadyAdmin.getRole()==User.Role.ADMIN){
            newAdmin.setRole(User.Role.ADMIN);
            userRepo.save(newAdmin);
        }
    }

    
    
}
