package com.unizar.major.domain;

import javax.persistence.Embeddable;
import java.awt.*;
import java.util.Set;

@Embeddable
public class Geometry {

    private Polygon geom;

    public Geometry(){

    }
    public Geometry(Polygon geom) {
        this.geom = geom;
    }

    public Polygon getPolygon() {
        return geom;
    }

    public void setPolygon(Polygon geom) {
        this.geom = geom;
    }
}
