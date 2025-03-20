package com.capstone1.vehical_rental_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle,Integer>{
    public List<Vehicle> findAllByType(VehicleType type);    

    public Vehicle findVehicleByRegistrationNumber(String registrantionNumber);

    @Query(value = "Select V from Vehicle V where "+
                    " Lower(name) like Lower(Concat('%',:keyword,'%')) Or  " + 
                    " Lower(type) like Lower(Concat('%',:keyword,'%')) Or " + 
                    " Lower(model) like Lower(Concat('%',:keyword,'%'))" 
        )
    public List<Vehicle> SearchingByKeyword(String keyword);
} 
