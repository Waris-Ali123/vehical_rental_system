package com.capstone1.vehical_rental_system.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    public enum VehicleType{
        CAR,BIKE,TRUCK;
    }

    public enum Availability{
        AVAILABLE,UNDER_MAINTENANCE;
    }

    public enum FuelType {
        PETROL,
        DIESEL,
        ELECTRIC,
        HYBRID,
        CNG
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicle_id;

    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    private String model ;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Availability availability;

    @Column(nullable = false)
    private double price_per_day;


    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookingsByVehicle = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviewsOnVehicle = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;  // PETROL, DIESEL, ELECTRIC, etc.

    @Column(nullable = false)
    private int seatingCapacity;

    @Column(nullable = false)
    private double mileage;  // in km

    @Column(length = 30)
    private String color;

    @Column(length = 255)
    private String vehicleImage;  // URL or file path

  



    public Vehicle(){
    }

    public Vehicle(String name, VehicleType type, String model, String registrationNumber, Availability availability,
            double price_per_day) {
                
        this();
        this.name = name;
        this.type = type;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.availability = availability;
        this.price_per_day = price_per_day;
    }
    
    public Vehicle(String name, VehicleType type,String registrationNumber, Availability availability,
            double price_per_day) {
        this(name, type, null, registrationNumber, availability, price_per_day);
    }

    public int getVehicle_id() {
        return vehicle_id;
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

    public String getRegistration_number() {
        return registrationNumber;
    }

    public void setRegistration_number(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public double getPrice_per_day() {
        return price_per_day;
    }

    public void setPrice_per_day(double price_per_day) {
        this.price_per_day = price_per_day;
    }

    public void addBooking(Booking b){
        bookingsByVehicle.add(b);
    }

    public void removeBooking(Booking b){
        bookingsByVehicle.remove(b);
    }

    public void addReview(Review b){
        reviewsOnVehicle.add(b);
    }

    public void removeReview(Review b){
        reviewsOnVehicle.remove(b);
    }

    public List<Booking> getBookingsByVehicle() {
        return bookingsByVehicle;
    }

    public List<Review> getReviewsOnVehicle() {
        return reviewsOnVehicle;
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
    

    @Override
    public String toString() {
        return "Vehicle [vehicle_id=" + vehicle_id + ", name=" + name + ", type=" + type + ", model=" + model
                + ", registrationNumber=" + registrationNumber + ", availability=" + availability + ", price_per_day="
                + price_per_day + "]";
    }
    
}
