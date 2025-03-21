package com.capstone1.vehical_rental_system.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;

import com.capstone1.vehical_rental_system.entities.Vehicle;

public interface VehicleService {

    public ResponseEntity<String> addVehicle(String email,Vehicle vehicle);

    public ResponseEntity<List<Vehicle>> getAllVehicles();

    public List<Vehicle> getByType(String type);

    public Vehicle getByRegistrationNumber(String registration_no);

    public ResponseEntity<List<Vehicle>> searching(String keyword);

    public ResponseEntity<Vehicle> updateVehicle(String registration_no,String email,Vehicle vehicle);

    public ResponseEntity<String> removeVehicleByRegistrationNumber(String registration_no,String email);

    public ResponseEntity<List<Vehicle>> findingAvailableVehicles(LocalDate startDate,LocalDate endDate);
    
    public ResponseEntity<List<Vehicle>> findingAvailableVehiclesByType(String type,LocalDate startDate,LocalDate endDate);

}