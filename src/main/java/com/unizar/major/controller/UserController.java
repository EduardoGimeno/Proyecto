package com.unizar.major.controller;

import com.unizar.major.domain.User;
import com.unizar.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/user/create")

    public String create(@RequestBody User user){

        userRepository.save(new User(user.getFirstName(), user.getLastName(),user.getRol(),user.getNombreUsuario()));

        return "User is created";

    }

    @PostMapping("/user/delete/{id}")

    public String delete(@PathVariable long id){

        User user = findById(id);

        userRepository.delete(user);

        return "User is deleted";

    }

    @GetMapping("/user/findAll")

    public List<User> findAll(){

        List<User> customers = userRepository.findAll();

        return customers;

    }

    @GetMapping("/user/findByRol/{rol}")

    public List<User> findByRol(@PathVariable String rol){

        List<User> userUI = new ArrayList<>();

        List<User> users = userRepository.findByRol(rol);
        for (User user: users){
            userUI.add(new User(user.getFirstName(),user.getLastName(), user.getRol(), user.getNombreUsuario()));

        }

        return userUI;

    }

    @GetMapping("/user/findByFirstName/{firstName}")

    public List<User> findByFirstName(@PathVariable String firstName){

        List<User> userUI = new ArrayList<>();

        List<User> users = userRepository.findByFirstName(firstName);
        for (User user: users){
            userUI.add(new User(user.getFirstName(),user.getLastName(), user.getRol(),user.getNombreUsuario()));

        }

        return userUI;

    }

    @GetMapping("/user/findById/{id}")

    public User findById(@PathVariable long id){

        User user = userRepository.findById(id);

        return user;

    }

}
