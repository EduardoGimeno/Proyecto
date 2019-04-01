package com.unizar.major.controller;

import com.unizar.major.domain.Booking;
import com.unizar.major.dtos.BookingDto;
import com.unizar.major.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

        Booking b = bookingService.getBookingById(id);
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
        List<BookingDto> bookingDtos =new List<BookingDto>() {

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<BookingDto> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] ts) {
                return null;
            }

            @Override
            public boolean add(BookingDto bookingDto) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends BookingDto> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, Collection<? extends BookingDto> collection) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public BookingDto get(int i) {
                return null;
            }

            @Override
            public BookingDto set(int i, BookingDto bookingDto) {
                return null;
            }

            @Override
            public void add(int i, BookingDto bookingDto) {

            }

            @Override
            public BookingDto remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<BookingDto> listIterator() {
                return null;
            }

            @Override
            public ListIterator<BookingDto> listIterator(int i) {
                return null;
            }

            @Override
            public List<BookingDto> subList(int i, int i1) {
                return null;
            }
        };
        for (int i = 0; i<booking.size(); i++){
            Booking b = booking.get(i);
            bookingDtos.add(convertDto(b));
        }

        return bookingDtos;

    }

    private Booking convertToEntity(BookingDto bookingDto) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        return booking;
    }

    private BookingDto convertDto(Booking booking) {
        ModelMapper modelMapper = new ModelMapper();
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
        return bookingDto;
    }

}
