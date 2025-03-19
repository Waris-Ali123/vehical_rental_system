package com.capstone1.vehical_rental_system.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.capstone1.vehical_rental_system.entities.User.Role;
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
        repo.save(new User("nayab","nayab@gmail.com","nayab","562565984",Role.ADMIN));
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
        
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updatingExistingUser(@PathVariable int id, @RequestBody User userDetailstoUpdate) {

        try {
            User user = loginService.getById(id);

            user.setName(userDetailstoUpdate.getName());    
            user.setEmail(userDetailstoUpdate.getEmail());
            user.setPassword(userDetailstoUpdate.getPassword());
            user.setContact_number(userDetailstoUpdate.getContact_number());


            User updatedUser = loginService.storeUser(user);
            
            return  ResponseEntity.ok().body(updatedUser);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.internalServerError().build();
    }




    // Admin Specific fucntionalities

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam String email){
        try {
            if(loginService.isAdmin(email)){
                List<User> users = loginService.getAllUsers();
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
            return loginService.deletingUser(userToDelete);
        }
        return ResponseEntity.internalServerError().build();
    }

    
   

}

