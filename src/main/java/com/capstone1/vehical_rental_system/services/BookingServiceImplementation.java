package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.dtos.UserDTO;
import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.User.Role;
import com.capstone1.vehical_rental_system.mappers.BookingMapper;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.exceptions.ResourceNotFoundException;
import com.capstone1.vehical_rental_system.repositories.BookingRepo;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingServiceImplementation implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImplementation.class);

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

    @Override
    public BookingDTO addBooking(String email, String registrationNo, String startDate, String endDate) {
        logger.info("Attempting to add a booking for user with email: {}", email);
        User user = loginService.getUserByEmail(email);
        Vehicle vehicle = vehicleService.getByRegistrationNumber(registrationNo);

        if(vehicle.getAvailability()==Vehicle.Availability.UNDER_MAINTENANCE){
            logger.warn("The vehicle with registration number: {} is currently under maintenance.", registrationNo);
            throw new IllegalArgumentException("The vehicle is currently under maintenance and cannot be booked.");
        }
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        logger.info("Checking availability for vehicle with registration number: {} between {} and {}", registrationNo, start, end);
        List<Booking> existingBookings = bookingRepo.findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndBookingStatus(
                vehicle, end, start, BookingStatus.CONFIRMED);
        if (!existingBookings.isEmpty()) {
            logger.warn("The vehicle with registration number: {} is already booked for the selected dates.", registrationNo);
            throw new IllegalArgumentException("The vehicle is already booked for the selected dates.");
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

        Booking savedBooking = bookingRepo.save(booking);
        logger.info("Booking successfully created with ID: {} for user with email: {}", savedBooking.getBooking_id(), email);
        return bookingMapper.toDTO(savedBooking);
    }

    @Override
    public List<BookingDTO> getBookings(String email) {
        logger.info("Fetching booking history for user with email: {}", email);
        User user = loginService.getUserByEmail(email);
        List<Booking> bookingsList = bookingRepo.findByUser(user);
        logger.info("Found {} bookings for user with email: {}", bookingsList.size(), email);
        return bookingMapper.toDTOList(bookingsList);
    }

    @Override
    public List<BookingDTO> getBookingsByRegistrationNumber(String registrationNumber) {
        logger.info("Fetching bookings for vehicle with registration number: {}", registrationNumber);
        Vehicle vehicle = vehicleService.getByRegistrationNumber(registrationNumber);
        List<Booking> bookingsList = bookingRepo.findByVehicleAndBookingStatus(vehicle, BookingStatus.CONFIRMED);
        logger.info("Found {} bookings for vehicle with registration number: {}", bookingsList.size(), registrationNumber);
        return bookingMapper.toDTOList(bookingsList);
    }

    @Override
    public List<BookingDTO> getAllBookings(String email) {
        logger.info("Fetching all bookings for admin with email: {}", email);
        User user = loginService.getUserByEmail(email);
        if (user.getRole() == Role.ADMIN) {
            List<Booking> allBookings = bookingRepo.findAll();
            logger.info("Found {} total bookings in the system.", allBookings.size());
            return bookingMapper.toDTOList(allBookings);
        } else {
            logger.warn("Unauthorized access attempt to fetch all bookings by user with email: {}", email);
            throw new IllegalArgumentException("Only admin can view all bookings.");
        }
    }

    @Override
    public String cancelBooking(final String password, final int id) {
        logger.info("Attempting to cancel booking with ID: {}", id);
        Booking booking = bookingRepo.findById(id).orElseThrow(() -> {
            logger.error("Booking not found with ID: {}", id);
            return new IllegalArgumentException("Booking not found.");
        });

        String bookerEmail = booking.getUser().getEmail();
        logger.info("Authenticating user with email: {} to cancel booking with ID: {}", bookerEmail, id);
        UserDTO userAuthenticated = loginService.getUserByEmailAndPass(bookerEmail, password);
        if (userAuthenticated == null) {
            logger.warn("Unauthorized access attempt to cancel booking with ID: {}", id);
            throw new IllegalArgumentException("Unauthorized access to cancel the booking.");
        }

        booking.setBookingStatus(BookingStatus.CANCELED);
        bookingRepo.save(booking);
        logger.info("Booking with ID: {} has been successfully canceled.", id);
        return "The booking has been canceled successfully.";
    }

    @Override
    public List<BookingDTO> searchBookings(String keyword) {
        logger.info("Searching bookings with keyword: {}", keyword);
        List<Booking> bookings = bookingRepo.searchingByKeyword(keyword);
        logger.info("Found {} bookings matching keyword: {}", bookings.size(), keyword);
        return bookingMapper.toDTOList(bookings);
    }

    public List<BookingDTO> searchForUpcomingOrCurrentBookings(int vehicleId,LocalDate endDate,String status) {
        BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));
        logger.info("Searching for {} bookings for vehicle with registration number: {}", status, vehicle.getRegistrationNumber());
        List<Booking> bookings = bookingRepo.findByVehicleAndEndDateGreaterThanEqualAndBookingStatus(vehicle, endDate, bookingStatus);
        logger.info("Found {} {} bookings for vehicle with registration number: {}", bookings.size(), status, vehicle.getRegistrationNumber());
        return bookingMapper.toDTOList(bookings);
    }
}