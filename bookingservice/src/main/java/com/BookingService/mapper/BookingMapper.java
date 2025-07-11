package com.BookingService.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BookingService.Dto.BookingDTO;
import com.BookingService.Entities.Booking;

@Component
public class BookingMapper {

    @Autowired
    private ModelMapper modelMapper;

    public BookingDTO toDto(Booking booking) {
        return modelMapper.map(booking, BookingDTO.class);
    }

    public Booking toEntity(BookingDTO dto) {
        return modelMapper.map(dto, Booking.class);
    }
}
