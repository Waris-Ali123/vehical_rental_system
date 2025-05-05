package com.capstone1.vehical_rental_system.feignclients;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.entities.Booking.BookingStatus;
import com.capstone1.vehical_rental_system.entities.Vehicle;

@FeignClient(name = "booking-service", url = "http://localhost:8080/booking")
public interface BookingServiceFeignClient {

    @GetMapping("/searchForUpcomingOrCurrentBookings")
    List<BookingDTO> searchForUpcomingOrCurrentBookings(
            @RequestParam("vehicleId") int vehicleId,
            @RequestParam("endDate") String endDate,
            @RequestParam("status") String status
    );
}