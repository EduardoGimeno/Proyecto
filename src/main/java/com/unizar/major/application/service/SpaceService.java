package com.unizar.major.application.service;

import com.unizar.major.application.dtos.SpaceInfoDto;
import com.unizar.major.application.dtos.SpaceTimetableDto;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Materials;
import com.unizar.major.domain.Period;
import com.unizar.major.domain.Space;
import com.unizar.major.domain.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public SpaceService() {

    }

    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    public Optional<Space> getSpaceByGid(int id) {
        return spaceRepository.findByGid(id);
    }

    public List<Booking> getBookingByIdSpace(int id) {
        Optional<Space> space = spaceRepository.findByGid(id);
        if (space.isPresent()) {
            return space.get().getBookings();
        }
        return null;
    }

    public Boolean updateInfoSpace(int id, SpaceInfoDto spaceInfoDto) {
        Optional<Space> space = spaceRepository.findByGid(id);
        if (space.isPresent()) {
            Space s = space.get();
            if (cumplePolitica(spaceInfoDto, space.get().getArea())) {
                Materials materials = new Materials(spaceInfoDto.isProyector(), spaceInfoDto.getPizarra(), spaceInfoDto.isPantalla(), spaceInfoDto.getOrdenadores(), spaceInfoDto.getSillas(), spaceInfoDto.getMesas());
                s.setMaterials(materials);
                spaceRepository.save(s);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    private boolean cumplePolitica(SpaceInfoDto spaceInfoDto, double area) {
        double area_m2 = area / 10000;
        double chairSpace = spaceInfoDto.getSillas() * 0.5;
        double tableSpace = (double) (spaceInfoDto.getMesas() * 2);
        double freeSpace = (area_m2 * 10) / 100;
        double remainArea = area_m2 - freeSpace - tableSpace - chairSpace;

        return remainArea >= 0;
    }

    public Optional<Space> getSpaceByCoords(int floor, double X, double Y) {
        return spaceRepository.findByCoords(floor, X, Y);
    }

    public List<Space> getSpacesQuery(int chairs, double area) {
        area = area * 10000; // convert to to cm²
        if (chairs == 0) {
            // SEARCH ONLY AREA
            return spaceRepository.findByArea(area);
        }
        if (area == 0) {
            // SEARCH ONLY CHAIRS
            return spaceRepository.findByChairs(chairs);
        }
        // SEARCH BOTH
        return getSpacesQuery(chairs, area);
    }

    public List<SpaceTimetableDto> getCalendarSpace(int id) {

        String state;
        List<SpaceTimetableDto> timetable = new ArrayList<>();
        Optional<Space> space = spaceRepository.findByGid(id);
        if(!space.isPresent()) {
            return timetable;
        }

        List<Booking> bookings = space.get().getBookings();

        Calendar initDate = Calendar.getInstance(TimeZone.getTimeZone("GTM"));
        Calendar endDate = Calendar.getInstance(TimeZone.getTimeZone("GTM"));

        int year, month, day;

        Calendar weekCalendar = Calendar.getInstance(TimeZone.getTimeZone("GTM"));
        weekCalendar.add(Calendar.DATE, 8);
        Calendar calendar_hora = Calendar.getInstance(TimeZone.getTimeZone("GTM"));

        while(initDate.before(weekCalendar)){
            year = initDate.get(Calendar.YEAR);
            month = initDate.get(Calendar.MONTH);
            day = initDate.get(Calendar.DATE);
            initDate.set(year, month, day, 8, 0);
            endDate.set(year, month, day, 9, 0);

            calendar_hora.set(year,month,day,20,0);

            while(initDate.before(calendar_hora)){
                state = "libre";
                SpaceTimetableDto spaceTimetableDto = new SpaceTimetableDto();
                Period period = new Period(initDate.getTime(), endDate.getTime());
                spaceTimetableDto.setPeriod(period);

                if (!bookings.isEmpty()) {

                    for (Booking booking : bookings) {
                        if (booking.isActive()) {
                            Collection<Period> p = booking.getPeriod();
                            for (int k = 0; k < p.size(); k++) {
                                Calendar ini = Calendar.getInstance();
                                ini.setTime(booking.getPeriod().get(k).getstartDate());
                                Calendar fin = Calendar.getInstance();
                                fin.setTime(booking.getPeriod().get(k).getEndDate());

                                if (ini.equals(initDate)) {
                                    state = "ocupado";
                                } else if (initDate.after(ini) && endDate.before(fin)) {
                                    state = "ocupado";
                                } else if (initDate.after(ini) && initDate.before(fin)) {
                                    state = "ocupado";
                                } else if (fin.equals(endDate)) {
                                    state = "ocupado";
                                } else if (initDate.before(ini) && endDate.after(fin)) {
                                    state = "ocupado";
                                }

                                if (state.compareTo("ocupado") == 0 && booking.isEspecial()) {
                                    state = "no reservable";
                                }
                            }
                        } else {
                            state = "libre";
                        }

                    }

                } else{
                    state="libre";
                }
                spaceTimetableDto.setState(state);
                timetable.add(spaceTimetableDto);
                initDate.add(Calendar.HOUR_OF_DAY,1);
                endDate.add(Calendar.HOUR_OF_DAY,1);
            }
            initDate.add(Calendar.DATE,1);
            endDate.add(Calendar.DATE,1);
        }

        return timetable;
    }
}
