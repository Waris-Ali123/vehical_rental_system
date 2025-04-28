package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.User.Role;
import com.capstone1.vehical_rental_system.mappers.BookingMapper;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.repositories.BookingRepo;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    @Autowired
    private BookingMapper bookingMapper;

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

    public ResponseEntity<?> addBooking(
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

            double totalPrice = vehicle.getPrice_per_day() * days;
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setVehicle(vehicle);
            booking.setBookingTime(LocalDateTime.now());
            booking.setStartDate(start);
            booking.setEndDate(end);
            booking.setTotalPrice(totalPrice);
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            Booking bookSaved = bookingRepo.save(booking);
            user.addBooking(bookSaved);
            vehicle.addBooking(bookSaved);

            return ResponseEntity.ok().body(bookingMapper.toDTO(bookSaved));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while booking");
        }
    }

    public ResponseEntity<?> getBookings(String email) {
        try {
            User user = loginService.getUserByEmail(email);
            List<Booking> bookingsList = bookingRepo.findByUser(user);
            return ResponseEntity.ok(bookingMapper.toDTOList(bookingsList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>());
        }
    }

    public ResponseEntity<?> getBookingsByRegistrationNumber(
            String registrationNumber) {
        try {
            Vehicle vehicle = 
                    vehicleService.getByRegistrationNumber(registrationNumber);
            List<Booking> bookingsList = 
                    bookingRepo.findByVehicleAndBookingStatus(
                            vehicle, BookingStatus.CONFIRMED);
            return ResponseEntity.ok(bookingMapper.toDTOList(bookingsList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch the bookings for this vehicle!");
        }
    }

    public ResponseEntity<List<BookingDTO>> getAllBookings(String email) {
        try {
            User user = loginService.getUserByEmail(email);
            if (user.getRole() == Role.ADMIN) {
                List<Booking> allBookings = bookingRepo.findAll();
                return ResponseEntity.ok(bookingMapper.toDTOList(allBookings));
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

    public ResponseEntity<String> cancelBooking(final String password,final int id) {
        try {
            final int bookingId = id;

            Booking booking = 
            bookingRepo.findById(bookingId).orElseThrow();

            String bookerEmail = booking.getUser().getEmail();
            
            UserDTO userAuthenticated = loginService.getUserByEmailAndPass(bookerEmail,password);
            
            if (userAuthenticated == null) {
                System.out.println("This user has no access to cancel the booking of another user");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access!");
            }else{   
                booking.setBookingStatus(BookingStatus.CANCELED);
                bookingRepo.save(booking);
                return ResponseEntity.ok("The booking has been canceled successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong!");
        }
    }

    public ResponseEntity<List<BookingDTO>> searchBookings(String keyword) {
        try {
            List<Booking> bookings = 
                    bookingRepo.searchingByKeyword(keyword);
            return ResponseEntity.ok(bookingMapper.toDTOList(bookings));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
