package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.models.Stadium;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class UpdateStadiumController {

    @FXML
    private TextField stadiumNameField;

    @FXML
    private TextField cityField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label labelAdmin;

    private Stadium currentStadium;

    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            labelAdmin.setText(currentUser.getUsername());
        }

        currentStadium = StadiumService.getSelectedStadium();

        if (currentStadium != null) {
            stadiumNameField.setText(currentStadium.getStadiumName());
            cityField.setText(currentStadium.getCity() != null ? currentStadium.getCity() : "");
        }

        // Ajouter un listener pour styliser les champs lors du focus
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

    // Navigation methods for sidebar
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

    @FXML
    public void handleUpdateStadium(ActionEvent event) {
        errorLabel.setVisible(false);

        String stadiumName = stadiumNameField.getText().trim();
        String city = cityField.getText().trim();

        if (stadiumName.isEmpty()) {
            showError("Le nom du stade est requis!");
            return;
        }

        if (city.isEmpty()) {
            showError("La ville est requise!");
            return;
        }

        try {
            StadiumService.updateStadium(currentStadium.getId(), stadiumName, city);
            showSuccessAndNavigate("✓ Stade modifié avec succès!");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Erreur système");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteStadium(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer le stade");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce stade ?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                StadiumService.deleteStadium(currentStadium.getId());

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("✓ Stade supprimé avec succès!");
                successAlert.showAndWait();

                goToStadiumList(event);
            } catch (Exception e) {
                showError("Erreur lors de la suppression");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void goToStadiumList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewStadiums.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(javafx.scene.paint.Color.web("#dc3545"));
        errorLabel.setVisible(true);

        // Style du champ en erreur
        stadiumNameField.setStyle("-fx-background-color: #fff5f5; -fx-border-color: #dc3545; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 14 20; -fx-font-size: 14px;");
    }

    private void showSuccessAndNavigate(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        try {
            goToStadiumList(new ActionEvent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

