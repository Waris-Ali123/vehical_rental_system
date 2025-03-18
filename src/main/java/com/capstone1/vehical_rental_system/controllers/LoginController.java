package com.capstone1.vehical_rental_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.repositories.UserRepo;
import com.capstone1.vehical_rental_system.services.LoginService;



@RestController
@RequestMapping("/auth") 
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    UserRepo repo;

    @GetMapping("/login")
    public ResponseEntity<User> getUserByEmailAndPassword(@RequestParam String email,@RequestParam String password) {
        try {
            User u1 = loginService.getUserByEmailAndPass(email,password);
            if(u1 != null)
                return ResponseEntity.ok().body(u1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/initialize")
    public void initializing() {
        repo.save(new User("waris", "waris@gmail.com", "waris"));
        repo.save(new User("ali", "ali@gmail.com", "ali"));
        repo.save(new User("sheikh", "sheikhj@gmail.com", "sheikh"));
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> SignUp(@RequestBody User user) {
        try {
            User u1 =  loginService.storeUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("The entry is whether duplicate or not filled completely");    
        }

        return ResponseEntity.ok("The data has been stored successfully");
        
    }


    @PostMapping("/createAdmin")
    public String creatingAdmin(@RequestParam String alreadyAdminEmail,@RequestBody User newAdmin) {
        try {
            loginService.creatingAdmin(newAdmin,alreadyAdminEmail);
            return "User created succcessfully";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create an admin";
        }
    }


    
    // @PostMapping("/forgetPassword")
    // public User forgetPassword(@RequestBody String entity) {
    //     //TODO: process POST request
    //     return entity;
    // }
    

}
