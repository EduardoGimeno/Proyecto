package com.unizar.major.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Embeddable
public class Period {


    private String startDate;

    private String endDate;

    public Period(){}

    public Period(String startDate, String endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getstartDate() {
        return startDate;
    }

    public void setstartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString(){
        return getstartDate()+ "-" + getEndDate();
    }


}
