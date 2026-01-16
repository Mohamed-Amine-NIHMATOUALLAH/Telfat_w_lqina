package com.firstproject.telfat_w_lqina.service;

import com.firstproject.telfat_w_lqina.dao.StadiumDAO;
import com.firstproject.telfat_w_lqina.models.Stadium;

import java.util.List;

public class StadiumService {

    private static final StadiumDAO stadiumDAO = new StadiumDAO();
    private static Stadium selectedStadium;

    public static void addStadium(String name, String city) {
        if (name.length() < 3) {
            throw new IllegalArgumentException(
                    "Le nom du stade doit contenir au moins 3 caractères!");
        }

        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("La ville est requise!");
        }

        if (stadiumDAO.existsByName(name)) {
            throw new IllegalArgumentException("Ce stade existe déjà!");
        }

        Stadium stadium = new Stadium();
        stadium.setStadiumName(name);
        stadium.setCity(city);

        stadiumDAO.saveStadium(stadium);
    }

    public static void updateStadium(Long id, String name, String city) {
        if (name.length() < 3) {
            throw new IllegalArgumentException(
                    "Le nom du stade doit contenir au moins 3 caractères!");
        }

        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("La ville est requise!");
        }

        Stadium stadium = stadiumDAO.findById(id);
        if (stadium == null) {
            throw new IllegalArgumentException("Stade introuvable!");
        }

        // Vérifier si le nom existe déjà (mais pas pour ce stade)
        if (stadiumDAO.existsByNameExcludingId(name, id)) {
            throw new IllegalArgumentException("Ce nom de stade existe déjà!");
        }

        stadium.setStadiumName(name);
        stadium.setCity(city);
        stadiumDAO.updateStadium(stadium);
    }

    public static void deleteStadium(Long id) {
        stadiumDAO.deleteStadium(id);
    }

    public static List<Stadium> getAllStadiums(){
        return stadiumDAO.getAllStadiums();
    }

    public static void setSelectedStadium(Stadium stadium) {
        selectedStadium = stadium;
    }

    public static Stadium getSelectedStadium() {
        return selectedStadium;
    }
}