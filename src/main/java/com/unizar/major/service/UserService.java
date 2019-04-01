package com.unizar.major.service;

import com.unizar.major.domain.User;
import com.unizar.major.dtos.UserDto;
import com.unizar.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){

    }


    public String createUser(User user){

        userRepository.save(new User(user.getFirstName(), user.getLastName(),user.getRol(),user.getNombreUsuario()));
        return "User is created";

    }

    public User getUser (long id){
        return userRepository.findById(id);

    }

    public String deleteUser(long id){

        User user = userRepository.findById(id);

        userRepository.delete(user);

        return "User is deleted";

    }

    public String updateUser(long id, User user){

        userRepository.setUserInfoById(user.getFirstName(),user.getLastName(),user.getRol(),user.getNombreUsuario(),id);

        return "User is update";

    }
}
