package com.unizar.major.controller;

import com.unizar.major.domain.User;
import com.unizar.major.dtos.UserDto;
import com.unizar.major.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;




@RestController
public class UserController {


    @Autowired
    UserService userService;

    @PostMapping("/user")

    public String create(@RequestBody UserDto userDto) {

        User user = new User();
        try {
            user = convertToEntity(userDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userService.createUser(user);

    }


    @GetMapping("/user/{id}")

    public UserDto getUserById(@PathVariable long id){
        User u = userService.getUser(id);
        if (u == null){
            return null;
        }
        else {
            return convertDto(u);
        }

    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable long id){

        return userService.deleteUser(id);

    }

    @PutMapping("/user/{id}")
    public String updateUser(@PathVariable long id, @RequestBody UserDto userDto){

        User user = new User();
        try {
            user = convertToEntity(userDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userService.updateUser(id,user);

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





}
