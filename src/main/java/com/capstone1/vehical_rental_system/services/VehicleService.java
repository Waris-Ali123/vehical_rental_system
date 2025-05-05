package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.VehicleCreateDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleUpdateDTO;
import com.capstone1.vehical_rental_system.entities.Vehicle;

import java.time.LocalDate;
import java.util.List;

public interface VehicleService {

    VehicleDTO addVehicle(final String email, final VehicleCreateDTO vehicle);

    List<VehicleDTO> getAllVehicles();

    List<VehicleDTO> getByType(final String type);

    Vehicle getByRegistrationNumber(final String registration_no);

    List<VehicleDTO> searching(final String keyword);

    VehicleDTO updateVehicle(final String registration_no, final String email, final VehicleUpdateDTO vehicle);

    String removeVehicleByRegistrationNumber(final String registration_no, final String email);

    List<VehicleDTO> findingAvailableVehicles(final LocalDate startDate, final LocalDate endDate);

    List<VehicleDTO> findingAvailableVehiclesByType(final String type, final LocalDate startDate, final LocalDate endDate);
}