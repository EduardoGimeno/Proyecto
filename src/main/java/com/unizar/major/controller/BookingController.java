package com.unizar.major.controller;

import com.unizar.major.domain.Booking;
import com.unizar.major.dtos.BookingDto;
import com.unizar.major.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
public class BookingController {


    @Autowired
    BookingService bookingService;


    @PostMapping("/booking")

    public String createNewBooking(@RequestParam("id") Long id,@RequestBody BookingDto bookingDto){

        Booking booking = new Booking();
        try {
            booking = convertToEntity(bookingDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bookingService.createNewBooking(id,booking);

    }

    @GetMapping("/booking/{id}")

    public BookingDto getBookingById(@PathVariable Long id){

        Optional<Booking> b = bookingService.getBookingById(id);
        if (b == null){
            return null;
        }
        else {
            return convertDto(b);
        }


    }

    @PutMapping("/booking/{id}")
    public String updateBooking(@PathVariable long id, @RequestBody BookingDto bookingDto){

        Booking booking = new Booking();
        try {
            booking = convertToEntity(bookingDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return bookingService.updateBooking(id, booking);
    }

    @DeleteMapping("/booking/{id}")
    public String deleteBooking(@PathVariable long id){

        return bookingService.deleteBooking(id);

    }

    @GetMapping("/bookings")

    public List<BookingDto> getAllBookings(){

        List<Booking> booking = bookingService.getAllBookings();
        List<BookingDto> bookingDtos = new ArrayList<>();

        for (Booking b : booking) {
            bookingDtos.add(convertDto(Optional.ofNullable(b)));
        }

        return bookingDtos;

    }

    private Booking convertToEntity(BookingDto bookingDto) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        return booking;
    }

    private BookingDto convertDto(Optional<Booking> booking) {
        ModelMapper modelMapper = new ModelMapper();
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
        return bookingDto;
    }

}
