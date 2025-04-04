package com.capstone1.vehical_rental_system.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.repositories.UserRepo;
import com.capstone1.vehical_rental_system.services.LoginService;

@CrossOrigin(origins = "*")
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
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/signUp")
    public ResponseEntity<User> SignUp(@RequestBody User user) {
        User u1 ;
        try {
            u1 =  loginService.storeUser(user);
            if(u1!=null){
                return ResponseEntity.ok().body(u1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updatingExistingUser(@PathVariable int id, @RequestBody User userDetailstoUpdate) {

        try {
        System.out.println("woking");
            return loginService.updatingExistingUser(id, userDetailstoUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.internalServerError().build();
    }




    // Admin Specific fucntionalities

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam String email){
        try {
            return loginService.getAllUsers(email);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.internalServerError().build();

    }

    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<User>> getMethodName(@PathVariable("keyword") String keyword) {
        try{
            return loginService.searching(keyword);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }


    @PostMapping("/createAdmin")
    public ResponseEntity<User>  creatingAdmin(@RequestParam String alreadyAdminEmail,@RequestBody User newAdmin) {
        try {
            return loginService.creatingAdmin(newAdmin,alreadyAdminEmail);
                   
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{adminEmail}")
    public ResponseEntity<String> deletingUserByAdmin(@PathVariable("adminEmail") String emailAdmin, @RequestBody User userToDelete){
        if(loginService.isAdmin(emailAdmin)){
            System.out.println("admin is correct");
            return loginService.deletingUser(userToDelete);
        }
        return ResponseEntity.internalServerError().build();
    }

    
    
    

    
   

}

