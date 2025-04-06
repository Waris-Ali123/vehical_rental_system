package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.User.Role;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.repositories.BookingRepo;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImplementation implements BookingService {

    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private LoginService loginService;
    @Autowired
    private VehicleService vehicleService;

    public ResponseEntity<String> searchForExistingBookings(
            String registrationNo, LocalDate startDate, 
            LocalDate endDate) {
        Vehicle vehicle = 
                vehicleService.getByRegistrationNumber(registrationNo);
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Start Date must be before end date");
        }
        List<Booking> existingBookings = 
                bookingRepo.findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndBookingStatus(
                        vehicle, endDate, startDate, 
                        BookingStatus.CONFIRMED);
        if (!existingBookings.isEmpty()) {
            StringBuilder bookedDates = new StringBuilder();
            for (Booking booking : existingBookings) {
                bookedDates.append(booking.getStartDate())
                        .append(" to ")
                        .append(booking.getEndDate())
                        .append(", ");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The Vehicle \"" + registrationNo + 
                            "\" is already booked between " +
                            bookedDates);
        }
        return ResponseEntity.ok("It is Available now");
    }

    public ResponseEntity<String> addBooking(
            String email, String registrationNo, 
            String startDate, String endDate) {
        try {
            User user = loginService.getUserByEmail(email);
            Vehicle vehicle = 
                    vehicleService.getByRegistrationNumber(registrationNo);
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            ResponseEntity<String> isAvailable = 
                    this.searchForExistingBookings(registrationNo, start, end);
            if (!isAvailable.getStatusCode().is2xxSuccessful()) {
                return isAvailable;
            }
            long days = ChronoUnit.DAYS.between(start, end) + 1;
            System.out.println("Total Days are : " + days);
            double totalPrice = vehicle.getPrice_per_day() * days;
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setVehicle(vehicle);
            booking.setBookingTime(LocalDateTime.now());
            booking.setStartDate(start);
            booking.setEndDate(end);
            booking.setTotalPrice(totalPrice);
            booking.setBooking_status(BookingStatus.CONFIRMED);
            Booking bookSaved = bookingRepo.save(booking);
            user.addBooking(bookSaved);
            vehicle.addBooking(bookSaved);
            System.out.println(bookSaved);
            return ResponseEntity.ok("Successfully Booked");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while booking");
        }
    }

    public ResponseEntity<List<Booking>> getBookings(String email) {
        try {
            User user = loginService.getUserByEmail(email);
            List<Booking> bookingsList = bookingRepo.findByUser(user);
            return ResponseEntity.ok(bookingsList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }
    }

    public ResponseEntity<List<Booking>> getBookingsByRegistrationNumber(
            String registrationNumber) {
        try {
            Vehicle vehicle = 
                    vehicleService.getByRegistrationNumber(registrationNumber);
            List<Booking> bookingsList = 
                    bookingRepo.findByVehicleAndBookingStatus(
                            vehicle, BookingStatus.CONFIRMED);
            return ResponseEntity.ok(bookingsList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }
    }

    public ResponseEntity<List<Booking>> getAllBookings(String email) {
        try {
            User user = loginService.getUserByEmail(email);
            if (user.getRole() == Role.ADMIN) {
                return ResponseEntity.ok(bookingRepo.findAll());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ArrayList<>());
        }
    }

    public ResponseEntity<String> cancelBooking(int bookingId) {
        try {
            System.out.println("booking Id ; "+bookingId);
            Booking booking = 
                    bookingRepo.findById(bookingId).orElseThrow();
            booking.setBooking_status(BookingStatus.CANCELED);
            bookingRepo.save(booking);
            return ResponseEntity.ok("The booking has been canceled");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<Booking>> searchBookings(String keyword) {
        try {
            List<Booking> bookings = 
                    bookingRepo.searchingByKeyword(keyword);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
