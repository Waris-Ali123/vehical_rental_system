package com.capstone1.vehical_rental_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.capstone1.vehical_rental_system.controllers.BookingController;
import com.capstone1.vehical_rental_system.controllers.LoginController;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;

@Service
public class VehicleServiceImplementation implements VehicleService {



    @Autowired
    VehicleRepo vehicleRepo;


    @Override
    public void addVehicle(Vehicle vehicle){
        vehicleRepo.save(vehicle);
    }

    public ResponseEntity<List<Vehicle>> getAllVehicles(){
        
        List<Vehicle> all = vehicleRepo.findAll();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(all);
    }

    public List<Vehicle> getByType(String type){
        VehicleType vType = VehicleType.valueOf(type.toUpperCase());
        return vehicleRepo.findAllByType(vType);
    }
    
    @Override
    public Vehicle getByRegistrationNumber(String registration_no){
        return vehicleRepo.findVehicleByRegistrationNumber(registration_no);
    }

}
