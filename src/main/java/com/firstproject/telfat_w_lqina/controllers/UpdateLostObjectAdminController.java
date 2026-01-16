package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.models.LostObject;
import com.firstproject.telfat_w_lqina.models.Stadium;
import com.firstproject.telfat_w_lqina.Enum.TypeState;
import com.firstproject.telfat_w_lqina.service.LostObjectService;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class UpdateLostObjectAdminController {
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ComboBox<String> stadiumComboBox;
    @FXML
    private DatePicker lostDatePicker;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField ownerNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private ToggleGroup statusGroup;
    @FXML
    private RadioButton inStorageRadio;
    @FXML
    private RadioButton returnedRadio;


    private List<Stadium> stadiums ;
    private LostObject curentLostObject = SessionLostObject.getInstance().getCurentLostObject();
    private File selectedImageFile;

    private LostObjectService lostObjectService = new LostObjectService();


    @FXML
    public void initialize() {
        Image image=ImageConverterUtil.convertByteToImage(curentLostObject.getImage());
        imageView.setImage(image);
        descriptionField.setText(curentLostObject.getDescription());
        lostDatePicker.setValue(curentLostObject.getLostDate());
        ownerNameField.setText(curentLostObject.getAgentName());
        phoneField.setText(curentLostObject.getPhone());
        emailField.setText(curentLostObject.getEmail());
        typeComboBox.getSelectionModel().select(curentLostObject.getType());
        stadiums = StadiumService.getAllStadiums();
        stadiumComboBox.getItems().clear();
        for (Stadium stadium :stadiums){
            stadiumComboBox.getItems().add(stadium.getStadiumName());
        }
        stadiumComboBox.getSelectionModel().select(curentLostObject.getZone());
        if (curentLostObject.getTypeState()== TypeState.IN_STORAGE) {
            inStorageRadio.setSelected(true);
        } else if (curentLostObject.getTypeState() == TypeState.RETURNED) {
            returnedRadio.setSelected(true);
        }


    }

    @FXML
    public void goToDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/Admin.fxml");
    }

    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewLostObjectsAdmin.fxml");
    }

    @FXML
    public void createUser(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddUsers.fxml");
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
    public void chooseImage(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file!=null)
            selectedImageFile = file;

        if (selectedImageFile!=null){
            Image image = new Image(selectedImageFile.toURL().toString());
            imageView.setImage(image);
        }
    }

    @FXML
    public void handleSave(ActionEvent event) throws IOException {
        if (!validateFields()) {
            return;
        }

        // Mise à jour des valeurs depuis les champs
        curentLostObject.setDescription(descriptionField.getText());
        curentLostObject.setLostDate(lostDatePicker.getValue());
        curentLostObject.setAgentName(ownerNameField.getText());
        curentLostObject.setPhone(phoneField.getText());
        curentLostObject.setEmail(emailField.getText());
        curentLostObject.setType(typeComboBox.getSelectionModel().getSelectedItem());
        curentLostObject.setZone(stadiumComboBox.getSelectionModel().getSelectedItem());

        // Mise à jour de l'image si une nouvelle a été sélectionnée
        if (selectedImageFile != null) {
            try {
                byte[] imageBytes = ImageConverterUtil.convertImageToByte(selectedImageFile);
                curentLostObject.setImage(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Mise à jour du statut
        if (inStorageRadio.isSelected()) {
            curentLostObject.setTypeState(TypeState.IN_STORAGE);
        } else if (returnedRadio.isSelected()) {
            curentLostObject.setTypeState(TypeState.RETURNED);
        }

        // Sauvegarde
        SessionLostObject.getInstance().setCurentLostObject(curentLostObject);
        lostObjectService.updateLostObject(curentLostObject);

        // Navigation corrigée
        NavigationUtil.navigate(event, "/fxml/ViewLostObjectsAdmin.fxml");
    }

    private boolean validateFields() {
        if (lostDatePicker.getValue() == null) {
            Alerts.errorAlert("Date invalide","Date de perte obligatoire", "Veuillez sélectionner une date de perte.");
            return false;
        }

        if (!lostDatePicker.getValue().equals(curentLostObject.getLostDate())) {
            Alerts.errorAlert("Date invalide","Modification de la date non autorisée", "La date de perte ne peut pas être modifiée.");
            return false;
        }

        if (ownerNameField.getText() == null || ownerNameField.getText().trim().isEmpty()) {
            Alerts.errorAlert("le nom invalide","Nom obligatoire","Veuillez saisir le nom du propriétaire.");
            return false;
        }

        if (!ownerNameField.getText().equals(curentLostObject.getAgentName())) {
            Alerts.errorAlert("le nom invalide","Modification du nom non autorisée","Le nom du propriétaire ne peut pas être modifié.");
            return false;
        }

        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            Alerts.errorAlert("Téléphone invalide","Téléphone obligatoire", "Veuillez saisir un numéro de téléphone.");
            return false;
        }

        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            Alerts.errorAlert("Email invalide","Email obligatoire", "Veuillez saisir une adresse email.");
            return false;
        }

        if (!emailField.getText().equals(curentLostObject.getEmail())) {
            Alerts.errorAlert("Email invalide","Modification de l'email non autorisée", "L'adresse email ne peut pas être modifiée.");
            return false;
        }

        if (typeComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("Type invalide","Type obligatoire", "Veuillez sélectionner un type d'objet.");
            return false;
        }

        if (stadiumComboBox.getSelectionModel().getSelectedItem() == null) {
            Alerts.errorAlert("Stade invalide","Stade obligatoire", "Veuillez sélectionner un stade.");
            return false;
        }

        return true;
    }
}