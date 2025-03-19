package com.capstone1.vehical_rental_system.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.vehical_rental_system.controllers.BookingController;
import com.capstone1.vehical_rental_system.entities.Booking;
import com.capstone1.vehical_rental_system.entities.User;
import com.capstone1.vehical_rental_system.entities.Vehicle;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.User.Role;
import com.capstone1.vehical_rental_system.repositories.BookingRepo;
import com.capstone1.vehical_rental_system.repositories.VehicleRepo;


@Service
public class BookingServiceImplementation implements BookingService {



    @Autowired
    VehicleRepo vehicleRepo;
    @Autowired
    BookingRepo bookingRepo;
    @Autowired
    LoginService loginService;
    @Autowired
    VehicleService vehicleService;



    public ResponseEntity<String> searchForExistingBookings(String registration_no,LocalDate starDate,LocalDate endDate){
        Vehicle vehicle = vehicleService.getByRegistrationNumber(registration_no);

        if(starDate.isAfter(endDate))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start Date must be before end date");

        List<Booking> existingBookings = bookingRepo.findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqual(vehicle,endDate,starDate);
        if (existingBookings.size()>0) {
            String[] bookedArr = new String[existingBookings.size()];
            Arrays.setAll(bookedArr, (i)->existingBookings.get(i).getStartDate().toString()+" and "+existingBookings.get(i).getEndDate().toString());
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                """
                    The Vehicle "%s" is already booked between %s 
                    
                """.formatted(registration_no,String.join(" , ", bookedArr))
            );
        }

        return ResponseEntity.ok("It is Available now");
    }


    public ResponseEntity<String> addBooking(String email, String registration_no, String startDate,String endDate){
        try {
            User u1 = loginService.getUserByEmail(email);
            Vehicle v1 = vehicleService.getByRegistrationNumber(registration_no);
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            //Checking for already booked case
            ResponseEntity<String> isAvailable = this.searchForExistingBookings(registration_no,start,end);

            if(isAvailable.getStatusCode()!=HttpStatusCode.valueOf(200))
                return isAvailable;



            //calculating total price;
            long days = ChronoUnit.DAYS.between(start,end)+1; //including the initial too
            System.out.println("Total Days are : " + days);
            double totalPrice = v1.getPrice_per_day()*days;


            Booking booking = new Booking();
            booking.setUser(u1);
            booking.setVehicle(v1);
            booking.setStartDate(start);
            booking.setEndDate(end);
            booking.setTotalPrice(totalPrice);
            booking.setBooking_status(BookingStatus.CONFIRMED);

            Booking bookSaved = bookingRepo.save(booking);
            u1.addBooking(bookSaved);
            v1.addBooking(bookSaved);
            System.out.println(bookSaved);

            return ResponseEntity.ok("Successfully Booked");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while booking");
        }

    }


    
    public ResponseEntity<List<Booking>> getBookings(String email){

        try {
            User user = loginService.getUserByEmail(email);
            List<Booking> bookingsList = bookingRepo.findByUser(user);
            return ResponseEntity.ok().body(bookingsList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }

    }


    public ResponseEntity<List<Booking>> getAllBooks(String email){
        try {
            User user = loginService.getUserByEmail(email);
            if(user.getRole()==Role.ADMIN)
                return ResponseEntity.ok().body( (List<Booking>) bookingRepo.findAll() );
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    
}
