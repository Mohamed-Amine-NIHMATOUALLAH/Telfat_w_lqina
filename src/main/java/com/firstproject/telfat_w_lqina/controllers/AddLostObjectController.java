package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.Enum.UserType;
import com.firstproject.telfat_w_lqina.models.*;
import com.firstproject.telfat_w_lqina.service.LostObjectService;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.Alerts;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;   // Pour l'élément ImageView
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class AddLostObjectController {
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker lostDatePicker;
    @FXML private ChoiceBox<String> stadiumComboBox;
    @FXML private TextField ownerNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private ImageView imageView;
    @FXML private Label labelUser;

    private final LostObjectService lostObjectService = new LostObjectService();
    private File selectedImageFile;


    public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        ownerNameField.setText(currentUser.getUsername());
        phoneField.setText(currentUser.getTelephone());
        emailField.setText(currentUser.getEmail());
        loadStadiums();
        labelUser.setText(currentUser.getUsername());

    }

    private void loadStadiums() {
        List<Stadium> stadiums = StadiumService.getAllStadiums();
        stadiumComboBox.getItems().clear();
        for (Stadium stadium : stadiums) {
            stadiumComboBox.getItems().add(stadium.getStadiumName());
        }

        if (SessionManager.getInstance().getCurrentUser().getUserType().equals(UserType.AGENT)) {
            Agent currentAgent = (Agent) SessionManager.getInstance().getCurrentUser();
            String stadiumName = currentAgent.getStadium().getStadiumName();
            stadiumComboBox.getSelectionModel().select(stadiumName);
        }
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
    public void handleSave() throws IOException {
        // Vérification champs vides
        if (typeComboBox.getValue() == null ||
                descriptionField.getText().isBlank() ||
                lostDatePicker.getValue() == null ||
                stadiumComboBox.getValue() == null || stadiumComboBox.getValue().isBlank() ||
                ownerNameField.getText().isBlank() ||
                phoneField.getText().isBlank() ||
                emailField.getText().isBlank() ||
                selectedImageFile == null) {

            Alerts.errorAlert(
                    "Champs manquants",
                    "Formulaire incomplet",
                    "Veuillez remplir tous les champs avant d'enregistrer."
            );
            return;
        }

        try {
            // Créer l'objet avec le constructeur complet pour éviter les problèmes de nullable
            LostObject obj = new LostObject(
                    ownerNameField.getText(),    // agentName
                    descriptionField.getText(),  // description
                    emailField.getText(),        // email
                    lostDatePicker.getValue(),   // lostDate
                    phoneField.getText(),        // phone
                    typeComboBox.getValue(),     // type
                    stadiumComboBox.getValue(),  // zone
                    null                         // image (sera ajoutée par le service)
            );

            // Appeler le service
            lostObjectService.addLostObject(obj, selectedImageFile);

            // Alert de succès
            Alerts.successAlert("Succès", "", "Objet perdu enregistré avec succès.");
            NavigationUtil.navigate(new ActionEvent(), "/fxml/ViewLostObjectsAgent.fxml");

        } catch (Exception e) {
            Alerts.errorAlert("Erreur", "Erreur lors de l'enregistrement",
                    "Une erreur s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void reinitialiser(ActionEvent event) {
        typeComboBox.getSelectionModel().clearSelection();
        descriptionField.clear();
        lostDatePicker.setValue(null);
    }


    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewLostObjectsAgent.fxml");
    }

    @FXML
    public void goTDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/Agent.fxml");
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
