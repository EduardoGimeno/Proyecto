package com.unizar.major.application.service;

import com.unizar.major.application.dtos.BookingDto;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Period;
import com.unizar.major.domain.Space;
import com.unizar.major.domain.User;
import com.unizar.major.domain.repository.BookingRepository;
import com.unizar.major.domain.repository.SpaceRepository;
import com.unizar.major.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService() {
    }

    @Transactional
    public Boolean createNewBooking(Long id, BookingDto bookingDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();

            Period period = new Period(bookingDto.getPeriod().getstartDate(), bookingDto.getPeriod().getEndDate());
            List<Period> p = new ArrayList<>();
            p.add(period);

            Booking booking = new Booking(bookingDto.isIsPeriodic(), bookingDto.getReason(), p);
            booking.setActive(true);
            booking.setState("inicial");
            booking.setFinalDate(null);
            booking.setPeriodRep(null);
            booking.setUser(u);

            for (int i = 0; i < bookingDto.getSpaces().size(); i++) {
                Optional<Space> space = spaceRepository.findByGid(bookingDto.getSpaces().get(i));
                booking.setSpaces(space.get());
            }

            booking.setEspecial(bookingDto.isEspecial());

            bookingRepository.save(booking);
            return true;
        }
        return false; //404
    }

    @Transactional
    public Boolean createNewPeriodicBooking(Long id, BookingDto bookingDto) {
        Optional<User> user = userRepository.findById(id);
        List<Space> spaces = new ArrayList<>();
        if (user.isPresent()) {
            User u = user.get();
            List<Period> p = calculatePeriods(bookingDto.getPeriodRep(), bookingDto.getFinalDate(), bookingDto.getPeriod().getstartDate(), bookingDto.getPeriod().getEndDate());
            Booking booking = new Booking(bookingDto.isIsPeriodic(), bookingDto.getReason(), p, bookingDto.getPeriodRep(), bookingDto.getFinalDate());
            booking.setActive(true);
            booking.setState("inicial");
            booking.setUser(u);

            for (int i = 0; i < bookingDto.getSpaces().size(); i++) {
                Optional<Space> space = spaceRepository.findByGid(bookingDto.getSpaces().get(i));
                booking.setSpaces(space.get());
            }

            if (u.getRol() == User.Rol.ADMIN) {
                booking.setEspecial(bookingDto.isEspecial());
            } else {
                booking.setEspecial(false);
            }

            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

    public boolean cumplePolitica(BookingDto bookingDto) {

        return true;

    }

    public List<Period> calculatePeriods(String periodRep, Date finalDate, Date startDate, Date endDate) {


        List<Period> p = new ArrayList<>();
        Calendar calendar;

        Period period = new Period();
        period.setstartDate(startDate);
        period.setEndDate(endDate);
        p.add(period);

        switch (periodRep) {

            case "diaria":

                calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, 1);

                while (calendar.getTime().compareTo(finalDate) < 0) {

                    Period period2 = new Period();
                    period2.setstartDate(calendar.getTime());
                    startDate = calendar.getTime();
                    calendar.setTime(endDate);
                    calendar.add(Calendar.DATE, 1);
                    period2.setEndDate(calendar.getTime());
                    endDate = calendar.getTime();
                    p.add(period2);
                    calendar.setTime(startDate);
                    calendar.add(Calendar.DATE, 1);
                }

                break;

            case "semanal":


                calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, 7);

                while (calendar.getTime().compareTo(finalDate) < 0) {

                    Period period2 = new Period();
                    period2.setstartDate(calendar.getTime());
                    startDate = calendar.getTime();
                    calendar.setTime(endDate);
                    calendar.add(Calendar.DATE, 7);
                    period2.setEndDate(calendar.getTime());
                    endDate = calendar.getTime();
                    p.add(period2);
                    calendar.setTime(startDate);
                    calendar.add(Calendar.DATE, 7);
                }

                break;
            case "mensual":

                calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int semana = calendar.get(Calendar.WEEK_OF_MONTH);
                int dia = calendar.get(Calendar.DAY_OF_WEEK);


                //calendar.setMinimalDaysInFirstWeek(1);
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_WEEK, dia);
                calendar.set(Calendar.WEEK_OF_MONTH, semana);

                while (calendar.getTime().compareTo(finalDate) < 0) {

                    Period period2 = new Period();
                    period2.setstartDate(calendar.getTime());
                    startDate = calendar.getTime();
                    calendar.setTime(endDate);
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_WEEK, dia);
                    calendar.set(Calendar.WEEK_OF_MONTH, semana);
                    period2.setEndDate(calendar.getTime());
                    endDate = calendar.getTime();
                    p.add(period2);
                    calendar.setTime(startDate);
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_WEEK, dia);
                    calendar.set(Calendar.WEEK_OF_MONTH, semana);
                }

                break;
            case "quincenal":

                calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.DATE, 14);

                while (calendar.getTime().compareTo(finalDate) < 0) {

                    Period period2 = new Period();
                    period2.setstartDate(calendar.getTime());
                    startDate = calendar.getTime();
                    calendar.setTime(endDate);
                    calendar.add(Calendar.DATE, 14);
                    period2.setEndDate(calendar.getTime());
                    endDate = calendar.getTime();
                    p.add(period2);
                    calendar.setTime(startDate);
                    calendar.add(Calendar.DATE, 14);
                }

                break;

        }
        return p;
    }


    public Optional<Booking> getBookingById(long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public Boolean updateBooking(long id, BookingDto bookingDto) {

        Optional<Booking> b = bookingRepository.findById(id);
        if (b.isPresent()) {
            Booking booking = b.get();
            List<Period> p = calculatePeriods(bookingDto.getPeriodRep(), bookingDto.getFinalDate(), bookingDto.getPeriod().getstartDate(), bookingDto.getPeriod().getEndDate());
            booking.setPeriod(p);
            booking.setPeriodRep(bookingDto.getPeriodRep());
            booking.setIsPeriodic(bookingDto.isIsPeriodic());
            booking.setReason(bookingDto.getReason());
            booking.setFinalDate(bookingDto.getFinalDate());
            booking.getSpaces().clear();
            for (int i = 0; i < bookingDto.getSpaces().size(); i++) {
                Optional<Space> space = spaceRepository.findByGid(bookingDto.getSpaces().get(i));
                booking.setSpaces(space.get());
            }
            bookingRepository.save(booking);

            return true;
        }
        return false; // 404
    }

    @Transactional
    public Boolean deleteBooking(long id) {

        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isPresent()) {
            Booking b = booking.get();
            b.setActive(false);
            bookingRepository.save(b);
            return true;
        }
        return false; // Not found
    }

    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();

    }

    public List<Booking> getBookingPending() {
        return bookingRepository.findByState("inicial");
    }

    @Transactional
    public Boolean validateBooking(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            Booking b = booking.get();
            b.setState("valida");
            bookingRepository.save(b);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean cancelBooking(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            Booking b = booking.get();
            b.setState("invalida");
            b.setActive(false);
            bookingRepository.save(b);
            return true;
        }
        return false;

    }

    public long getBookingOwnerByID (long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return (booking.isPresent() ? booking.get().getUser().getId() : -1 );
    }
}
