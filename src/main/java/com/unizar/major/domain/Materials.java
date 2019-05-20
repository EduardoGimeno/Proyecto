package com.unizar.major.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Materials {
    @Column(name = "projector")
    private boolean proyector;
    @Column(name= "boards")
    private int pizarra;
    @Column(name="screen")
    private boolean pantalla;
    @Column(name="computers")
    private int ordenadores;
    @Column(name="chairs")
    private int sillas;
    @Column(name="tables")
    private int mesas ;

    public Materials(){
    }

    public Materials(boolean proyector, int pizarra, boolean pantalla, int ordenadores, int sillas, int mesas){
        this.proyector=proyector;
        this.pizarra=pizarra;
        this.pantalla=pantalla;
        this.ordenadores=ordenadores;
        this.sillas=sillas;
        this.mesas=mesas;
    }

    public boolean isProyector() {
        return proyector;
    }

    public void setProyector(boolean proyector) {
        this.proyector = proyector;
    }

    public int getPizarra() {
        return pizarra;
    }

    public void setPizarra(int pizarra) {
        this.pizarra = pizarra;
    }

    public boolean isPantalla() {
        return pantalla;
    }

    public void setPantalla(boolean pantalla) {
        this.pantalla = pantalla;
    }

    public int getOrdenadores() {
        return ordenadores;
    }

    public void setOrdenadores(int ordenadores) {
        this.ordenadores = ordenadores;
    }

    public int getSillas() {
        return sillas;
    }

    public void setSillas(int sillas) {
        this.sillas = sillas;
    }

    public int getMesas() {
        return mesas;
    }

    public void setMesas(int mesas) {
        this.mesas = mesas;
    }
}
