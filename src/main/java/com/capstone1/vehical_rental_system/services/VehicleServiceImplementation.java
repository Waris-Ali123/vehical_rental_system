package com.capstone1.vehical_rental_system.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<Vehicle> addVehicle(String email, Vehicle vehicle){
        try {

            
            if(loginService.isAdmin(email)){
                Vehicle storedVehicle = vehicleRepo.save(vehicle);
                if(storedVehicle==null){
                    return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
                }
                return ResponseEntity.ok(storedVehicle);
                
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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
                oldVehicle.setAvailability(vehicleModified.getAvailability());

                // ==newly added===
                oldVehicle.setFuelType(vehicleModified.getFuelType());
                oldVehicle.setSeatingCapacity(vehicleModified.getSeatingCapacity());
                oldVehicle.setMileage(vehicleModified.getMileage());
                oldVehicle.setColor(vehicleModified.getColor());
                oldVehicle.setVehicleImage(vehicleModified.getVehicleImage());

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

            return ResponseEntity.ok().body(vehicles);
        } catch (Exception e) {
           e.printStackTrace();
           return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Vehicle>> findingAvailableVehicles(LocalDate startDate, LocalDate endDate) {
        try {
            List<Vehicle> availableVehicles = vehicleRepo.SearchingAvailableVehicles(startDate, endDate);
            return ResponseEntity.ok().body(availableVehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @Override
    public ResponseEntity<List<Vehicle>> findingAvailableVehiclesByType(String type,LocalDate startDate, LocalDate endDate) {
        try{
            Vehicle.VehicleType vehicleType = VehicleType.valueOf(type.toUpperCase());
            List<Vehicle> availableVehicles = vehicleRepo.SearchingAvailableVehiclesByType(vehicleType,startDate, endDate);
            return ResponseEntity.ok().body(availableVehicles);
             

        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

}
