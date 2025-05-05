package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleCreateDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleUpdateDTO;
import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.Availability;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import com.capstone1.vehical_rental_system.feignclients.BookingServiceFeignClient;
import com.capstone1.vehical_rental_system.mappers.VehicleMapper;
import com.capstone1.vehical_rental_system.repositories.BookingRepo;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VehicleServiceImplementation implements VehicleService {
    
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImplementation.class);
    

    @Autowired
    private BookingServiceFeignClient bookingServiceFeignClient;
   


    private final VehicleRepo vehicleRepo;
    private final LoginService loginService;
    private final VehicleMapper vehicleMapper;

    
    public VehicleServiceImplementation(VehicleRepo vehicleRepo, LoginService loginService, VehicleMapper vehicleMapper) {
        this.vehicleRepo = vehicleRepo;
        this.loginService = loginService;
        this.vehicleMapper = vehicleMapper;
    }
    

    @Override
    public VehicleDTO addVehicle(final String email, final VehicleCreateDTO vehicleCreateDTO) {
        logger.info("Attempting to add a new vehicle by admin with email: {}", email);
        if (loginService.isAdmin(email)) {
            Vehicle vehicleEntity = vehicleMapper.toEntity(vehicleCreateDTO);
            final Vehicle storedVehicle = vehicleRepo.save(vehicleEntity);
            logger.info("Vehicle added successfully with registration number: {}", storedVehicle.getRegistrationNumber());
            return vehicleMapper.toDto(storedVehicle);
        } else {
            logger.warn("Unauthorized attempt to add a vehicle by user with email: {}", email);
            throw new IllegalArgumentException("Only admin can add vehicle");
        }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        logger.info("Fetching all vehicles from the database");
        final List<Vehicle> all = vehicleRepo.findAll();
        logger.info("Successfully fetched {} vehicles", all.size());
        return vehicleMapper.toDtoList(all);
    }

    @Override
    public List<VehicleDTO> getByType(final String type) {
        logger.info("Fetching vehicles of type: {}", type);
        final VehicleType vType = VehicleType.valueOf(type.toUpperCase());
        List<Vehicle> vehicles = vehicleRepo.findAllByType(vType);
        logger.info("Successfully fetched {} vehicles of type: {}", vehicles.size(), type);
        return vehicleMapper.toDtoList(vehicles);
    }

    @Override
    public Vehicle getByRegistrationNumber(final String registrationNumber) {
        logger.info("Fetching vehicle with registration number: {}", registrationNumber);
        Vehicle vehicle = vehicleRepo.findVehicleByRegistrationNumber(registrationNumber);
        if (vehicle == null) {
            logger.error("Vehicle not found with registration number: {}", registrationNumber);
        } else {
            logger.info("Vehicle found with registration number: {}", registrationNumber);
        }
        return vehicle;
    }

    @Override
    public VehicleDTO updateVehicle(final String registrationNumber, final String email, final VehicleUpdateDTO vehicleModified) {
        logger.info("Attempting to update vehicle with registration number: {} by admin with email: {}", registrationNumber, email);
        if (loginService.isAdmin(email)) {
            final Vehicle oldVehicle = getByRegistrationNumber(registrationNumber);
            if (oldVehicle == null) {
                logger.error("Vehicle not found with registration number: {}", registrationNumber);
                throw new IllegalArgumentException("Vehicle not found");
            }
            vehicleMapper.updateVehicleFromDto(vehicleModified, oldVehicle);
            final Vehicle updatedVehicle = vehicleRepo.save(oldVehicle);
            logger.info("Vehicle updated successfully with registration number: {}", registrationNumber);
            return vehicleMapper.toDto(updatedVehicle);
        } else {
            logger.warn("Unauthorized attempt to update vehicle by user with email: {}", email);
            throw new IllegalArgumentException("Only admin can update vehicle");
        }
    }

    @Override
    public String removeVehicleByRegistrationNumber(final String registrationNumber, final String email) {
        logger.info("Attempting to delete vehicle with registration number: {} by admin with email: {}", registrationNumber, email);

        // Check if the user is an admin
        if (!loginService.isAdmin(email)) {
            logger.warn("Unauthorized attempt to delete vehicle by user with email: {}", email);
            throw new IllegalArgumentException("Only admin can delete vehicle");
        }

        // Fetch the vehicle
        final Vehicle vehicle = getByRegistrationNumber(registrationNumber);
        if (vehicle == null) {
            logger.error("Vehicle not found with registration number: {}", registrationNumber);
            throw new IllegalArgumentException("Vehicle not found");
        }

        

        // Check for existing bookings using Feign client
    LocalDate today = LocalDate.now();
    List<BookingDTO> existingBookings = bookingServiceFeignClient.searchForUpcomingOrCurrentBookings(
            vehicle.getVehicle_id(), today.toString(), "CONFIRMED");
    if (!existingBookings.isEmpty()) {
        logger.warn("Vehicle with registration number: {} has current or future bookings and cannot be deleted.", registrationNumber);
        throw new IllegalArgumentException("The vehicle has current or future bookings and cannot be deleted.");
    }

        // Proceed with deleting the vehicle
        vehicleRepo.delete(vehicle);
        logger.info("Vehicle deleted successfully with registration number: {}", registrationNumber);
        return "Deleted Successfully";
    }

    @Override
    public List<VehicleDTO> searching(final String keyword) {
        logger.info("Searching vehicles with keyword: {}", keyword);
        final List<Vehicle> vehicles = vehicleRepo.SearchingByKeyword(keyword);
        logger.info("Found {} vehicles matching keyword: {}", vehicles.size(), keyword);
        return vehicleMapper.toDtoList(vehicles);
    }

    @Override
    public List<VehicleDTO> findingAvailableVehicles(final LocalDate startDate, final LocalDate endDate) {
        logger.info("Searching for available vehicles between {} and {}", startDate, endDate);
        final List<Vehicle> availableVehicles = vehicleRepo.SearchingAvailableVehicles(startDate, endDate);
        logger.info("Found {} available vehicles between {} and {}", availableVehicles.size(), startDate, endDate);
        return vehicleMapper.toDtoList(availableVehicles);
    }

    @Override
    public List<VehicleDTO> findingAvailableVehiclesByType(final String type, final LocalDate startDate, final LocalDate endDate) {
        logger.info("Searching for available vehicles of type: {} between {} and {}", type, startDate, endDate);
        final VehicleType vehicleType = VehicleType.valueOf(type.toUpperCase());
        final List<Vehicle> availableVehicles = vehicleRepo.SearchingAvailableVehiclesByType(vehicleType, startDate, endDate);
        logger.info("Found {} available vehicles of type: {} between {} and {}", availableVehicles.size(), type, startDate, endDate);
        return vehicleMapper.toDtoList(availableVehicles);
    }
}