package com.unizar.major.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_booking")
    private Long id;

    @Column(name="isperiodic")
    private boolean isPeriodic;

    @Column(name="reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser")
    @JsonBackReference
    private User user;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Booking_Period", joinColumns = @JoinColumn(name="id_booking"))
    private List<Period> periods = new ArrayList<>();

    @Column(name="state")
    private String state;

    @Column(name="active")
    private boolean active;

    @Column(name="periodRep")
    private String periodRep;

    @Column(name="finalDate")
    private Date finalDate;

    @Column(name="especial")
    private boolean especial;

    @ManyToMany
    @JoinTable(name ="booking_space", joinColumns = @JoinColumn(name="booking"),
    inverseJoinColumns = @JoinColumn(name="space_gid"))
    @JsonManagedReference
    private List<Space> spaces = new ArrayList<>();


    public Booking(){

    }

    public Booking(boolean isPeriodic, String reason, List<Period> periods){
        this.isPeriodic = isPeriodic;
        this.reason=reason;
        this.periods = periods;
    }

    public Booking(boolean isPeriodic, String reason, List<Period> periods, String periodRep, Date finalDate){
        this.isPeriodic = isPeriodic;
        this.reason=reason;
        this.periods = periods;
        this.periodRep = periodRep;
        this.finalDate = finalDate;
    }
    public List<Period> getPeriod() {
        return this.periods;
    }

    public void setPeriod(List<Period> period) {
        this.periods = period;
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

    public boolean isIsPeriodic() {
        return isPeriodic;
    }

    public void setIsPeriodic(boolean isPeriodic) {
        this.isPeriodic = isPeriodic;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String motivo) {
        this.reason = motivo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPeriodRep() {
        return periodRep;
    }

    public void setPeriodRep(String periodRep) {
        this.periodRep = periodRep;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public boolean isEspecial() {
        return especial;
    }

    public void setEspecial(boolean especial) {
        this.especial = especial;
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(Space space) {
        this.spaces.add(space);
        if(!space.getBookings().contains(this)){
            user.setBookings(this);
        }

    }
}
