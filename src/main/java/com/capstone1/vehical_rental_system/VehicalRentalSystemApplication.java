package com.capstone1.vehical_rental_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VehicalRentalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicalRentalSystemApplication.class, args);

    }
}


// userId,name
