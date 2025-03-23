package com.capstone1.vehical_rental_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.repositories.UserRepo;

@Service
public class LoginServiceImplementation implements LoginService  {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepo userRepo ;

    @Override
    public User getUserByEmailAndPass(String email, String password) {
        
        User user = userRepo.findUserByEmail(email).orElse(null);

        if(user!=null && passwordEncoder.matches(password, user.getPassword())){
            return user;
        }
        
        return null;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public ResponseEntity<User> creatingAdmin(User newAdmin,String alreadyAdminEmail){

        try {
            
            if(isAdmin(alreadyAdminEmail)){
                newAdmin.setRole(User.Role.ADMIN);
                newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
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


    public ResponseEntity<User> updatingExistingUser(int id,User userDetailstoUpdate){
        try {
            User user = getById(id);

            user.setName(userDetailstoUpdate.getName());    
            user.setEmail(userDetailstoUpdate.getEmail());
            user.setContact_number(userDetailstoUpdate.getContact_number());

            
            // user.setPassword(userDetailstoUpdate.getPassword());
            // user.setPassword(passwordEncoder.encode(userDetailstoUpdate.getPassword()));
            
            User updatedUser = userRepo.save(user);
            return  ResponseEntity.ok().body(updatedUser);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.internalServerError().build();
    }

    @Override
    public ResponseEntity<List<User>> searching(String keyword) {
        try {
            List<User> users =  userRepo.SearchingByKeyword(keyword);

            //if data not found we give not found status
            if(users.size()<=0)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.internalServerError().build();
        }
    }
    
    
}
