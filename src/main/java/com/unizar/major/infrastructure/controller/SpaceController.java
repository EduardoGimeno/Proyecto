package com.unizar.major.infrastructure.controller;

import com.unizar.major.application.dtos.BookingDto;
import com.unizar.major.application.dtos.BookingDtoReturn;
import com.unizar.major.application.dtos.SpaceDto;
import com.unizar.major.application.service.BookingService;
import com.unizar.major.application.service.SpaceService;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Space;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SpaceController {

    @Autowired
    SpaceService spaceService;

    Logger logger = LoggerFactory.getLogger(BookingService.class);

    @GetMapping("/spaces")
    public List<SpaceDto> getAllSpaces(){
        List<Space> space =spaceService.getAllSpaces();
        List<SpaceDto> spaceDtos = new ArrayList<>();

        for (Space s : space) {
            spaceDtos.add(convertDto(s));
        }

        return spaceDtos;

    }

    @GetMapping("/space/{id}")
    public SpaceDto getAllSpaces(@PathVariable int id){
        Optional<Space> space =spaceService.getSpaceByGid(id);

        return convertDto(space.get());

    }

    @GetMapping("/space/{id}/bookings")
    public List<BookingDtoReturn> getBookingByIdSpace(@PathVariable int id) {
        List<Booking> bookings = spaceService.getBookingByIdSpace(id);
        List<BookingDtoReturn> bookingDtosReturn = new ArrayList<>();

        for (Booking b : bookings) {
            bookingDtosReturn.add(convertDtoBooking(b));
        }
        return bookingDtosReturn;
    }

    private SpaceDto convertDto(Space space) {
        ModelMapper modelMapper = new ModelMapper();
        SpaceDto spaceDto = modelMapper.map(space, SpaceDto.class);
        return spaceDto;
    }

    private BookingDtoReturn convertDtoBooking(Booking booking) {
        ModelMapper modelMapper = new ModelMapper();
        BookingDtoReturn bookingDto = modelMapper.map(booking, BookingDtoReturn.class);
        return bookingDto;
    }

}
