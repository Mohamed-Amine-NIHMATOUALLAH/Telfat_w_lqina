package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.exception.creationexception.DuplicateEmailException;
import com.firstproject.telfat_w_lqina.exception.creationexception.DuplicateUsernameException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidEmailException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidPasswordException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidPhoneException;
import com.firstproject.telfat_w_lqina.models.Admin;
import com.firstproject.telfat_w_lqina.models.Agent;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.models.Stadium;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.service.UserService;
import com.firstproject.telfat_w_lqina.dao.StadiumDAO;
import com.firstproject.telfat_w_lqina.util.Alerts;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;


import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

public class AddUsersController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> userTypeComboBox;

    @FXML
    private ComboBox<Stadium> stadiumComboBox;

    @FXML
    private VBox stadiumContainer;

    @FXML
    private javafx.scene.control.Label labelAdmin;


    UserService userService = new UserService();
    StadiumDAO stadiumDAO = new StadiumDAO();



    public void initialize() {
        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = com.firstproject.telfat_w_lqina.util.SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && labelAdmin != null) {
            labelAdmin.setText(currentUser.getUsername());
        }

        userTypeComboBox.getItems().addAll("ADMIN", "AGENT");

        // Charger les stades disponibles
        loadStadiums();

        // Configurer l'affichage des stades dans le ComboBox
        //Had partie 9aditha b IA hitach m3rftch liha !!!
        stadiumComboBox.setCellFactory(param -> new ListCell<Stadium>() {
            @Override
            protected void updateItem(Stadium item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getStadiumName());
                }
                userTypeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
                    if ("ADMIN".equals(newValue)) {
                        stadiumContainer.setVisible(false);
                        stadiumContainer.setManaged(false);
                    } else {
                        stadiumContainer.setVisible(true);
                        stadiumContainer.setManaged(true);
                    }
                });
            }
        });

        stadiumComboBox.setButtonCell(new ListCell<Stadium>() {
            @Override
            protected void updateItem(Stadium item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Sélectionner un stade");
                } else {
                    setText(item.getStadiumName());
                }
            }
        });
    }
    //Had function 9adita b IA hitach m3rftch liha !!!
    private void loadStadiums() {
            List<Stadium> stadiums = StadiumService.getAllStadiums();
            stadiumComboBox.getItems().clear();
            stadiumComboBox.getItems().addAll(stadiums);
    }

    @FXML
    public void createUser(ActionEvent event) throws IOException {
        if (userTypeComboBox.getValue() == null) {
            Alerts.errorAlert("Validation Error", "Role not selected","Please select a user role.");
            return;
        }
        if (userTypeComboBox.getValue().equals("ADMIN")){
            Admin admin = new Admin(usernameTextField.getText(),telephoneTextField.getText(),passwordField.getText(),emailTextField.getText());
            try {
                userService.createUser((User) admin);
            }catch (InvalidEmailException invalidEmailException){
                Alerts.errorAlert("Validation Error","Error syntaxe email","Exemple Email : user@gmail.com");
            }
            catch (InvalidPhoneException invalidPhoneException){
                Alerts.errorAlert("Validation Error","Error syntaxe Telephone","Exemple Telephone : 0xxxxxxxxx or +212 xxxxxxxxx ");
            }
            catch (InvalidPasswordException invalidPasswordException){
                Alerts.errorAlert("Validation Error","Error syntaxe Password","Pleasse choose another Password");
            }
            catch (DuplicateUsernameException duplicateUsernameException){
                Alerts.errorAlert("Creation Error","Username already exists","Please choose another username.");
            }
            catch (DuplicateEmailException duplicateEmailException){
                Alerts.errorAlert("Creation Error","Email already exists","Please choose another email.");
            };
            NavigationUtil.navigate(event,"/fxml/AddUsers.fxml");
            return;
        }
        if (userTypeComboBox.getValue().equals("AGENT")){
            Agent agent = new Agent(usernameTextField.getText(),telephoneTextField.getText(),passwordField.getText(),emailTextField.getText());

            // Associer le stade si sélectionné
            if (stadiumComboBox.getValue() != null) {
                agent.setStadium(stadiumComboBox.getValue());
            }

            try {
                userService.createUser((User) agent);
                Alerts.successAlert("Succès", "Agent créé", "L'agent a été créé avec succès.");
            } catch (InvalidEmailException invalidEmailException){
                Alerts.errorAlert("Validation Error","Error syntaxe email","Exemple Email : user@gmail.com");
                return;
            }
            catch (InvalidPhoneException invalidPhoneException){
                Alerts.errorAlert("Validation Error","Error syntaxe Telephone","Exemple Telephone : 0xxxxxxxxx or +212 xxxxxxxxx ");
                return;
            }
            catch (InvalidPasswordException invalidPasswordException){
                Alerts.errorAlert("Validation Error","Error syntaxe Password","Pleasse choose another Password");
                return;
            }
            catch (DuplicateUsernameException duplicateUsernameException){
                Alerts.errorAlert("Creation Error","Username already exists","Please choose another username.");
                return;
            }
            catch (DuplicateEmailException duplicateEmailException){
                Alerts.errorAlert("Creation Error","Email already exists","Please choose another email.");
                return;
            }
            NavigationUtil.navigate(event,"/fxml/AddUsers.fxml");
            return;
        }
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/Admin.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }

    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewLostObjectsAdmin.fxml");
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
    public void goToDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/Admin.fxml");
    }

    @FXML
    public void goToComplaintList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewComplaintAdmin.fxml");
    }

    @FXML
    public void goToStatistique(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AdminStatistics.fxml");
    }

}
