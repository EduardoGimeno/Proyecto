package com.unizar.major.application.service;

import com.unizar.major.domain.Booking;
import com.unizar.major.domain.User;
import com.unizar.major.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){

    }

    @Transactional
    public String createUser(User user) {

        User user1 = userRepository.save(new User(user.getFirstName(), user.getLastName(), user.getRol(), user.getNombreUsuario()));
        Optional<User> user_2 = userRepository.findById(user1.getId());
        if (user_2.isPresent()) {
            return "User "+ user_2.get().getNombreUsuario() +" is created";
        }
        else {
            return "User "+ user_2.get().getNombreUsuario()+" is not created";
        }

    }

    public Optional<User> getUser (long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        }
        else {
            return null;
        }

    }

    @Transactional
    public String deleteUser(long id){

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User u = user.get();
            userRepository.delete(u);
            return "User "+ user.get().getNombreUsuario()+" is deleted";
        }
        else {
            return "User not exist";
        }

        // else 404

    }

    @Transactional
    public String updateUser(long id, User user){

        userRepository.setUserInfoById(user.getFirstName(),user.getLastName(),user.getRol(),user.getNombreUsuario(),id);

        return "User is update";

    }

    public List<Booking> getBookings (long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            return u.getBookings();
        }
        else {
            return null;
        }

    }
}
