package com.unizar.major.application.service;

import com.unizar.major.application.dtos.SpaceHorarioDto;
import com.unizar.major.application.dtos.SpaceInfoDto;
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

    public boolean cumplePolitica(SpaceInfoDto spaceInfoDto, double area) {
        double area_m2 = area / 10000;
        double espacio_sillas = spaceInfoDto.getSillas() * 0.5;
        double espacio_mesas = spaceInfoDto.getMesas() * 2;
        double espacio_libre = (area_m2 * 10) / 100;
        double area_restante = area_m2 - espacio_libre - espacio_mesas - espacio_sillas;

        if (area_restante >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public Optional<Space> getSpaceByCoords(int floor, double X, double Y) {
        return spaceRepository.findByCoords(floor, X, Y);
    }

    public List<Space> getSpacesQuery(int chairs, double area) {
        area = area * 10000; // converto to cm²
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

    public List<SpaceHorarioDto> getCalendarSpace(int id) {

        String estado = "libre";
        List<SpaceHorarioDto> horarios = new ArrayList<>();
        Optional<Space> space = spaceRepository.findByGid(id);
        List<Booking> bookings = space.get().getBookings();

        Calendar fecha_ini = Calendar.getInstance(TimeZone.getTimeZone("GTM"));
        Calendar fecha_fin = Calendar.getInstance(TimeZone.getTimeZone("GTM"));

        int ano = 0;
        int mes = 0;
        int dia = 0;

        Calendar calendar_semana = Calendar.getInstance(TimeZone.getTimeZone("GTM"));
        calendar_semana.add(Calendar.DATE, 8);
        Calendar calendar_hora = Calendar.getInstance(TimeZone.getTimeZone("GTM"));

        for (int r=0; fecha_ini.before(calendar_semana);r++){
            ano = fecha_ini.get(Calendar.YEAR);
            mes = fecha_ini.get(Calendar.MONTH);
            dia = fecha_ini.get(Calendar.DATE);
            fecha_ini.set(ano, mes, dia, 8, 0);
            fecha_fin.set(ano, mes, dia, 9, 0);

            calendar_hora.set(ano,mes,dia,20,0);

            for (int i = 0; fecha_ini.before(calendar_hora); i++){
                estado = "libre";
                SpaceHorarioDto spaceHorarioDto = new SpaceHorarioDto();
                Period period = new Period(fecha_ini.getTime(), fecha_fin.getTime());
                spaceHorarioDto.setPeriod(period);

                if (!bookings.isEmpty()) {

                    for (int j = 0; j < bookings.size(); j++) {
                        Booking booking = bookings.get(j);
                        if (booking.isActive()) {
                            Collection<Period> p = booking.getPeriod();
                            for (int k = 0; k < p.size(); k++) {
                                Calendar ini = Calendar.getInstance();
                                ini.setTime(booking.getPeriod().get(k).getstartDate());
                                Calendar fin = Calendar.getInstance();
                                fin.setTime(booking.getPeriod().get(k).getEndDate());

                                if (ini.equals(fecha_ini)) {
                                    estado = "ocupado";
                                } else if (fecha_ini.after(ini) && fecha_fin.before(fin)) {
                                    estado = "ocupado";
                                } else if (fecha_ini.after(ini) && fecha_ini.before(fin)) {
                                    estado = "ocupado";
                                } else if (fin.equals(fecha_fin)) {
                                    estado = "ocupado";
                                } else if (fecha_ini.before(ini) && fecha_fin.after(fin)) {
                                    estado = "ocupado";
                                }

                                if (estado.compareTo("ocupado") == 0 && booking.isEspecial()) {
                                    estado = "no reservable";
                                }
                            }
                        } else {
                            estado = "libre";
                        }

                    }

                } else{
                    estado="libre";
                }
                spaceHorarioDto.setState(estado);
                horarios.add(spaceHorarioDto);
                fecha_ini.add(Calendar.HOUR_OF_DAY,1);
                fecha_fin.add(Calendar.HOUR_OF_DAY,1);
            }
            fecha_ini.add(Calendar.DATE,1);
            fecha_fin.add(Calendar.DATE,1);
        }

        return horarios;
    }
}
