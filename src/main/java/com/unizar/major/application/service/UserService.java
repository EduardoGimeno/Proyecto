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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){

    }

    @Transactional
    public Boolean createUser(UserDto userDto) {

        byte[] data = userDto.getPassword().getBytes(StandardCharsets.UTF_8);
        String password_encrypt = DigestUtils.md5DigestAsHex(data);

        User user1 = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getRol().toString().toUpperCase(), userDto.getUserName(), userDto.getEmail(), password_encrypt);
        user1.setActive(true);
        userRepository.save(user1);

        return userRepository.findById(user1.getId()).isPresent();
    }

    public Optional<User> getUser (long id){
        return userRepository.findById(id);
    }

    @Transactional
    public Boolean deleteUser(long id){

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User u = user.get();
            u.setActive(false);
            userRepository.save(u);
            for (Booking b : u.getBookings()) {
                b.setActive(false);
            }

            return true;
        }
        return false;
    }

    @Transactional
    public Boolean updateUser(long id, UserDto userDto){

        Optional<User> u = userRepository.findById(id);
        if (u.isPresent()){
            byte[] data = userDto.getPassword().getBytes(StandardCharsets.UTF_8);
            String password_encrypt = DigestUtils.md5DigestAsHex(data);

            User user = u.get();
            if(userDto.getFirstName() != null) { user.setFirstName(userDto.getFirstName()); }
            if(userDto.getLastName() != null) { user.setLastName(userDto.getLastName()); }
            if(userDto.getUserName() != null) { user.setUserName(userDto.getUserName()); }
            if(userDto.getEmail() != null) { user.setEmail(userDto.getEmail()); }
            if(userDto.getPassword() != null) { user.setPassword(password_encrypt); }
            userRepository.save(user);
            return true;
        }
        return false;
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

    public Optional<User> loginUser(LoginDto loginDto) {

        Optional<User> user = userRepository.findByUserName(loginDto.getLogin());
        if (user.isPresent() && user.get().isActive()){
            String password_encrypt;
            byte[] data = loginDto.getPassword().getBytes(StandardCharsets.UTF_8);
            password_encrypt = DigestUtils.md5DigestAsHex(data);
            if(user.get().getPassword().compareTo(password_encrypt)==0) {
                return user;
            }
        }
        return Optional.empty();
    }

}
