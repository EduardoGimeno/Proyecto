package com.unizar.major.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "personEina")
public class User{

    public enum Rol {
        ESTUDIANTE, PDI, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="iduser")
    private Long id;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name="rol")
    private Rol rol;

    @Column(name="nombreusuario")
    private String userName;

    @Column(name="email")
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Booking> bookings;

    @Column(name="active")
    private boolean active;

    @Column(name="password")
    private String password;


    public User(){

    }

    public User(String firstName, String lastName, Rol rol, String nameuser, String email, String password){
        this.firstName=firstName;
        this.lastName = lastName;
        this.rol = rol;
        this.userName = nameuser;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String rol, String nameuser, String email, String password){
        this.firstName=firstName;
        this.lastName = lastName;
        this.rol = Rol.valueOf(rol.toUpperCase());
        this.userName = nameuser;
        this.email = email;
        this.password = password;
    }

    public List<Booking> getBookings(){
        return bookings;
    }

    public void setBookings(Booking booking){
        if (booking.getUser()==null){
            booking.setUser(this);
        }
        if(!this.bookings.contains(booking)){
            this.bookings.add(booking);
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Rol getRol(){
        return rol;
    }

    public void setRol(Rol rol){
        this.rol = rol;
    }

    public void setRol(String rol) { this.rol = Rol.valueOf(rol); }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
