package com.capstone1.vehical_rental_system.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiq {

    @Bean
    public PasswordEncoder encodingPassword(){

        return new BCryptPasswordEncoder();
    }   
    
    
}
