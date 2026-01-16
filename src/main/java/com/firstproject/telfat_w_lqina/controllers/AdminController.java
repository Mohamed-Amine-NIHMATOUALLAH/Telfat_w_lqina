package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import javafx.event.ActionEvent;

public class AdminController {
    @FXML
    private Label labelAdmin;


    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        labelAdmin.setText(currentUser.getUsername());
    }

    @FXML
    public void createUser(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddUsers.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }

    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewLostObjectsAdmin.fxml");
    }

    @FXML
    public void addStadium(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddStadium.fxml");
    }

    @FXML
    public void goToStadiumList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewStadiums.fxml");
    }

    @FXML
    public void goToComplaintList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewComplaintAdmin.fxml");
    }

    @FXML
    public void AfficherStatistique(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AdminStatistics.fxml");
    }
}
