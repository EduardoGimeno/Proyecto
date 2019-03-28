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


    @PostMapping("/booking")

    public String createNewBooking(@RequestParam("id") Long id,@RequestBody Booking booking){
        User user = userRepository.findById(id);
        Period period = new Period(booking.getPeriod().getstartDate(),booking.getPeriod().getEndDate());
        Booking booking1 = new Booking(booking.getIsPeriodic(), booking.getReason(),period);
        booking1.setUser(user);



        bookingRepository.save(booking1);

        return "Booking is created";

    }

    @GetMapping("/booking/{id}")

    public Booking getBookingById(@PathVariable Long id){

        Booking booking = bookingRepository.findById(id);

        return booking;

    }

    @PutMapping("/booking/{id}")
    public String updateBooking(@PathVariable long id, @RequestBody Booking booking){

        bookingRepository.setBookingInfoById(booking.getIsPeriodic(),booking.getReason(),booking.getPeriod().getEndDate(),booking.getPeriod().getstartDate(),id);

        return "Booking is update";

    }

    @DeleteMapping("/booking/{id}")
    public String deleteBooking(@PathVariable long id){

        Booking booking = bookingRepository.findById(id);

        bookingRepository.delete(booking);

        return "Booking is deleted";

    }

    @GetMapping("/bookings")

    public List<Booking> getAllBookings(){

        List<Booking> booking = bookingRepository.findAll();

        return booking;

    }
}
