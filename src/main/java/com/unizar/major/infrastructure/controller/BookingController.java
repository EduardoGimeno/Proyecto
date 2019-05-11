package com.unizar.major.infrastructure.controller;

import com.unizar.major.application.dtos.BookingDtoReturn;
import com.unizar.major.application.service.UserService;
import com.unizar.major.domain.Booking;
import com.unizar.major.application.dtos.BookingDto;
import com.unizar.major.application.service.BookingService;
import com.unizar.major.domain.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
/*
@RestController
public class BookingController {


    @Autowired
    BookingService bookingService;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(BookingService.class);

    @PostMapping("/booking")

    public String createNewBooking(@RequestParam("id") Long id,@RequestBody BookingDto bookingDto){

        Optional <User> u = userService.getUser(id);

        if (u == null){
            return "User not exists";
        }
        else{
            if (u.get().isActive()){

                if (bookingDto.getPeriod().getstartDate().compareTo(bookingDto.getPeriod().getEndDate())>0){
                    return "Error, period is incorrect";
                }
                else{

                    if (!bookingDto.isIsPeriodic()){
                        return bookingService.createNewBooking(id,bookingDto);
                    }
                    else{
                        if (bookingDto.getFinalDate().compareTo(bookingDto.getPeriod().getEndDate())<0) {
                            return "Error, finalDate is incorrect";
                        }
                        else {
                            return bookingService.createNewBookingPeriodic(id, bookingDto);
                        }
                    }
                }

            }
            else{
                return "User is not active in the system";
            }

        }


    }

    @GetMapping("/booking/{id}")

    public BookingDtoReturn getBookingById(@PathVariable Long id){

       Optional <Booking> b = bookingService.getBookingById(id);
        if (b == null){
            return null;
        }
        else {
            return convertDto(b.get());
        }


    }

    @PutMapping("/booking/{id}")
    public String updateBooking(@PathVariable long id, @RequestBody BookingDto bookingDto){

        return bookingService.updateBooking(id, bookingDto);
    }

    @PutMapping("/booking/{id}/validate")
    public String validateBooking(@PathVariable long id){

        return bookingService.validateBooking(id);
    }

    @PutMapping("/booking/{id}/cancel")
    public String cancelBooking(@PathVariable long id){

        return bookingService.cancelBooking(id);
    }

    @DeleteMapping("/booking/{id}")
    public String deleteBooking(@PathVariable long id){

        return bookingService.deleteBooking(id);

    }

    @GetMapping("/bookings")

    public List<BookingDtoReturn> getAllBookings(){

        List<Booking> booking = bookingService.getAllBookings();
        List<BookingDtoReturn> bookingDtosReturn = new ArrayList<>();

        for (Booking b : booking) {
            bookingDtosReturn.add(convertDto(b));
        }

        return bookingDtosReturn;

    }


    @GetMapping("/bookingspending")
    public List<BookingDtoReturn> getBookingPending(){
        List<Booking>bookings= bookingService.getBookingPending();
        List<BookingDtoReturn> bookingDtosReturn = new ArrayList<>();

        for (Booking b : bookings) {
            bookingDtosReturn.add(convertDto(b));
        }
        return bookingDtosReturn;

    }


    private Booking convertToEntity(BookingDto bookingDto) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        return booking;
    }

    private BookingDtoReturn convertDto(Booking booking) {
        ModelMapper modelMapper = new ModelMapper();
        BookingDtoReturn bookingDtoReturn = modelMapper.map(booking, BookingDtoReturn.class);

        return bookingDtoReturn;
    }

}
*/