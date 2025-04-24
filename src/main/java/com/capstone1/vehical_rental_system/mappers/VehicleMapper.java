package com.capstone1.vehical_rental_system.mappers;

import com.capstone1.vehical_rental_system.dtos.VehicleCreateDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleDTO;
import com.capstone1.vehical_rental_system.dtos.VehicleUpdateDTO;
import com.capstone1.vehical_rental_system.entities.Vehicle;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

    @Mapping(source = "vehicle_id", target = "vehicleId")
    @Mapping(source = "price_per_day", target = "pricePerDay")
    VehicleDTO toDto(Vehicle vehicle);
    
    @Mapping(source = "vehicle_id", target = "vehicleId")
    @Mapping(source = "price_per_day", target = "pricePerDay")
    List<VehicleDTO> toDtoList(List<Vehicle> vehicle);

    @Mapping(source = "pricePerDay", target = "price_per_day")
    @Mapping(target = "bookingsByVehicle", ignore = true)
    @Mapping(target = "reviewsOnVehicle", ignore = true)
    Vehicle toEntity(VehicleDTO dto);

    @Mapping(source = "registrationNumber", target = "registrationNumber")
    @Mapping(source = "pricePerDay", target = "price_per_day")
    Vehicle toEntity(VehicleCreateDTO dto);

    // @Mapping(source = "vehicleId", target = "vehicle_id")
    @Mapping(source = "pricePerDay", target = "price_per_day")
    Vehicle toEntity(VehicleUpdateDTO dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "model", target = "model"),
        @Mapping(source = "type", target = "type"),
        @Mapping(source = "availability", target = "availability"),
        @Mapping(source = "pricePerDay", target = "price_per_day"),
        @Mapping(source = "fuelType", target = "fuelType"),
        @Mapping(source = "seatingCapacity", target = "seatingCapacity"),
        @Mapping(source = "mileage", target = "mileage"),
        @Mapping(source = "color", target = "color"),
        @Mapping(source = "vehicleImage", target = "vehicleImage")
    })
    void updateVehicleFromDto(VehicleUpdateDTO dto, @MappingTarget Vehicle vehicle);
}
