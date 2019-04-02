package com.unizar.major.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_booking")
    private Long id;

    @Column(name="isperiodic")
    private Boolean isPeriodic;

    @Column(name="reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser")
    @JsonBackReference
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

    public Booking(Boolean isPeriodic, String reason, Period period){
        this.isPeriodic = isPeriodic;
        this.reason=reason;
        this.period = period;
    }
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }


    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
        if(!user.getBookings().contains(this)){
            user.setBookings(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsPeriodic() {
        return isPeriodic;
    }

    public void setIsPeriodica(Boolean isPeriodic) {
        this.isPeriodic = isPeriodic;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String motivo) {
        this.reason = motivo;
    }






}
