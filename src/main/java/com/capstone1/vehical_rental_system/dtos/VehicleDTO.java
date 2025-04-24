package com.capstone1.vehical_rental_system.dtos;

import com.capstone1.vehical_rental_system.entities.Vehicle.Availability;
import com.capstone1.vehical_rental_system.entities.Vehicle.FuelType;
import com.capstone1.vehical_rental_system.entities.Vehicle.VehicleType;
import jakarta.validation.constraints.*;

public class VehicleDTO {

    @NotNull(message = "Vehicle ID is required")
    private int vehicleId;

    @NotBlank(message = "Vehicle name is required")
    private String name;

    @NotNull(message = "Vehicle type is required")
    private VehicleType type;

    @NotNull(message = "Model must be specified")
    private String model;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotNull(message = "Availability status is required")
    private Availability availability;

    @Positive(message = "Price per day must be greater than zero")
    private double pricePerDay;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @Min(value = 1, message = "Seating capacity must be at least 1")
    private int seatingCapacity;

    @Positive(message = "Mileage must be greater than 0")
    private double mileage;

    @Size(max = 30, message = "Color should not exceed 30 characters")
    private String color;

    // @Size(max = 255, message = "Image path should not exceed 255 characters")
    @NotBlank(message = "Image url must be specified")
    private String vehicleImage;

    public VehicleDTO() {
    }

    // Getters and Setters

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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
