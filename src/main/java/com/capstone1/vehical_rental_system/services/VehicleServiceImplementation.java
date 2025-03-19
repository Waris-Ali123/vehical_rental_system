package com.capstone1.vehical_rental_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;

@Service
public class VehicleServiceImplementation implements VehicleService {




    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired 
    LoginService loginService;


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
    
    @Override
    public ResponseEntity<Vehicle> updateVehicle(String registration_no, String email, Vehicle vehicleModified) {
        
        try {
            if (loginService.isAdmin(email)) {
                Vehicle oldVehicle = getByRegistrationNumber(registration_no);
                oldVehicle.setModel(vehicleModified.getModel());
                oldVehicle.setName(vehicleModified.getName());
                oldVehicle.setPrice_per_day(vehicleModified.getPrice_per_day());
                oldVehicle.setType(vehicleModified.getType());

                Vehicle updatedVehicle = vehicleRepo.save(oldVehicle);
                return ResponseEntity.ok().body(updatedVehicle);

                
            }else{
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
            
    }

    @Override
    public ResponseEntity<String> removeVehicleByRegistrationNumber(String registration_no, String email) {
        try {
            if(loginService.isAdmin(email)){
                    Vehicle vehicle = getByRegistrationNumber(registration_no);

                    vehicleRepo.delete(vehicle);
                    return ResponseEntity.ok("Deleted Successfully");
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }




}
