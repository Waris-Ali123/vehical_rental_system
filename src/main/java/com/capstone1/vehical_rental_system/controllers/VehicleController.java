package com.capstone1.vehical_rental_system.controllers;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/getAllVehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<Vehicle>> searchVehicles(@PathVariable("keyword") final String keyword) {
        try {
            return vehicleService.searching(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findingAvailable")
    public ResponseEntity<List<Vehicle>> findingAvailableVehicles(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate) {
        try {
            return vehicleService.findingAvailableVehicles(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/findingAvailable/{type}")
    public ResponseEntity<List<Vehicle>> findingAvailableVehiclesByType(
            @PathVariable("type") final String type,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate) {
        try {
            return vehicleService.findingAvailableVehiclesByType(type, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    // ================Admin specific details=====================

    @GetMapping("/getByType")
    public ResponseEntity<List<Vehicle>> getVehicleByType(@RequestParam("type") final String type) {
        List<Vehicle> vehicle = new ArrayList<>();
        try {
            vehicle = vehicleService.getByType(type);
            if (!vehicle.isEmpty()) {
                return ResponseEntity.ok(vehicle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(vehicle);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addingVehicleByAdminOnly(
            @RequestParam("email") final String email,
            @RequestBody final Vehicle vehicle) {
        try {
            return vehicleService.addVehicle(email, vehicle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PutMapping("/update/{registration_number}/{email}")
    public ResponseEntity<?> updatingVehicleDetails(
            @PathVariable("registration_number") final String registration_number,
            @PathVariable("email") final String email,
            @RequestBody final Vehicle vehicle) {
        return vehicleService.updateVehicle(registration_number, email, vehicle);
    }

    @DeleteMapping("/delete/{registration_number}/{email}")
    public ResponseEntity<String> deleteByRegistrationNumber(
            @PathVariable("registration_number") final String registration_number,
            @PathVariable("email") final String email) {
        try {
            return vehicleService.removeVehicleByRegistrationNumber(registration_number, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
