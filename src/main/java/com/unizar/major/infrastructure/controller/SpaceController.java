package com.unizar.major.infrastructure.controller;

import com.unizar.major.application.dtos.SpaceDto;
import com.unizar.major.application.service.BookingService;
import com.unizar.major.application.service.SpaceService;
import com.unizar.major.domain.Space;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SpaceController {

    @Autowired
    SpaceService spaceService;

    Logger logger = LoggerFactory.getLogger(BookingService.class);

    @GetMapping("/spaces")
    public List<SpaceDto> getAllSpaces(){
        List<Space> space =spaceService.getAllSpaces();
        List<SpaceDto> spaceDtos = new ArrayList<>();

        for (Space s : space) {
            spaceDtos.add(convertDto(s));
        }

        return spaceDtos;

    }

    @GetMapping("/spaces/{id}")
    public SpaceDto getAllSpaces(@PathVariable int id){
        Optional<Space> space =spaceService.getSpaceByGid(id);

        return convertDto(space.get());

    }

    private SpaceDto convertDto(Space space) {
        ModelMapper modelMapper = new ModelMapper();
        SpaceDto spaceDto = modelMapper.map(space, SpaceDto.class);
        return spaceDto;
    }

}
