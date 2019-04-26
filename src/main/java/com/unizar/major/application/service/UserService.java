package com.unizar.major.application.service;

import com.unizar.major.application.dtos.LoginDto;
import com.unizar.major.application.dtos.UserDto;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.User;
import com.unizar.major.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){

    }

    @Transactional
    public String createUser(long id,UserDto userDto) {
        String password_encript="";

        try{
            byte[] data = userDto.getPassword().getBytes("UTF-8");
            password_encript = DigestUtils.md5DigestAsHex(data);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        Optional<User> user = userRepository.findById(id);
        User user1;
        if (user.isPresent() && user.get().getRol()=="admin"){
            user1 = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getRol(), userDto.getUserName(), userDto.getEmail(),password_encript);
            user1.setActive(true);
            userRepository.save(user1);
        }
        else{
            user1 = new User(userDto.getFirstName(), userDto.getLastName(), "estudiante", userDto.getUserName(), userDto.getEmail(),password_encript);
            user1.setActive(true);
            userRepository.save(user1);
        }

        Optional<User> user_2 = userRepository.findById(user1.getId());
        if (user_2.isPresent()) {
            return "User "+ user_2.get().getUserName() +" is created";
        }
        else {
            return "User "+ user_2.get().getUserName()+" is not created";
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
            for (Booking b : u.getBookings()) {
                b.setActive(false);
            }

            return "User "+ user.get().getUserName()+" is deleted";
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
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
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

    public String loginUser(LoginDto loginDto) {

        Optional<User> user = userRepository.findByUserName(loginDto.getLogin());
        if (user.isPresent()){
            String password_encript="";
            try{
                byte[] data = loginDto.getPassword().getBytes("UTF-8");
                password_encript = DigestUtils.md5DigestAsHex(data);
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
            if (user.get().getPassword().compareTo(password_encript)==0){
                return "login";
            }
            else{
                return "Password is incorrect";
            }
        }
        else{
            return "Name of user is incorrect";
        }
    }

}
