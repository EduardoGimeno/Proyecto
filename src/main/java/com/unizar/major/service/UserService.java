package com.unizar.major.service;

import com.unizar.major.domain.User;
import com.unizar.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


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

    public Optional<User> getUser (long id){
        return userRepository.findById(id);

    }

    public String deleteUser(long id){

        Optional<User> user = userRepository.findById(id);
        user.ifPresent(user1 -> userRepository.delete(user1));
        // else 404
        return "User is deleted";

    }

    public String updateUser(long id, User user){

        userRepository.setUserInfoById(user.getFirstName(),user.getLastName(),user.getRol(),user.getNombreUsuario(),id);

        return "User is update";

    }
}
