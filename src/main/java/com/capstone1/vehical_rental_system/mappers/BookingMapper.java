package com.capstone1.vehical_rental_system.mappers;

import com.capstone1.vehical_rental_system.dtos.BookingDTO;
import com.capstone1.vehical_rental_system.entities.Booking;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, VehicleMapper.class})
public interface BookingMapper {

    @Mapping(source = "booking_id", target = "bookingId")
    BookingDTO toDTO(Booking booking);


    @Mapping(source = "booking_id", target = "bookingId")
    List<BookingDTO> toDTOList(List<Booking> booking);

    // @Mapping(source = "bookingId", target = "booking_id")
    // Booking toEntity(BookingDTO bookingDTO);
}
