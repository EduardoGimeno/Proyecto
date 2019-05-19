package com.unizar.major.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "espacios")
public class Space {

    private String id;

    @Id
    private int gid;
    private String layer;
    private String subclasses;
    private String extendeden;
    private String linetype;
    private String entityhand;
    private String text;
    private double area;
    private double perimeter;

    @Type(type="com.vividsolutions.jts.geom.Geometry")
    @JsonIgnore
    private MultiPolygon geom;

    @Embedded
    private DataSpace dataSpace;

    @Embedded
    private Materials materials;

    @ManyToMany(mappedBy="spaces")//,fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference
    private List<Booking> bookings;

    public Space(){

    }

    public Space(String id, int gid, String layer, String subclasses, String extendeden, String linetype, String entityhand, String text, double area, double perimeter, MultiPolygon geom, DataSpace dataSpace) {
        this.id = id;
        this.gid = gid;
        this.layer = layer;
        this.subclasses = subclasses;
        this.extendeden = extendeden;
        this.linetype = linetype;
        this.entityhand = entityhand;
        this.text = text;
        this.area = area;
        this.perimeter = perimeter;
        this.geom = geom;
        this.dataSpace = dataSpace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getSubclasses() {
        return subclasses;
    }

    public void setSubclasses(String subclasses) {
        this.subclasses = subclasses;
    }

    public String getExtendeden() {
        return extendeden;
    }

    public void setExtendeden(String extendeden) {
        this.extendeden = extendeden;
    }

    public String getLinetype() {
        return linetype;
    }

    public void setLinetype(String linetype) {
        this.linetype = linetype;
    }

    public String getEntityhand() {
        return entityhand;
    }

    public void setEntityhand(String entityhand) {
        this.entityhand = entityhand;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area/10000;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

   public MultiPolygon getGeom() {
        return geom;
    }

    public void setGeom(MultiPolygon geom) {
        this.geom = geom;
    }

    public DataSpace getDataSpace() {
        return dataSpace;
    }

    public void setDataSpace(DataSpace dataSpace) {
        this.dataSpace = dataSpace;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Booking booking) {
        if (booking.getSpaces()==null){
            booking.setSpaces(this);
        }
        if(!this.bookings.contains(booking)) {
            this.bookings.add(booking);
        }

    }

    public Materials getMaterials() {
        return materials;
    }

    public void setMaterials(Materials materials) {
        this.materials = materials;
    }
}
