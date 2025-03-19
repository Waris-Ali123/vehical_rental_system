package com.capstone1.vehical_rental_system.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.capstone1.vehical_rental_system.entities.Vehicle;

public interface VehicleService {

    public void addVehicle(Vehicle vehicle);

    public ResponseEntity<List<Vehicle>> getAllVehicles();

    public List<Vehicle> getByType(String type);

    public Vehicle getByRegistrationNumber(String registration_no);


}