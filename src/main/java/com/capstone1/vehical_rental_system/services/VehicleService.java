package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.VehicleCreateDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleUpdateDTO;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface VehicleService {

    ResponseEntity<?> addVehicle(final String email, final VehicleCreateDTO vehicle);

    ResponseEntity<List<VehicleDTO>> getAllVehicles();

    List<VehicleDTO> getByType(final String type);

    Vehicle getByRegistrationNumber(final String registration_no);

    ResponseEntity<List<VehicleDTO>> searching(final String keyword);

    ResponseEntity<?> updateVehicle(final String registration_no, final String email, final VehicleUpdateDTO vehicle);

    ResponseEntity<String> removeVehicleByRegistrationNumber(final String registration_no, final String email);

    ResponseEntity<List<VehicleDTO>> findingAvailableVehicles(final LocalDate startDate, final LocalDate endDate);

    ResponseEntity<List<VehicleDTO>> findingAvailableVehiclesByType(final String type,
                    final LocalDate startDate, final LocalDate endDate);

}