package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class AgentController {


    @FXML
    private Label labelAgent;

    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        labelAgent.setText(currentUser.getUsername());
    }

    @FXML
    public void addLostObject(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddLostObject.fxml");
    }

    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewLostObjectsAgent.fxml");
    }

    @FXML
    public void seDeconnecter(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }

    @FXML
    public void goToAddComplaint(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddComplaint.fxml");
    }

    @FXML
    public void goToListComplaint(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewComplaintAgent.fxml");
    }
}