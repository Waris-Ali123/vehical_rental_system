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
    public ResponseEntity<String> addVehicle(String email, Vehicle vehicle){
        try {

            
            if(loginService.isAdmin(email)){
                vehicleRepo.save(vehicle);
                return ResponseEntity.ok("Vehicle Deleted Successfully");
                
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only admins are allowed to add the vehicle");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("May be the vehicle is already registered");
        }

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().body(null);
            
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

    @Override
    public ResponseEntity<List<Vehicle>> searching(String keyword) {
        try {
            List<Vehicle> vehicles =  vehicleRepo.SearchingByKeyword(keyword);

            //if data not found we give not found status
            if(vehicles.size()<=0)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok().body(vehicles);
        } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.internalServerError().build();
        }
    }






}
