package com.unizar.major.application.service;

import com.unizar.major.application.dtos.UserDto;
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
    public String createUser(UserDto userDto) {

        User user1 = new User(userDto.getFirstName(), userDto.getLastName(), "estudiante", userDto.getNombreUsuario());
        user1.setActive(true);
        userRepository.save(user1);

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
            u.setActive(false);
            userRepository.save(u);
            return "User "+ user.get().getNombreUsuario()+" is deleted";
        }
        else {
            return "User not exist";
        }

        // else 404

    }

    @Transactional
    public String updateUser(long id, UserDto userDto){

        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()){
            User user = u.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setNombreUsuario(userDto.getNombreUsuario());
            userRepository.save(user);
            return "User is update";
        }
        else{
            return "User not exist";
        }





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
