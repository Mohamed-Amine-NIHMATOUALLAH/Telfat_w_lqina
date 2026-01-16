package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.Enum.DocumentType;
import com.firstproject.telfat_w_lqina.Enum.PresenceProofType;
import com.firstproject.telfat_w_lqina.Enum.StatusComplaint;
import com.firstproject.telfat_w_lqina.Enum.TypeObjet;
import com.firstproject.telfat_w_lqina.dao.UserDAO;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidEmailException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidPhoneException;
import com.firstproject.telfat_w_lqina.models.*;
import com.firstproject.telfat_w_lqina.service.ComplaintService;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.*;
import com.mysql.cj.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class AddComplaintController {
    // Groupes pour les RadioButtons
    @FXML
    private final ToggleGroup documentTypeGroup = new ToggleGroup();
    @FXML
    private final ToggleGroup proofTypeGroup = new ToggleGroup();

    @FXML private TextField firstNameField;
    @FXML private TextField emailField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField documentNumberField;
    @FXML private RadioButton cinRadio;
    @FXML private RadioButton passportRadio;
    @FXML private ComboBox<String> objectTypeComboBox;
    @FXML private ComboBox<String> stadiumComboBox;
    @FXML private DatePicker lostDatePicker;
    @FXML private TextArea objectDescriptionArea;
    @FXML private RadioButton ticketRadio;
    @FXML private RadioButton badgeRadio;
    @FXML private ComboBox<String> complaintStatusComboBox;
    @FXML private ImageView imageView;
    @FXML private Label imageFileNameLabel;
    @FXML private Label labelUser;

    private StadiumService stadiumService = new StadiumService();
    private List<Stadium> stadiums = new ArrayList<>();
    private File selectedImageFile;
    private ComplaintService complaintService = new ComplaintService();
    private Agent currentAgent = (Agent) SessionManager.getInstance().getCurrentUser();

    public void initialize() {

        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        labelUser.setText(currentUser.getUsername());

        // Associer les groupes de boutons radio
        cinRadio.setToggleGroup(documentTypeGroup);
        passportRadio.setToggleGroup(documentTypeGroup);
        ticketRadio.setToggleGroup(proofTypeGroup);
        badgeRadio.setToggleGroup(proofTypeGroup);

        // Remplir les ComboBox
        objectTypeComboBox.getItems().addAll("Électronique","Documents","Objets personnels","Vêtements","Sacs","Équipement sportif","Accessoires","Divers","Autre / Inconnu");
        stadiums = stadiumService.getAllStadiums();
        for (Stadium stadium : stadiums){
            stadiumComboBox.getItems().add(stadium.getStadiumName());
        }
        stadiumComboBox.getSelectionModel().select(currentAgent.getStadium().getStadiumName());
        complaintStatusComboBox.getItems().addAll("Recherche en cours","Clôturée – objet non trouvé","Objet retrouvé");
    }

    @FXML
    public void chooseImage(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
            imageFileNameLabel.setText(selectedImageFile.getName());
        }
    }


    @FXML
    public void saveComplaint() throws IOException {
        // Validation des champs obligatoires
        if (objectTypeComboBox.getSelectionModel().isEmpty() ||
                stadiumComboBox.getSelectionModel().isEmpty() ||
                lostDatePicker.getValue() == null ||
                objectDescriptionArea.getText().isBlank() ||
                firstNameField.getText().isBlank() ||
                lastNameField.getText().isBlank() ||
                emailField.getText().isBlank() ||
                phoneField.getText().isBlank() ||
                documentNumberField.getText().isBlank() ||
                (documentTypeGroup.getSelectedToggle() == null) ||
                (proofTypeGroup.getSelectedToggle() == null) ||
                complaintStatusComboBox.getSelectionModel().isEmpty() ||
                selectedImageFile == null) {
            Alerts.errorAlert("Erreur de validation", "Champs obligatoires manquants",
                    "Veuillez remplir tous les champs obligatoires, y compris l'image de la preuve.");
            return;
        }

        try {
            // Création de la preuve
            Proof proof = new Proof();
            if (ticketRadio.isSelected()) {
                proof.setPresenceProofType(PresenceProofType.TICKET);
            } else if (badgeRadio.isSelected()) {
                proof.setPresenceProofType(PresenceProofType.BADGE);
            }
            proof.setProofImage(ImageConverterUtil.convertImageToByte(selectedImageFile));

            // Création de la personne
            Person person = new Person();
            person.setFirstName(firstNameField.getText().trim());
            person.setLastName(lastNameField.getText().trim());
            person.setEmail(emailField.getText().trim());
            person.setTelephone(phoneField.getText().trim());

            IdentityDocument identityDocument = new IdentityDocument();
            identityDocument.setDocumentNumber(documentNumberField.getText().trim());
            if (cinRadio.isSelected()) {
                identityDocument.setType(DocumentType.CIN);
            } else if (passportRadio.isSelected()) {
                identityDocument.setType(DocumentType.PASSPORT);
            }
            person.setIdentityDocument(identityDocument);

            // ✅ Création de l'objet perdu
            BaseObject lostObject = new BaseObject();
            String selectedType = objectTypeComboBox.getSelectionModel().getSelectedItem();

            switch (selectedType) {
                case "Électronique":
                    lostObject.setType(TypeObjet.ELECTRONIQUE.toString());
                    break;
                case "Documents":
                    lostObject.setType(TypeObjet.DOCUMENTS.toString());
                    break;
                case "Objets personnels":
                    lostObject.setType(TypeObjet.OBJETS_PERSONNELS.toString());
                    break;
                case "Vêtements":
                    lostObject.setType(TypeObjet.VETEMENTS.toString());
                    break;
                case "Sacs":
                    lostObject.setType(TypeObjet.SACS.toString());
                    break;
                case "Équipement sportif":
                    lostObject.setType(TypeObjet.EQUIPEMENT_SPORTIF.toString());
                    break;
                case "Accessoires":
                    lostObject.setType(TypeObjet.ACCESSOIRES.toString());
                    break;
                case "Divers":
                    lostObject.setType(TypeObjet.DIVERS.toString());
                    break;
                case "Autre / Inconnu":
                    lostObject.setType(TypeObjet.AUTRE_OU_INCONNU.toString());
                    break;
            }

            lostObject.setDescription(objectDescriptionArea.getText().trim());
            lostObject.setLostDate(lostDatePicker.getValue());

            // Récupérer le stade sélectionné
            String stadiumName = stadiumComboBox.getSelectionModel().getSelectedItem();
            Stadium selectedStadium = stadiums.stream()
                    .filter(s -> s.getStadiumName().equals(stadiumName))
                    .findFirst()
                    .orElse(null);

            if (selectedStadium != null) {
                lostObject.setZone(selectedStadium.getStadiumName());
            }

            // ✅ Création de la plainte avec le bon nom de méthode
            Complaint complaint = new Complaint();
            complaint.setPerson(person);
            complaint.setObject(lostObject);  // ✅ Changé de setObject() à setBaseObject()
            complaint.setProof(proof);

            String selectedStatus = complaintStatusComboBox.getSelectionModel().getSelectedItem();
            switch (selectedStatus) {
                case "Recherche en cours":
                    complaint.setStatus(StatusComplaint.SEARCH_IN_PROGRESS);
                    break;
                case "Clôturée – objet non trouvé":
                    complaint.setStatus(StatusComplaint.CLOSED_NOT_FOUND);
                    break;
                case "Objet retrouvé":
                    complaint.setStatus(StatusComplaint.FOUND);
                    break;
            }

            // ✅ Enregistrement avec gestion d'erreur améliorée
            boolean isCreated = complaintService.createComplaint(complaint);

            if (isCreated) {
                Alerts.successAlert("Réclamation ajoutée", "Succès", "La réclamation a été ajoutée avec succès.");
                clearForm();  // Nettoyer le formulaire après succès
                NavigationUtil.navigate(new ActionEvent(), "/fxml/ViewComplaintAgent.fxml");
            } else {
                Alerts.errorAlert("Erreur", "Échec de l'enregistrement",
                        "La réclamation n'a pas pu être enregistrée. Veuillez réessayer.");
            }

        } catch (InvalidEmailException invalidEmailException) {
            Alerts.errorAlert("Erreur de validation", "Email invalide", "Veuillez entrer un email valide.");
        } catch (InvalidPhoneException invalidPhoneException) {
            Alerts.errorAlert("Erreur de validation", "Numéro de téléphone invalide", "Veuillez entrer un numéro de téléphone valide.");
        } catch (Exception e) {
            e.printStackTrace();  // ✅ Afficher l'exception complète dans la console
            Alerts.errorAlert("Erreur", "Erreur lors de l'enregistrement",
                    "Une erreur est survenue : " + e.getMessage());
        }
    }
    @FXML
    public void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        documentNumberField.clear();
        documentTypeGroup.selectToggle(null);
        objectTypeComboBox.getSelectionModel().clearSelection();
        stadiumComboBox.getSelectionModel().clearSelection();
        lostDatePicker.setValue(null);
        objectDescriptionArea.clear();
        proofTypeGroup.selectToggle(null);
        complaintStatusComboBox.getSelectionModel().clearSelection();
        imageView.setImage(null);
        selectedImageFile = null;
        if (imageFileNameLabel != null) {
            imageFileNameLabel.setText("Aucune image sélectionnée");
        }
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
    public void goToAddLostObject(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddLostObject.fxml");
    }

    @FXML
    public void goToListComplaint(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewComplaintAgent.fxml");
    }

}