package com.capstone1.vehical_rental_system.mappers;

import com.capstone1.vehical_rental_system.dtos.ReviewDTO;
import com.capstone1.vehical_rental_system.entities.Review;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    // Map Review to ReviewDTO
    @Mapping(target = "user", source = "user") // Adjust if nested mapping is required
    @Mapping(target = "vehicle", source = "vehicle") // Adjust if nested mapping is required
    ReviewDTO toDTO(Review review);

    @Mapping(source = "reviewId", target = "reviewId")
    List<ReviewDTO> toDTOList(List<Review> reviewEntities);

    // Map ReviewDTO to Review
    @Mapping(target = "user", source = "user") // Adjust if nested mapping is required
    @Mapping(target = "vehicle", source = "vehicle") // Adjust if nested mapping is required
    Review toEntity(ReviewDTO reviewDTO);
}