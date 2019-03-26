package com.unizar.major.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="idbooking")
    private long id;

    @Column(name="isperiodic")
    private Boolean isPeriodic;

    @Column(name="reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "iduser")
    private User user;



    @Embedded
    @AttributeOverrides(value= {
            @AttributeOverride(name="startDate", column = @Column(name="startDate")),
            @AttributeOverride(name="endDate", column = @Column(name="endDate"))
    })
    private Period period;

    public Booking(){

    }

    public Booking(Boolean isPeriodic, String reason, Period period, User user){
        this.isPeriodic = isPeriodic;
        this.reason=reason;
        this.period = period;
        this.user = user;
    }


    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }


   /* public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getIsPeriodic() {
        return isPeriodic;
    }

    public void setEsPeriodica(Boolean isPeriodic) {
        this.isPeriodic = isPeriodic;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String motivo) {
        this.reason = motivo;
    }






}
