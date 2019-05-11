package com.unizar.major.infrastructure.controller;

/*
@RestController
public class SpaceController {

    @Autowired
    Receptor receptor;
/*
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

    @GetMapping("/space/{id}")
    public SpaceDto getAllSpaces(@PathVariable int id){
        Optional<Space> space =spaceService.getSpaceByGid(id);

        return convertDto(space.get());

    }

    @GetMapping("/space/{id}/bookings")
    public List<BookingDtoReturn> getBookingByIdSpace(@PathVariable int id) {
        List<Booking> bookings = spaceService.getBookingByIdSpace(id);
        List<BookingDtoReturn> bookingDtosReturn = new ArrayList<>();

        for (Booking b : bookings) {
            bookingDtosReturn.add(convertDtoBooking(b));
        }
        return bookingDtosReturn;
    }

    private SpaceDto convertDto(Space space) {
        ModelMapper modelMapper = new ModelMapper();
        SpaceDto spaceDto = modelMapper.map(space, SpaceDto.class);
        return spaceDto;
    }

    private BookingDtoReturn convertDtoBooking(Booking booking) {
        ModelMapper modelMapper = new ModelMapper();
        BookingDtoReturn bookingDto = modelMapper.map(booking, BookingDtoReturn.class);
        return bookingDto;
    }

}
*/