package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface VehicleService {

    ResponseEntity<Vehicle> addVehicle(final String email, final Vehicle vehicle);

    ResponseEntity<List<Vehicle>> getAllVehicles();

    List<Vehicle> getByType(final String type);

    Vehicle getByRegistrationNumber(final String registration_no);

    ResponseEntity<List<Vehicle>> searching(final String keyword);

    ResponseEntity<Vehicle> updateVehicle(final String registration_no, final String email, final Vehicle vehicle);

    ResponseEntity<String> removeVehicleByRegistrationNumber(final String registration_no, final String email);

    ResponseEntity<List<Vehicle>> findingAvailableVehicles(final LocalDate startDate, final LocalDate endDate);

    ResponseEntity<List<Vehicle>> findingAvailableVehiclesByType(final String type,
                    final LocalDate startDate, final LocalDate endDate);

}