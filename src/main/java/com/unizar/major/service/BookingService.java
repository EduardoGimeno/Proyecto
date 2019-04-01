package com.unizar.major.service;

import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Period;
import com.unizar.major.domain.User;
import com.unizar.major.repository.BookingRepository;
import com.unizar.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public BookingService(){}

    public String createNewBooking(long id, Booking booking){
        User user = userRepository.findById(id);
        Period period = new Period(booking.getPeriod().getstartDate(),booking.getPeriod().getEndDate());
        Booking booking1 = new Booking(booking.getIsPeriodic(), booking.getReason(),period);
        booking1.setUser(user);

        bookingRepository.save(booking1);

        return "Booking is created";

    }

    public Booking getBookingById(long id){

        Booking booking = bookingRepository.findById(id);

        return booking;

    }

    public String updateBooking(long id, Booking booking){

        bookingRepository.setBookingInfoById(booking.getIsPeriodic(),booking.getReason(),booking.getPeriod().getEndDate(),booking.getPeriod().getstartDate(),id);

        return "Booking is update";

    }

    public String deleteBooking(long id){

        Booking booking = bookingRepository.findById(id);

        bookingRepository.delete(booking);

        return "Booking is deleted";

    }

    public List<Booking> getAllBookings(){

        List<Booking> booking = bookingRepository.findAll();

        return booking;

    }
}
