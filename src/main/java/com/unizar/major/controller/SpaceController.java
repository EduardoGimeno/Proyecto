package com.unizar.major.controller;

import com.unizar.major.domain.Space;
import com.unizar.major.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpaceController {

    @Autowired
    SpaceRepository spaceRepository;


    @PostMapping("/space")
    public String createNewBooking(){
        Space space = new Space("Planta 1", "ADA");
        spaceRepository.save(space);

        return "Space is created";

    }

}
