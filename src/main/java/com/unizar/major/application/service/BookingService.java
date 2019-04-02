package com.unizar.major.application.service;

import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Period;
import com.unizar.major.domain.User;
import com.unizar.major.domain.repository.BookingRepository;
import com.unizar.major.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public BookingService(){}

    @Transactional
    public String createNewBooking(Long id, Booking booking){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            User u = user.get();
            Period period = new Period(booking.getPeriod().getstartDate(),booking.getPeriod().getEndDate());
            Booking booking1 = new Booking(booking.getIsPeriodic(), booking.getReason(),period);
            booking1.setUser(u);

            bookingRepository.save(booking1);
        }
        else{
            return "User with id "+ id + "not exist";
        }

        return "Booking is created";

    }

    public Optional<Booking> getBookingById(long id){

        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            return booking;
        }
        else {
            return null;
        }

    }

    @Transactional
    public String updateBooking(long id, Booking booking){

        bookingRepository.setBookingInfoById(booking.getIsPeriodic(),booking.getReason(),booking.getPeriod().getEndDate(),booking.getPeriod().getstartDate(),id);

        return "Booking is update";

    }

    @Transactional
    public String deleteBooking(long id){

        Optional<Booking> booking = bookingRepository.findById(id);

        if(booking.isPresent()){
            Booking b = booking.get();
            bookingRepository.delete(b);
            return "Booking is deleted";
        }else{
            return "Booking not exist";
        }


    }

    public List<Booking> getAllBookings(){

        List<Booking> booking = bookingRepository.findAll();

        return booking;

    }
}
