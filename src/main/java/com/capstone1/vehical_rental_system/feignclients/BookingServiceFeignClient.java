package com.capstone1.vehical_rental_system.feignclients;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.Vehicle;

import jakarta.validation.constraints.NotBlank;

@Validated
@FeignClient(name = "booking-service", url = "http://localhost:8080/booking")
public interface BookingServiceFeignClient {

        @GetMapping("/searchForUpcomingOrCurrentBookings")
        List<BookingDTO> searchForUpcomingOrCurrentBookings(
                        @RequestParam("vehicleId") int vehicleId,
                        @RequestParam("endDate") String endDate,
                        @RequestParam("status") String status);

        @GetMapping("/getByRegistrationNumber")
        public ResponseEntity<List<BookingDTO>> getBookingHistoryByVehicle(
                        @RequestParam @NotBlank(message = "Registration number cannot be blank") final String registrationNumber);

        @PutMapping("/dissociateVehicle")
        ResponseEntity<Void> dissociateVehicleFromBookings(
                        @RequestParam("registrationNumber") String registrationNumber);

        @PutMapping("/dissociateUser")
        ResponseEntity<Void> dissociateUserFromBookings(@RequestParam("userId") int userId);

        @GetMapping("/searchForUpcomingOrCurrentBookingsUsingUserId")
        List<BookingDTO> searchForUpcomingOrCurrentBookingsUsingUserId(
                @RequestParam("userId") int userId,
                @RequestParam("endDate") String endDate,
                @RequestParam("status") String status
    );
}