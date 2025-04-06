package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VehicleServiceImplementation implements VehicleService {

    private final VehicleRepo vehicleRepo;
    private final LoginService loginService;

    
    public VehicleServiceImplementation(VehicleRepo vehicleRepo, LoginService loginService) {
        this.vehicleRepo = vehicleRepo;
        this.loginService = loginService;
    }

    @Override
    public ResponseEntity<Vehicle> addVehicle(final String email, final Vehicle vehicle) {
        try {
            if (loginService.isAdmin(email)) {
                final Vehicle storedVehicle = vehicleRepo.save(vehicle);
                return ResponseEntity.ok(storedVehicle);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        final List<Vehicle> all = vehicleRepo.findAll();
        return ResponseEntity.ok(all);
    }

    @Override
    public List<Vehicle> getByType(final String type) {
        final VehicleType vType = VehicleType.valueOf(type.toUpperCase());
        return vehicleRepo.findAllByType(vType);
    }

    @Override
    public Vehicle getByRegistrationNumber(final String registrationNumber) {
        return vehicleRepo.findVehicleByRegistrationNumber(registrationNumber);
    }

    @Override
    public ResponseEntity<Vehicle> updateVehicle(final String registrationNumber, final String email, final Vehicle vehicleModified) {
        try {
            if (loginService.isAdmin(email)) {
                final Vehicle oldVehicle = getByRegistrationNumber(registrationNumber);
                oldVehicle.setModel(vehicleModified.getModel());
                oldVehicle.setName(vehicleModified.getName());
                oldVehicle.setPrice_per_day(vehicleModified.getPrice_per_day());
                oldVehicle.setType(vehicleModified.getType());
                oldVehicle.setAvailability(vehicleModified.getAvailability());
                oldVehicle.setFuelType(vehicleModified.getFuelType());
                oldVehicle.setSeatingCapacity(vehicleModified.getSeatingCapacity());
                oldVehicle.setMileage(vehicleModified.getMileage());
                oldVehicle.setColor(vehicleModified.getColor());
                oldVehicle.setVehicleImage(vehicleModified.getVehicleImage());
                final Vehicle updatedVehicle = vehicleRepo.save(oldVehicle);
                return ResponseEntity.ok(updatedVehicle);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> removeVehicleByRegistrationNumber(
    final String registrationNumber,
    final String email) {
        try {
            if (loginService.isAdmin(email)) {
                final Vehicle vehicle = getByRegistrationNumber(registrationNumber);
                vehicleRepo.delete(vehicle);
                return ResponseEntity.ok("Deleted Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Vehicle>> searching(final String keyword) {
        try {
            final List<Vehicle> vehicles = vehicleRepo.SearchingByKeyword(keyword);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Vehicle>> findingAvailableVehicles(final LocalDate startDate, final LocalDate endDate) {
        try {
            final List<Vehicle> availableVehicles = vehicleRepo.SearchingAvailableVehicles(startDate, endDate);
            return ResponseEntity.ok(availableVehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Vehicle>> findingAvailableVehiclesByType(final String type, 
                                    final LocalDate startDate, final LocalDate endDate) {
        try {
            final VehicleType vehicleType = VehicleType.valueOf(type.toUpperCase());
            final List<Vehicle> availableVehicles = 
                vehicleRepo.SearchingAvailableVehiclesByType(vehicleType, startDate, endDate);
            return ResponseEntity.ok(availableVehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
