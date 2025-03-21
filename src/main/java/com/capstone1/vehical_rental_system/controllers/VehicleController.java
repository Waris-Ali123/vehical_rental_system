package com.capstone1.vehical_rental_system.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.services.VehicleService;


@RestController
@RequestMapping("/vehicle")
public class VehicleController {


    @Autowired
    VehicleService vehicleService;

    // @Autowired
    
    @GetMapping("/getAllVehicles")
    public ResponseEntity<List<Vehicle>> getMethodName() {
        return vehicleService.getAllVehicles();
    }

    @PostMapping("/initialize")
    public void initializing() {
        // vehicleService.addVehicle(new Vehicle("Raider 786",VehicleType.BIKE,"mp0976439",Availability.AVAILABLE,1000.58));
        // vehicleService.addVehicle(new Vehicle("PASSION 786",VehicleType.BIKE,"mp09SL439",Availability.AVAILABLE,1000.58));
        // vehicleService.addVehicle(new Vehicle("JII 786",VehicleType.BIKE,"mpSDF76439",Availability.AVAILABLE,100.58));
        
    }


    @GetMapping("/getByType")
    public ResponseEntity<List<Vehicle>> getVehicleByType(@RequestParam String type) {
        List<Vehicle> vehicle = new ArrayList<>();
        try {
            vehicle = vehicleService.getByType(type);
            if(vehicle.size()>0)
                return ResponseEntity.ok(vehicle);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(vehicle);

    }

    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<Vehicle>> getMethodName(@PathVariable("keyword") String keyword) {
        try{
            return vehicleService.searching(keyword);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }


    @GetMapping("/findingAvailable")
    public ResponseEntity<List<Vehicle>> findingAvailableVehicles( @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
            
        try {
            return vehicleService.findingAvailableVehicles(startDate,endDate);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findingAvailable/{type}")
    public ResponseEntity<List<Vehicle>> findingAvailableVehiclesByType(@PathVariable("type") String type, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
            
        try {
            return vehicleService.findingAvailableVehiclesByType(type,startDate,endDate);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    

        // Admin Specific Functionalities

    @PostMapping("/add/{email}")
    public ResponseEntity<String> addingVehicleByAdminOnly(@PathVariable("email") String email ,@RequestBody Vehicle vehicle) {
        try {
            return vehicleService.addVehicle(email,vehicle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't store the vehicle.");
        }
        // return ResponseEntity.ok("Added succesfully");
    }


    //updatingVehicle
    @PutMapping("/update/{registration_no}/{email}")
    public ResponseEntity<Vehicle> updatingVehicleDetails(@PathVariable("registration_no") String registration_no,@PathVariable("email") String email, @RequestBody Vehicle vehicle) {
        
        return vehicleService.updateVehicle(registration_no,email,vehicle);
         
    }


    @DeleteMapping("/delete/{registration_no}/{email}")
    public ResponseEntity<String> deleteByRegistrationNumber(@PathVariable("registration_no") String registration_no,@PathVariable("email") String email){
        try {
            return vehicleService.removeVehicleByRegistrationNumber(registration_no,email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    
    
    
}
