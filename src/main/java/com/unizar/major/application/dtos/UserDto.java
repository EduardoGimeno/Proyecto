package com.unizar.major.application.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unizar.major.domain.Booking;

import java.util.List;

public class UserDto {


    private long id;

    private String firstName;

    private String lastName;

    private String rol;

    private String userName;

    private String email;

    private String password;

    @JsonIgnore
    private List<Booking> bookings;

    public UserDto(long id, String firstName, String lastName, String rol, String userName, String email, String pwd){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rol = rol;
        this.userName = userName;
        this.email= email;
        this.password = pwd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
