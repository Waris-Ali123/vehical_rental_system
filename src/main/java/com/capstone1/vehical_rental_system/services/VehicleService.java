package com.capstone1.vehical_rental_system.services;

import java.util.List;

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


}