package com.unizar.major.application.service;

import com.unizar.major.application.dtos.BookingCsv;
import com.unizar.major.application.dtos.BookingDto;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Period;
import com.unizar.major.domain.PersonaEina;
import com.unizar.major.domain.Space;
import com.unizar.major.domain.repository.BookingRepository;
import com.unizar.major.domain.repository.PersonaEinaRepository;
import com.unizar.major.domain.repository.SpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class BookingService {

    @Autowired
    private PersonaEinaRepository personaEinaRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService() {
    }

    @Transactional
    public Boolean createNewBooking(Long id, BookingCsv bookingcsv) {
        Optional<PersonaEina> user = personaEinaRepository.findById(id);
        if (user.isPresent()) {
            PersonaEina u = user.get();
            Booking booking = new Booking(false, bookingcsv.getReason(), bookingcsv.getPeriods());
            booking.setPersonaEina(u);
            booking.setState("valida");
            for (int i : bookingcsv.getSpaces()) {
                Optional<Space> space = spaceRepository.findByGid(i);
                space.ifPresent(booking::setSpaces);
            }
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean createNewBooking(Long id, BookingDto bookingDto) {
        Optional<PersonaEina> user = personaEinaRepository.findById(id);
        if (user.isPresent()) {
            if (keepPolicy(bookingDto,user.get().getRol())){
                PersonaEina u = user.get();

                Period period = new Period(bookingDto.getPeriod().getstartDate(), bookingDto.getPeriod().getEndDate());
                List<Period> p = new ArrayList<>();
                p.add(period);

                Booking booking = new Booking(bookingDto.isIsPeriodic(), bookingDto.getReason(), p);
                booking.setActive(true);
                booking.setState("inicial");
                booking.setFinalDate(null);
                booking.setPeriodRep(null);
                booking.setEspecial(bookingDto.isEspecial());
                booking.setPersonaEina(u);

                for (int i = 0; i < bookingDto.getSpaces().size(); i++) {
                    Optional<Space> space = spaceRepository.findByGid(bookingDto.getSpaces().get(i));
                    space.ifPresent(booking::setSpaces);
                }

                booking.setEspecial(bookingDto.isEspecial());

                bookingRepository.save(booking);
                return true;
            }
            else{
                return false;
            }


        }
        else{
            return false; //404
        }

    }

    @Transactional
    public Boolean createNewPeriodicBooking(Long id, BookingDto bookingDto) {
        Optional<PersonaEina> user = personaEinaRepository.findById(id);
        if (user.isPresent()) {
            if (keepPolicy(bookingDto,user.get().getRol())) {
                PersonaEina u = user.get();
                List<Period> p = calculatePeriods(bookingDto.getPeriodRep(), bookingDto.getFinalDate(), bookingDto.getPeriod().getstartDate(), bookingDto.getPeriod().getEndDate());
                Booking booking = new Booking(bookingDto.isIsPeriodic(), bookingDto.getReason(), p, bookingDto.getPeriodRep(), bookingDto.getFinalDate());
                booking.setActive(true);
                booking.setState("inicial");
                booking.setPersonaEina(u);
                booking.setEspecial(bookingDto.isEspecial());

                for (int i = 0; i < bookingDto.getSpaces().size(); i++) {
                    Optional<Space> space = spaceRepository.findByGid(bookingDto.getSpaces().get(i));
                    space.ifPresent(booking::setSpaces);
                }

                if (u.getRol() == PersonaEina.Rol.ADMIN) {
                    booking.setEspecial(bookingDto.isEspecial());
                } else {
                    booking.setEspecial(false);
                }

                bookingRepository.save(booking);
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    private boolean keepPolicy(BookingDto bookingDto, PersonaEina.Rol user_rol) {

        boolean keepPolicy = true;

        Calendar initialDate= Calendar.getInstance();
        initialDate.setTime(bookingDto.getPeriod().getstartDate());

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(bookingDto.getPeriod().getEndDate());

        Calendar calendarMonth = Calendar.getInstance();
        calendarMonth.add(Calendar.MONTH, 1);

        Calendar calendarWeek = Calendar.getInstance();
        calendarWeek.add(Calendar.DATE, 7);

        Calendar hoy = Calendar.getInstance();

        if (initialDate.after(hoy)){

            if (user_rol== PersonaEina.Rol.PDI) {
                if (bookingDto.isIsPeriodic()){
                    if (initialDate.after(calendarMonth)) {
                        keepPolicy = false;
                    }
                }
                else{
                    if (initialDate.after(calendarMonth) || endDate.after(calendarMonth)) {
                        keepPolicy = false;
                    }
                }
            }
            else if (user_rol== PersonaEina.Rol.ESTUDIANTE){
                if(initialDate.after(calendarWeek) || endDate.after(calendarWeek)){
                    keepPolicy =false;
                }
            }

            if (keepPolicy) {

                for (int i = 0; i < bookingDto.getSpaces().size(); i++) {
                    Optional<Space> space = spaceRepository.findByGid(bookingDto.getSpaces().get(i));
                    if(!space.isPresent()) {
                        return false;
                    }
                    List<Booking> bookings = space.get().getBookings();

                    if (!bookings.isEmpty()) {

                        for (Booking booking : bookings) {
                            if (booking.isActive()) {
                                Collection<Period> p = booking.getPeriod();
                                for (int k = 0; k < p.size(); k++) {
                                    Calendar ini = Calendar.getInstance();
                                    ini.setTime(booking.getPeriod().get(k).getstartDate());
                                    Calendar fin = Calendar.getInstance();
                                    fin.setTime(booking.getPeriod().get(k).getEndDate());

                                    if (ini.equals(initialDate)) {
                                        keepPolicy = false;
                                    } else if (initialDate.after(ini) && endDate.before(fin)) {
                                        keepPolicy = false;
                                    } else if (endDate.after(ini) && endDate.before(fin)) {
                                        keepPolicy = false;
                                    } else if (initialDate.after(ini) && initialDate.before(fin)) {
                                        keepPolicy = false;
                                    } else if (fin.equals(endDate)) {
                                        keepPolicy = false;
                                    } else if (initialDate.before(ini) && endDate.after(fin)) {
                                        keepPolicy = false;
                                    }

                                }
                            }
                        }
                    } else {
                        keepPolicy = true;
                    }
                }
            }

        } else{
            keepPolicy =false;
        }

        return keepPolicy;

    }

    private List<Period> calculatePeriods(String periodRep, Date finalDate, Date startDate, Date endDate) {


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
                        int dia = calendar.get(Calendar.DAY_OF_WEEK);
                        if (dia!=1 && dia!=7){
                            p.add(period2);
                        }
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
                space.ifPresent(booking::setSpaces);
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
    public Optional<Booking> validateBooking(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent() && booking.get().getState().equalsIgnoreCase("inicial")) {
            Booking b = booking.get();
            b.setState("valida");
            bookingRepository.save(b);
            return booking;
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Booking> cancelBooking(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent() && !booking.get().getState().equalsIgnoreCase("inicial")) {
            Booking b = booking.get();
            b.setState("invalida");
            b.setActive(false);
            bookingRepository.save(b);
            return booking;
        }
        return Optional.empty();

    }

    public long getBookingOwnerByID (long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return (booking.isPresent() ? booking.get().getPersonaEina().getId() : -1 );
    }

    public PersonaEina getBookingUserByID(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return (booking.map(Booking::getPersonaEina).orElse(null));
    }
}
