package com.firstproject.telfat_w_lqina.models;


import jakarta.persistence.*;

@Entity
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NomStade", nullable = false, unique = true)
    private String StadiumName;

    @Column(name = "Ville")
    private String city;

    // Constructeurs
    public Stadium() {
    }

    public Stadium(String stadiumName) {
        this.StadiumName = stadiumName;
    }

    public Stadium(String stadiumName, String city) {
        this.StadiumName = stadiumName;
        this.city = city;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public String getStadiumName() {
        return StadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.StadiumName = stadiumName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Stadium{" +
                "id=" + id +
                ", StadiumName='" + StadiumName + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
