package com.unizar.major.application.service;

import com.unizar.major.application.dtos.LoginDto;
import com.unizar.major.application.dtos.PersonaEinaDto;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.PersonaEina;
import com.unizar.major.domain.repository.PersonaEinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@Service
public class PersonaEinaService {

    @Autowired
    private PersonaEinaRepository personaEinaRepository;

    public PersonaEinaService(){

    }

    @Transactional
    public Boolean createUser(PersonaEinaDto personaEinaDto) {

        byte[] data = personaEinaDto.getPassword().getBytes(StandardCharsets.UTF_8);
        String password_encrypt = DigestUtils.md5DigestAsHex(data);

        PersonaEina personaEina1 = new PersonaEina(personaEinaDto.getFirstName(), personaEinaDto.getLastName(), personaEinaDto.getRol().toString().toUpperCase(), personaEinaDto.getUserName(), personaEinaDto.getEmail(), password_encrypt);
        personaEina1.setActive(true);
        personaEinaRepository.save(personaEina1);

        return personaEinaRepository.findById(personaEina1.getId()).isPresent();
    }

    public Optional<PersonaEina> getUser (long id){
        return personaEinaRepository.findById(id);
    }

    @Transactional
    public Boolean deleteUser(long id){

        Optional<PersonaEina> user = personaEinaRepository.findById(id);

        if (user.isPresent()) {
            PersonaEina u = user.get();
            u.setActive(false);
            personaEinaRepository.save(u);
            for (Booking b : u.getBookings()) {
                b.setActive(false);
            }

            return true;
        }
        return false;
    }

    @Transactional
    public Boolean updateUser(long id, PersonaEinaDto personaEinaDto){

        Optional<PersonaEina> u = personaEinaRepository.findById(id);
        if (u.isPresent()){
            byte[] data = personaEinaDto.getPassword().getBytes(StandardCharsets.UTF_8);
            String password_encrypt = DigestUtils.md5DigestAsHex(data);

            PersonaEina personaEina = u.get();
            if(personaEinaDto.getFirstName() != null) { personaEina.setFirstName(personaEinaDto.getFirstName()); }
            if(personaEinaDto.getLastName() != null) { personaEina.setLastName(personaEinaDto.getLastName()); }
            if(personaEinaDto.getUserName() != null) { personaEina.setUserName(personaEinaDto.getUserName()); }
            if(personaEinaDto.getEmail() != null) { personaEina.setEmail(personaEinaDto.getEmail()); }
            if(personaEinaDto.getPassword() != null) { personaEina.setPassword(password_encrypt); }
            personaEinaRepository.save(personaEina);
            return true;
        }
        return false;
    }

    public List<Booking> getBookings (long id){
        Optional<PersonaEina> user = personaEinaRepository.findById(id);
        if (user.isPresent()) {
            PersonaEina u = user.get();
            return u.getBookings();
        }
        else {
            return null;
        }

    }

    public Optional<PersonaEina> loginUser(LoginDto loginDto) {

        Optional<PersonaEina> user = personaEinaRepository.findByUserName(loginDto.getLogin());
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

    public boolean existsUserInSystem(PersonaEinaDto personaEinaDto) {
        return personaEinaRepository.findByUserName(personaEinaDto.getUserName()).isPresent() ||
                personaEinaRepository.findByEmail(personaEinaDto.getEmail()).isPresent();
    }

}
