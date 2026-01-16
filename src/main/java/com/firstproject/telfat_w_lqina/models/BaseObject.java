package com.firstproject.telfat_w_lqina.models;

import jakarta.persistence.*;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

@Entity
@Table(name = "object")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "typeObjet", nullable = false)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "lostDate", nullable = false)
    private LocalDate lostDate;

    @Column(name = "zone", nullable = false)
    private String zone;



    public BaseObject(String description, Long id, LocalDate lostDate, String type, String zone ) {
        this.description = description;
        this.lostDate = lostDate;
        this.type = type;
        this.zone = zone;
        this.id = id;
    }
    public BaseObject(String description, LocalDate lostDate, String type, String zone) {
        this.description = description;
        this.lostDate = lostDate;
        this.type = type;
        this.zone = zone;
    }
    public BaseObject() {}

    // Getters

    public String getDescription() {
        return description;
    }

    public LocalDate getLostDate() {
        return lostDate;
    }

    public String getType() {
        return type;
    }

    public String getZone() {
        return zone;
    }
    public Long getId() {
        return id;
    }
    //Setters


    public void setDescription(String description) {
        this.description = description;
    }

    public void setLostDate(LocalDate lostDate) {
        this.lostDate = lostDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

}
