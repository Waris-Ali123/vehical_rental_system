package com.capstone1.vehical_rental_system.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import com.capstone1.vehical_rental_system.dtos.VehicleCreateDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleUpdateDTO;
import com.capstone1.vehical_rental_system.services.VehicleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vehicle")
@Validated // Enables validation for @RequestParam and @PathVariable
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/getAllVehicles")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/searching/{keyword}")
    public ResponseEntity<List<VehicleDTO>> searchVehicles(
            @PathVariable("keyword") @NotBlank(message = "Keyword cannot be blank") final String keyword) {
        return ResponseEntity.ok(vehicleService.searching(keyword));
    }

    @GetMapping("/findingAvailable")
    public ResponseEntity<List<VehicleDTO>> findingAvailableVehicles(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate) {
        return ResponseEntity.ok(vehicleService.findingAvailableVehicles(startDate, endDate));
    }

    @GetMapping("/findingAvailable/{type}")
    public ResponseEntity<List<VehicleDTO>> findingAvailableVehiclesByType(
            @PathVariable("type") @NotBlank(message = "Vehicle type cannot be blank") final String type,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate) {
        return ResponseEntity.ok(vehicleService.findingAvailableVehiclesByType(type, startDate, endDate));
    }

    @GetMapping("/getByType")
    public ResponseEntity<List<VehicleDTO>> getVehicleByType(
            @RequestParam("type") @NotBlank(message = "Vehicle type cannot be blank") final String type) {
        return ResponseEntity.ok(vehicleService.getByType(type));
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleDTO> addingVehicleByAdminOnly(
            @RequestParam("email") @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") final String email,
            @Valid @RequestBody final VehicleCreateDTO vehicle) {
        return ResponseEntity.ok(vehicleService.addVehicle(email, vehicle));
    }

    @PutMapping("/update/{registrationNumber}/{email}")
    public ResponseEntity<VehicleDTO> updatingVehicleDetails(
            @PathVariable("registrationNumber") @NotBlank(message = "Registration number cannot be blank") final String registrationNumber,
            @PathVariable("email") @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") final String email,
            @RequestBody @Valid final VehicleUpdateDTO vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(registrationNumber, email, vehicle));
    }

    @DeleteMapping("/delete/{registrationNumber}/{email}")
    public ResponseEntity<String> deleteByRegistrationNumber(
            @PathVariable("registrationNumber") @NotBlank(message = "Registration number cannot be blank") final String registrationNumber,
            @PathVariable("email") @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") final String email) {
        return ResponseEntity.ok(vehicleService.removeVehicleByRegistrationNumber(registrationNumber, email));
    }
}