package com.unizar.major.controller;

import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Period;
import com.unizar.major.domain.User;
import com.unizar.major.repository.BookingRepository;
import com.unizar.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/{id}/booking/create")

    public String create(@PathVariable long id,@RequestBody Booking booking){
        User user = userRepository.findById(id);
        Period period = new Period(booking.getPeriod().getstartDate(),booking.getPeriod().getEndDate());
        bookingRepository.save(new Booking(booking.getIsPeriodic(), booking.getReason(),period, user));

        return "Booking is created";

    }

    @GetMapping("/booking/findAll")

    public List<Booking> findAll(){

        List<Booking> booking = bookingRepository.findAll();

        return booking;

    }
}
