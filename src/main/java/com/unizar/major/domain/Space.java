package com.unizar.major.domain;

import javax.persistence.*;

@Entity
@Table(name = "space")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="spaceId")
    private long id;

    @Column(name="plant")
    private String plant;

    @Column(name="building")
    private String building;

    public Space() {
    }

    public Space(String plant, String building) {
        this.plant = plant;
        this.building = building;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
