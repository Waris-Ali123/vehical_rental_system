package com.capstone1.vehical_rental_system.dtos;

import com.capstone1.vehical_rental_system.entities.Vehicle.Availability;
import com.capstone1.vehical_rental_system.entities.Vehicle.FuelType;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import jakarta.validation.constraints.*;

public class VehicleUpdateDTO {

    // @Positive(message = "Vehicle ID must be a positive integer")
    // private int vehicleId;

    @NotBlank(message = "Vehicle name must not be blank")
    private String name;

    private String model;

    @NotNull(message = "Vehicle type must be specified")
    private VehicleType type;

    @NotNull(message = "Availability status must be specified")
    private Availability availability;

    @Positive(message = "Price per day must be a positive value")
    private double pricePerDay;

    @NotNull(message = "Fuel type must be specified")
    private FuelType fuelType;
    
    @Min(value = 1, message = "Seating capacity must be at least 1")
    private int seatingCapacity;
    
    @Positive(message = "Mileage must be a positive value")
    private double mileage;
    
    private String color;

    private String vehicleImage;
    
    // Getters and Setters

    // public int getVehicleId() {
    //     return vehicleId;
    // }

    // public void setVehicleId(int vehicleId) {
    //     this.vehicleId = vehicleId;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }


    
}
