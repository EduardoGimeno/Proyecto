package com.unizar.major;


import com.unizar.major.application.dtos.UserDto;
import com.unizar.major.application.service.UserService;
import com.unizar.major.domain.User;
import com.unizar.major.domain.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MajorApplicationTests {

	@Test
	public void contextLoads() {

	}

    @Autowired
    private UserRepository userRepository;

	@Autowired
	private UserService userService;

    @Test
    public void whenFindByNameUser_theReturnUser(){
        String password_encript="";

        try{
            byte[] data = "pppppppp".getBytes("UTF-8");
            password_encript = DigestUtils.md5DigestAsHex(data);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        User user = new User("pepa", "lopez", "estudiante", "plopez","plopez@unizar.es",password_encript);
        user.setActive(true);
        userRepository.save(user);

        Optional<User> found = userRepository.findByUserName(user.getUserName());
        assertThat(found.get().getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    public void whenFindById_theReturnUser(){
        String password_encript="";

        try{
            byte[] data = "pppppppp".getBytes("UTF-8");
            password_encript = DigestUtils.md5DigestAsHex(data);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        User user = new User("pepa", "lopez", "estudiante", "plopez","plopez@unizar.es",password_encript);
        user.setActive(true);
        userRepository.save(user);

        Optional<User> found = userRepository.findById(user.getId());
        assertThat(found.get().getId()).isEqualTo(user.getId());
    }

    @Test
    public void whenFindAll_theReturnUsers(){

        userRepository.deleteAll();
        User user = new User("pepa", "lopez", "estudiante", "plopez","plopez@unizar.es","pppppppp");
        user.setActive(true);
        userRepository.save(user);

        User user1 = new User("pepe", "perez", "estudiante", "pperez","pperez@unizar.es","pppppppp");
        user1.setActive(true);
        userRepository.save(user1);

        List<User> found = userRepository.findAll();
        assertThat(found.size()).isEqualTo(2);
    }

    @Test
    public void whenCreateNewUser(){

        UserDto userDto = new UserDto(1,"mario", "amador", "estudiante", "mamador","mamador@unizar.es","mmmmmmmm");
        userService.createUser(0,userDto);

        Optional<User> user_new = userRepository.findById(userDto.getId());
        assertEquals(userDto.getFirstName(),user_new.get().getFirstName());
        assertEquals(userDto.getLastName(),user_new.get().getLastName());
        assertEquals(userDto.getUserName(),user_new.get().getUserName());
        assertEquals(userDto.getEmail(),user_new.get().getEmail());
        assertEquals("estudiante", user_new.get().getRol());

        String password_encript="";

        try{
            byte[] data = userDto.getPassword().getBytes("UTF-8");
            password_encript = DigestUtils.md5DigestAsHex(data);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        assertEquals(password_encript, user_new.get().getPassword());
        assertEquals(true, user_new.get().isActive());

    }

    @Test
    public void whenGetUser(){
        UserDto userDto = new UserDto(1,"mario", "amador", "estudiante", "mamador","mamador@unizar.es","mmmmmmmm");
        userService.createUser(0,userDto);
        Optional<User> user = userService.getUser(userDto.getId());

        Optional<User> user_found = userRepository.findById(userDto.getId());

        assertEquals(user.get().getFirstName(),user_found.get().getFirstName());
        assertEquals(user.get().getLastName(),user_found.get().getLastName());
        assertEquals(user.get().getUserName(),user_found.get().getUserName());
        assertEquals(user.get().getEmail(),user_found.get().getEmail());
        assertEquals(user.get().getRol(), user_found.get().getRol());
        assertEquals(user.get().getPassword(),user_found.get().getPassword());
        assertEquals(user.get().isActive(), user_found.get().isActive());
    }

    @Test
    public void whenDeleteUser(){
        UserDto userDto = new UserDto(1,"mario", "amador", "estudiante", "mamador","mamador@unizar.es","mmmmmmmm");
        userService.createUser(0,userDto);
        userService.deleteUser(userDto.getId());
        Optional<User> user = userRepository.findById(userDto.getId());
        assertEquals(false,user.get().isActive());
    }

    @Test
    public void whenUpdateUser(){
        UserDto userDto = new UserDto(2,"rosa", "perez", "estudiante", "rperez","rperez@unizar.es","rrrrrrrr");
        userService.createUser(0,userDto);

        UserDto userDto_mod = new UserDto(2,"rosa", "perez", "admin", "rperez1","rperez1@unizar.es","rrrrrrrr1");
        userService.updateUser(userDto.getId(),userDto_mod);

        Optional<User> user_mod = userRepository.findById(userDto.getId());

        assertEquals(userDto_mod.getFirstName(),user_mod.get().getFirstName());
        assertEquals(userDto_mod.getLastName(),user_mod.get().getLastName());
        assertEquals(userDto_mod.getUserName(),user_mod.get().getUserName());
        assertEquals(userDto_mod.getEmail(),user_mod.get().getEmail());
        assertEquals("estudiante", user_mod.get().getRol());
        assertEquals(userDto_mod.getPassword(),user_mod.get().getPassword());
        assertEquals(true, user_mod.get().isActive());

    }



}

