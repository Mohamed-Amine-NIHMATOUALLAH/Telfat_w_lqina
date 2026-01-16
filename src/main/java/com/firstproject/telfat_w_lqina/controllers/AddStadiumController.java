package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddStadiumController {

    @FXML
    private TextField stadiumNameField;

    @FXML
    private TextField cityField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label labelAdmin;

    private EntityManagerFactory emf;
    private EntityManager em;

    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            labelAdmin.setText(currentUser.getUsername());
        }

        // Ajouter un listener pour styliser le champ lors du focus
        stadiumNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                stadiumNameField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #006233; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 14 20; -fx-font-size: 14px;");
            } else {
                stadiumNameField.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1.5; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 14 20; -fx-font-size: 14px;");
            }
        });

        cityField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                cityField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #C1272D; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 14 20; -fx-font-size: 14px;");
            } else {
                cityField.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1.5; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 14 20; -fx-font-size: 14px;");
            }
        });
    }

    @FXML
    public void handleAddStadium(ActionEvent event) {
        errorLabel.setVisible(false);

        String stadiumName = stadiumNameField.getText().trim();
        String city = cityField.getText().trim();

        if (stadiumName.isEmpty()) {
            showError("⚠️ Le nom du stade est requis!");
            return;
        }

        if (city.isEmpty()) {
            showError("⚠️ La ville est requise!");
            return;
        }

        try {
            StadiumService.addStadium(stadiumName, city);
            showSuccess("✓ Stade ajouté avec succès!");
            stadiumNameField.clear();
            cityField.clear();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("❌ Erreur système");
        }
    }


    @FXML
    public void goToDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/Admin.fxml");
    }

    @FXML
    public void createUser(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddUsers.fxml");
    }

    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewLostObjectsAdmin.fxml");
    }

    @FXML
    public void addStadium(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddStadium.fxml");
    }

    @FXML
    public void goToStadiumList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewStadiums.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }

    @FXML
    public void goToComplaintList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewComplaintAdmin.fxml");
    }

    @FXML
    public void goToStatistique(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AdminStatistics.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(javafx.scene.paint.Color.web("#dc3545"));
        errorLabel.setVisible(true);

        // Style du champ en erreur
        stadiumNameField.setStyle("-fx-background-color: #fff5f5; -fx-border-color: #dc3545; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 14 20; -fx-font-size: 14px;");
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(javafx.scene.paint.Color.web("#28a745"));
        errorLabel.setVisible(true);

        // Style du champ en succès
        stadiumNameField.setStyle("-fx-background-color: #f0fdf4; -fx-border-color: #28a745; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12 18; -fx-font-size: 14px;");
    }

}
