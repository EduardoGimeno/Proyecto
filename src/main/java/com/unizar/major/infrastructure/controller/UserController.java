package com.unizar.major.infrastructure.controller;

import com.unizar.major.application.dtos.BookingDto;
import com.unizar.major.application.dtos.BookingDtoReturn;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.User;
import com.unizar.major.application.dtos.UserDto;
import com.unizar.major.application.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class UserController {


    @Autowired
    UserService userService;

    @PostMapping("/user")

    public String createUser(@RequestBody UserDto userDto) {

        return userService.createUser(userDto);

    }


    @GetMapping("/user/{id}")

    public UserDto getUserById(@PathVariable long id){
       Optional<User> u = userService.getUser(id);
        if (u == null){
            return null;
        }
        else {
            return convertDto(u.get());
        }

    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable long id){

        return userService.deleteUser(id);

    }

    @PutMapping("/user/{id}")
    public String updateUser(@PathVariable long id, @RequestBody UserDto userDto){

        return userService.updateUser(id, userDto);

    }

    @GetMapping("/user/{id}/bookings")

    public List<BookingDtoReturn> getUserBookingsById(@PathVariable long id){
        List<Booking> booking = userService.getBookings(id);
        if (booking == null){
            return null;
        }
        else {
            List<BookingDtoReturn> bookingDtos = new ArrayList<>();

            for (Booking b : booking) {
                bookingDtos.add(convertDto(b));
            }
            return bookingDtos;
        }

    }


    private User convertToEntity (UserDto userDto) throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    private UserDto convertDto(User user) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    private BookingDtoReturn convertDto(Booking booking) {
        ModelMapper modelMapper = new ModelMapper();
        BookingDtoReturn bookingDtoReturn  = modelMapper.map(booking, BookingDtoReturn.class);
        return bookingDtoReturn;
    }





}
