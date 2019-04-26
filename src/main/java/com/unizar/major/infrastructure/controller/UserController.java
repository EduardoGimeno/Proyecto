package com.unizar.major.infrastructure.controller;

import com.unizar.major.application.dtos.BookingDtoReturn;
import com.unizar.major.application.dtos.LoginDto;
import com.unizar.major.application.service.BookingService;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.User;
import com.unizar.major.application.dtos.UserDto;
import com.unizar.major.application.service.UserService;
import com.unizar.major.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(BookingService.class);


    @PostMapping("/user/login")
    public String loginUser(@RequestBody LoginDto loginDto){

        if (loginDto.getLogin().isEmpty()){
            return "enter a username or email";
        }
        else if (loginDto.getPassword().isEmpty()){
            return "enter your password";
        }
        else{
            return userService.loginUser(loginDto);
        }

    }

    @PostMapping("/user")
    public String createUser(@RequestParam(value="id", required=false) long id, @RequestBody UserDto userDto) {

        String email = userDto.getEmail();
        String unizar = "unizar.es";
        int pos = email.indexOf("@");
        String caracteres = email.substring(pos+1);

        logger.info(caracteres);
        logger.info(userDto.getEmail());

        if (caracteres.compareTo(unizar)==0){
            if (userDto.getFirstName().length()<=20){
                if (userDto.getLastName().length()<=20){
                    if (userDto.getUserName().length()<=20){
                        if (userDto.getPassword().length()>=8){
                            if (!userRepository.findByUserName(userDto.getUserName()).isPresent()){
                                return userService.createUser(id,userDto);
                            }
                            else{
                                return "Username already exist in the system";
                            }
                        }
                        else{
                            return "Password not be equal or greater than 8 characters";
                        }

                    }
                    else{
                        return "Username not be more than 20 characters";
                    }

                }
                else{
                    return "LastName not be more than 20 characters";
                }
            }
            else{
                return "FirstName not be more than 20 characters";
            }
        }
        else{
            return "Email incorrect, not is @unizar.es";
        }


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
