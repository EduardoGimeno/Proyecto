package com.unizar.major.application.service;

import com.unizar.major.application.dtos.SpaceInfoDto;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Materials;
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
        if(space.isPresent()) {
            return space.get().getBookings();
        }
        return null;
    }

    public Boolean updateInfoSpace(int id, SpaceInfoDto spaceInfoDto){
        Optional<Space> space = spaceRepository.findByGid(id);
        if(space.isPresent()) {
            Space s = space.get();
            Materials materials = new Materials(spaceInfoDto.isProyector(), spaceInfoDto.getPizarra(),spaceInfoDto.isPantalla(),spaceInfoDto.getOrdenadores(), spaceInfoDto.getSillas(), spaceInfoDto.getMesas());
            s.setMaterials(materials);
            spaceRepository.save(s);
            return true;
        }
        return false;
    }

    public Optional<Space> getSpaceByCoords(int floor, double X, double Y) {
        return spaceRepository.findByCoords(floor, X, Y);
    }

    public List<Space> getSpacesQuery(int chairs, double area) {
        area = area*10000; // converto to cm²
        if(chairs == 0) {
            // SEARCH ONLY AREA
            return spaceRepository.findByArea(area);
        }
        if(area == 0) {
            // SEARCH ONLY CHAIRS
            return spaceRepository.findByChairs(chairs);
        }
        // SEARCH BOTH
        return getSpacesQuery(chairs, area);
    }

}
