package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface VehicleService {

    public ResponseEntity<Vehicle> addVehicle(final String email, final Vehicle vehicle);

    public ResponseEntity<List<Vehicle>> getAllVehicles();

    public List<Vehicle> getByType(final String type);

    public Vehicle getByRegistrationNumber(final String registration_no);

    public ResponseEntity<List<Vehicle>> searching(final String keyword);

    public ResponseEntity<Vehicle> updateVehicle(final String registration_no, final String email, final Vehicle vehicle);

    public ResponseEntity<String> removeVehicleByRegistrationNumber(final String registration_no, final String email);

    public ResponseEntity<List<Vehicle>> findingAvailableVehicles(final LocalDate startDate, final LocalDate endDate);

    public ResponseEntity<List<Vehicle>> findingAvailableVehiclesByType(final String type, final LocalDate startDate, final LocalDate endDate);

}