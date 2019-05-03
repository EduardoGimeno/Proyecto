package com.unizar.major.application.service;

import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Space;
import com.unizar.major.domain.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public SpaceService(){

    }

    public List<Space> getAllSpaces(){
       return spaceRepository.findAll();
    }

    public Optional<Space> getSpaceByGid(int id){
        return spaceRepository.findByGid(id);
    }

    public List<Booking> getBookingByIdSpace(int id){
        Optional<Space> space = spaceRepository.findByGid(id);
        return space.get().getBookings();
    }

}
