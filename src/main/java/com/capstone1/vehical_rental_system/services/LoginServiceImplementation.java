package com.capstone1.vehical_rental_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public User getUserByEmail(String email){
        return userRepo.findUserByEmail(email).get();
    }

    @Override
    public User getById(int id){
        return userRepo.findById(id).get();
    }

    @Override
    public User storeUser(User user){
        user.setRole(User.Role.USER);
        return userRepo.save(user);
    }

    @Override
    public ResponseEntity<User> creatingAdmin(User newAdmin,String alreadyAdminEmail){

        try {
            
            if(isAdmin(alreadyAdminEmail)){
                newAdmin.setRole(User.Role.ADMIN);
                User user =  userRepo.save(newAdmin);
                return ResponseEntity.ok().body(user);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
           }
            
        } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    public boolean isAdmin(String alreadyAdminEmail){

        try {     
            User alreadyAdmin = getUserByEmail(alreadyAdminEmail);
            
            if(alreadyAdmin != null && alreadyAdmin.getRole()==User.Role.ADMIN){
                return true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;

    }

    public ResponseEntity<List<User>> getAllUsers(String email){

        try {
            if(isAdmin(email)){
                List<User> users = userRepo.findAll();
                return ResponseEntity.ok().body(users);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.internalServerError().build();
    }

    public ResponseEntity<String> deletingUser(User userToDelete){
        try {
            userRepo.delete(userToDelete);
            return ResponseEntity.ok().body("User delete Successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("User did not deleted.Check the arguments passed");
        }
        
    }

    @Override
    public ResponseEntity<User> UpdateUser(int id, User userDetailstoUpdate) {
    
        try {
           
            User user = getById(id);

            user.setName(userDetailstoUpdate.getName());    
            user.setEmail(userDetailstoUpdate.getEmail());
            user.setPassword(userDetailstoUpdate.getPassword());
            user.setContact_number(userDetailstoUpdate.getContact_number());


            //not updating the role here so no one can make himself an Admin. Admin can be added by the Admins only

            User updatedUser = storeUser(user);
            
            return  ResponseEntity.ok().body(updatedUser);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    
    
}
