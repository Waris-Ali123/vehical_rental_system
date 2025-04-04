package com.capstone1.vehical_rental_system.repositories;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {
    public List<Vehicle> findAllByType(VehicleType type);

    public Vehicle findVehicleByRegistrationNumber(String registrationNumber);

    @Query(value = "Select V from Vehicle V where " +
            " Lower(name) like Lower(Concat('%',:keyword,'%')) Or  " +
            " Lower(type) like Lower(Concat('%',:keyword,'%')) Or " +
            " Lower(model) like Lower(Concat('%',:keyword,'%'))"
    )
    public List<Vehicle> SearchingByKeyword(String keyword);


    @Query(value = "select V from Vehicle V where " +
            " V.availability = 'AVAILABLE' " +
            " And V.vehicle_id Not In " +
            " ( Select B.vehicle.vehicle_id from Booking B where " +
            " B.startDate <=:endDate and B.endDate >=:startDate and B.bookingStatus = 'CONFIRMED')"
    )
    public List<Vehicle> SearchingAvailableVehicles(LocalDate startDate, LocalDate endDate);

    @Query(value = "select V from Vehicle V where " +
            " V.type =:type And " +
            " V.availability = 'AVAILABLE' And " +
            " V.vehicle_id Not In " +
            " ( Select B.vehicle.vehicle_id from Booking B where " +
            " B.startDate <=:endDate and B.endDate >=:startDate and B.bookingStatus = 'CONFIRMED')"
    )
    public List<Vehicle> SearchingAvailableVehiclesByType(Vehicle.VehicleType type, LocalDate startDate, LocalDate endDate);
} 
