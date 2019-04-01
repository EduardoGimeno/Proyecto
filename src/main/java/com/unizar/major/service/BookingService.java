package com.unizar.major.service;

import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Period;
import com.unizar.major.domain.User;
import com.unizar.major.repository.BookingRepository;
import com.unizar.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public BookingService(){}

    public String createNewBooking(Long id, Booking booking){
        User user = userRepository.findById(id).get();
        Period period = new Period(booking.getPeriod().getstartDate(),booking.getPeriod().getEndDate());
        Booking booking1 = new Booking(booking.getIsPeriodic(), booking.getReason(),period);
        booking1.setUser(user);

        bookingRepository.save(booking1);

        return "Booking is created";

    }

    public Optional<Booking> getBookingById(long id){

        return bookingRepository.findById(id);

    }

    public String updateBooking(long id, Booking booking){

        bookingRepository.setBookingInfoById(booking.getIsPeriodic(),booking.getReason(),booking.getPeriod().getEndDate(),booking.getPeriod().getstartDate(),id);

        return "Booking is update";

    }

    public String deleteBooking(long id){

        Optional<Booking> booking = bookingRepository.findById(id);

        booking.ifPresent(booking1 -> bookingRepository.delete(booking1));

        return "Booking is deleted";

    }

    public List<Booking> getAllBookings(){

        List<Booking> booking = bookingRepository.findAll();

        return booking;

    }
}
