package com.unizar.major.dtos;


import com.unizar.major.domain.Period;


public class BookingDto {


    private long id;

    private Boolean isPeriodic;

    private String reason;

    private Period period;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getIsPeriodic() {
        return isPeriodic;
    }

    public void setIsPeriodic(Boolean isPeriodic) {
        this.isPeriodic = isPeriodic;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }



    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
