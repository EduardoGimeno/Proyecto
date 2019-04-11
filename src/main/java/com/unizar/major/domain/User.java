package com.unizar.major.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "personEina")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="iduser")
    private Long id;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="rol")
    private String rol;

    @Column(name="nombreusuario")
    private String nombreUsuario;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Booking> bookings;


    public User(){

    }

    public User(String firstName, String lastName, String rol, String nombreUsuario){
        this.firstName=firstName;
        this.lastName = lastName;
        this.rol = rol;
        this.nombreUsuario = nombreUsuario;
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

    public String getRol(){
        return rol;
    }

    public void setRol(String rol){
        this.rol = rol;
    }

    public String getNombreUsuario(){
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario){
        this.nombreUsuario = nombreUsuario;
    }
}
