package com.capstone1.vehical_rental_system.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    public enum VehicleType{
        CAR,BIKE,TRUCK;
    }

    public enum Availability{
        AVAILABLE,BOOKED,UNDER_MAINTENANCE;
    }

    // private static int lastVehicleId = 1;

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
    private String registration_number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Availability availability;

    @Column(nullable = false)
    private double price_per_day;

  



    public Vehicle(){
        // this.vehicle_id = lastVehicleId++;
    }

    public Vehicle(String name, VehicleType type, String model, String registration_number, Availability availability,
            double price_per_day) {
                
        this();
        this.name = name;
        this.type = type;
        this.model = model;
        this.registration_number = registration_number;
        this.availability = availability;
        this.price_per_day = price_per_day;
        // this.bookings.add(new Booking(LocalDate.now(),LocalDate.now(ZoneId.systemDefault()),133.98,BookingStatus.CONFIRMED));
    }
    
    public Vehicle(String name, VehicleType type,String registration_number, Availability availability,
            double price_per_day) {
        this(name, type, null, registration_number, availability, price_per_day);
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
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
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

  
    @Override
    public String toString() {
        return "Vehicle [vehicle_id=" + vehicle_id + ", name=" + name + ", type=" + type + ", model=" + model
                + ", registration_number=" + registration_number + ", availability=" + availability + ", price_per_day="
                + price_per_day + "]";
    }
    
}
