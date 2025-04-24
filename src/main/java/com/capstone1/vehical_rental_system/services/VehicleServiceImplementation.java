package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.controllers.VehicleController;
import com.capstone1.vehical_rental_system.dtos.VehicleCreateDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleUpdateDTO;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import com.capstone1.vehical_rental_system.mappers.VehicleMapper;
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
    private final VehicleMapper vehicleMapper;

    
    public VehicleServiceImplementation(VehicleRepo vehicleRepo, LoginService loginService,VehicleMapper vehicleMapper) {
        this.vehicleRepo = vehicleRepo;
        this.loginService = loginService;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public ResponseEntity<?> addVehicle(final String email, final VehicleCreateDTO vehicleCreateDTO) {
        try {
            if (loginService.isAdmin(email)) {
                Vehicle vehicleEntity = vehicleMapper.toEntity(vehicleCreateDTO);
                final Vehicle storedVehicle = vehicleRepo.save(vehicleEntity);
                return ResponseEntity.ok(vehicleMapper.toDto(storedVehicle));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only admin can add vehicle");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @Override
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        final List<Vehicle> all = vehicleRepo.findAll();
        return ResponseEntity.ok(vehicleMapper.toDtoList(all));
    }
    
    @Override
    public List<VehicleDTO> getByType(final String type) {
        final VehicleType vType = VehicleType.valueOf(type.toUpperCase());
        List<Vehicle>  vehicles =  vehicleRepo.findAllByType(vType);
        return vehicleMapper.toDtoList(vehicles);
    }

    //Helper method for other service classes. It has not been used by any controller yet
    @Override
    public Vehicle getByRegistrationNumber(final String registrationNumber) {
        return vehicleRepo.findVehicleByRegistrationNumber(registrationNumber);
    }

    @Override
    public ResponseEntity<?> updateVehicle(final String registrationNumber, final String email,
                                           final VehicleUpdateDTO vehicleModified) {
        try {
            if (loginService.isAdmin(email)) {
                final Vehicle oldVehicle = getByRegistrationNumber(registrationNumber);
                vehicleMapper.updateVehicleFromDto(vehicleModified, oldVehicle);
                final Vehicle updatedVehicle = vehicleRepo.save(oldVehicle);
                return ResponseEntity.ok(vehicleMapper.toDto(updatedVehicle));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Admin can add vehicle!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong!!");
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
    public ResponseEntity<List<VehicleDTO>> searching(final String keyword) {
        try {
            final List<Vehicle> vehicles = vehicleRepo.SearchingByKeyword(keyword);
            return ResponseEntity.ok(vehicleMapper.toDtoList(vehicles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<VehicleDTO>> findingAvailableVehicles(final LocalDate startDate, final LocalDate endDate) {
        try {
            final List<Vehicle> availableVehicles = vehicleRepo.SearchingAvailableVehicles(startDate, endDate);
            return ResponseEntity.ok(vehicleMapper.toDtoList(availableVehicles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<VehicleDTO>> findingAvailableVehiclesByType(final String type, 
                                    final LocalDate startDate, final LocalDate endDate) {
        try {
            final VehicleType vehicleType = VehicleType.valueOf(type.toUpperCase());
            final List<Vehicle> availableVehicles = 
                vehicleRepo.SearchingAvailableVehiclesByType(vehicleType, startDate, endDate);
            return ResponseEntity.ok(vehicleMapper.toDtoList(availableVehicles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
