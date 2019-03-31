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

    @PostMapping("/user")

    public String create(@RequestBody User user){

        userRepository.save(new User(user.getFirstName(), user.getLastName(),user.getRol(),user.getNombreUsuario()));

        return "User is created";

    }


    @GetMapping("/user/{id}")

    public User getUserById(@PathVariable long id){

        return userRepository.findById(id);

    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable long id){

        User user = userRepository.findById(id);

        userRepository.delete(user);

        return "User is deleted";

    }

    @PutMapping("/user/{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user){

        userRepository.setUserInfoById(user.getFirstName(),user.getLastName(),user.getRol(),user.getNombreUsuario(),id);

        return "User is update";

    }

    @GetMapping("/user/findAll")

    public List<User> findAll(){

        return userRepository.findAll();

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

    @GetMapping("/user/findByFirstName{firstName}")

    public List<User> findByFirstName(@PathVariable String firstName){

        List<User> userUI = new ArrayList<>();

        List<User> users = userRepository.findByFirstName(firstName);
        for (User user: users){
            userUI.add(new User(user.getFirstName(),user.getLastName(), user.getRol(),user.getNombreUsuario()));

        }

        return userUI;

    }



}
