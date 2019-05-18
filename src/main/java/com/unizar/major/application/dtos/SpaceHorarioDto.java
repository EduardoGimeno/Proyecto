package com.unizar.major.application.dtos;

import com.unizar.major.domain.Period;

public class SpaceHorarioDto {

    Period period;
    String state;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
