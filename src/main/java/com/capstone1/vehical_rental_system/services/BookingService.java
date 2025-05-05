package com.capstone1.vehical_rental_system.services;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.entities.Vehicle;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO addBooking(final String email, final String registrationNumber, final String startDate, final String endDate);

    List<BookingDTO> getBookings(final String email);

    List<BookingDTO> getBookingsByRegistrationNumber(final String registrationNumber);

    List<BookingDTO> getAllBookings(final String email);

    String cancelBooking(final String password, final int id);

    List<BookingDTO> searchBookings(final String keyword);

    List<BookingDTO> searchForUpcomingOrCurrentBookings(int vehicleId,LocalDate endDate,String status);
}