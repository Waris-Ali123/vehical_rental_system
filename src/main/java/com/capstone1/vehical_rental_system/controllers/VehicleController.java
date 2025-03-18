package com.capstone1.vehical_rental_system.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.Availability;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import com.capstone1.vehical_rental_system.services.VehicleService;



@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    // @Autowired
    
    @GetMapping("/getAll")
    public ResponseEntity<List<Vehicle>> getMethodName() {
        return vehicleService.getAllVehicles();
    }

    @PostMapping("/initialize")
    public void initializing() {
        vehicleService.addVehicle(new Vehicle("Raider 786",VehicleType.BIKE,"mp0976439",Availability.AVAILABLE,1000.58));
        vehicleService.addVehicle(new Vehicle("PASSION 786",VehicleType.BIKE,"mp09SL439",Availability.AVAILABLE,1000.58));
        vehicleService.addVehicle(new Vehicle("JII 786",VehicleType.BIKE,"mpSDF76439",Availability.AVAILABLE,100.58));
        
    }


    @PostMapping("/add")
    public ResponseEntity<String> postMethodName(@RequestBody Vehicle vehicle) {
        try {
            vehicleService.addVehicle(vehicle);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't store the vehicle.");
        }
        return ResponseEntity.ok("Added succesfully");
    }


    @GetMapping("/getByType")
    public ResponseEntity<List<Vehicle>> getMethodName(@RequestParam String type) {
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


    // @GetMapping("/get")
    // public String getMethodName(@RequestParam String param) {
    //     return new String();
    // }
    
    
    
    
}
