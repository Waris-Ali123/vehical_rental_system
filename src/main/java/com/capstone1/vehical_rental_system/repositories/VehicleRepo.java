package com.capstone1.vehical_rental_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;

public interface VehicleRepo extends JpaRepository<Vehicle,Integer>{
    public List<Vehicle> findAllByType(VehicleType type);    

    public Vehicle findVehicleByRegistrationNumber(String registrantionNumber);
} 
