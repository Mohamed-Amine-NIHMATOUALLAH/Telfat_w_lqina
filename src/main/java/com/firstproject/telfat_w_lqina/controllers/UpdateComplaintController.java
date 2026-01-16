package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.Enum.DocumentType;
import com.firstproject.telfat_w_lqina.Enum.PresenceProofType;
import com.firstproject.telfat_w_lqina.Enum.StatusComplaint;
import com.firstproject.telfat_w_lqina.Enum.TypeObjet;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidEmailException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidPhoneException;
import com.firstproject.telfat_w_lqina.models.*;
import com.firstproject.telfat_w_lqina.service.ComplaintService;
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
import java.util.ArrayList;
import java.util.List;

public class UpdateComplaintController {

    // Groupes pour les RadioButtons
    @FXML private final ToggleGroup documentTypeGroup = new ToggleGroup();
    @FXML private final ToggleGroup proofTypeGroup = new ToggleGroup();

    // Champs de formulaire
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

    // Services et données
    private StadiumService stadiumService = new StadiumService();
    private ComplaintService complaintService = new ComplaintService();
    private List<Stadium> stadiums = new ArrayList<>();
    private File selectedImageFile;
    private Complaint currentComplaint = SessionComplaint.getInstance().getCurentComplaint();

    /**
     * Initialise le formulaire avec les données de la réclamation courante
     */
    @FXML
    public void initialize() {
        // Associer les groupes de boutons radio
        cinRadio.setToggleGroup(documentTypeGroup);
        passportRadio.setToggleGroup(documentTypeGroup);
        ticketRadio.setToggleGroup(proofTypeGroup);
        badgeRadio.setToggleGroup(proofTypeGroup);

        // Charger les informations de la personne
        Person person = currentComplaint.getPerson();
        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        emailField.setText(person.getEmail());
        phoneField.setText(person.getTelephone());

        // Charger le document d'identité
        IdentityDocument identityDocument = person.getIdentityDocument();
        documentNumberField.setText(identityDocument.getDocumentNumber());
        if (identityDocument.getType() == DocumentType.CIN) {
            cinRadio.setSelected(true);
        } else if (identityDocument.getType() == DocumentType.PASSPORT) {
            passportRadio.setSelected(true);
        }

        // Charger les informations de l'objet perdu
        BaseObject object = currentComplaint.getObject();

        // Remplir la ComboBox des types d'objets
        objectTypeComboBox.getItems().addAll(
                "Électronique",
                "Documents",
                "Objets personnels",
                "Vêtements",
                "Sacs",
                "Équipement sportif",
                "Accessoires",
                "Divers",
                "Autre / Inconnu"
        );

        // Sélectionner le type d'objet actuel
        String objectType = object.getType();
        if (objectType.equals(TypeObjet.ELECTRONIQUE.toString())) {
            objectTypeComboBox.setValue("Électronique");
        } else if (objectType.equals(TypeObjet.DOCUMENTS.toString())) {
            objectTypeComboBox.setValue("Documents");
        } else if (objectType.equals(TypeObjet.OBJETS_PERSONNELS.toString())) {
            objectTypeComboBox.setValue("Objets personnels");
        } else if (objectType.equals(TypeObjet.VETEMENTS.toString())) {
            objectTypeComboBox.setValue("Vêtements");
        } else if (objectType.equals(TypeObjet.SACS.toString())) {
            objectTypeComboBox.setValue("Sacs");
        } else if (objectType.equals(TypeObjet.EQUIPEMENT_SPORTIF.toString())) {
            objectTypeComboBox.setValue("Équipement sportif");
        } else if (objectType.equals(TypeObjet.ACCESSOIRES.toString())) {
            objectTypeComboBox.setValue("Accessoires");
        } else if (objectType.equals(TypeObjet.DIVERS.toString())) {
            objectTypeComboBox.setValue("Divers");
        } else if (objectType.equals(TypeObjet.AUTRE_OU_INCONNU.toString())) {
            objectTypeComboBox.setValue("Autre / Inconnu");
        }

        // Charger les stades
        stadiums = StadiumService.getAllStadiums();
        for (Stadium stadium : stadiums) {
            stadiumComboBox.getItems().add(stadium.getStadiumName());
        }
        stadiumComboBox.setValue(object.getZone());

        // Charger la date et la description
        lostDatePicker.setValue(object.getLostDate());
        objectDescriptionArea.setText(object.getDescription());

        // Charger les informations de la preuve
        Proof proof = currentComplaint.getProof();
        if (proof.getPresenceProofType() == PresenceProofType.TICKET) {
            ticketRadio.setSelected(true);
        } else if (proof.getPresenceProofType() == PresenceProofType.BADGE) {
            badgeRadio.setSelected(true);
        }

        // Afficher l'image de la preuve
        imageView.setImage(ImageConverterUtil.convertByteToImage(proof.getProofImage()));
        imageFileNameLabel.setText("Image actuelle chargée");

        // Charger le statut de la réclamation
        complaintStatusComboBox.getItems().addAll(
                "Recherche en cours",
                "Clôturée – objet non trouvé",
                "Objet retrouvé"
        );

        StatusComplaint status = currentComplaint.getStatus();
        if (status == StatusComplaint.SEARCH_IN_PROGRESS) {
            complaintStatusComboBox.setValue("Recherche en cours");
        } else if (status == StatusComplaint.CLOSED_NOT_FOUND) {
            complaintStatusComboBox.setValue("Clôturée – objet non trouvé");
        } else if (status == StatusComplaint.FOUND) {
            complaintStatusComboBox.setValue("Objet retrouvé");
        }
    }

    /**
     * Permet de choisir une nouvelle image pour la preuve
     */
    @FXML
    public void chooseImage(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
            imageFileNameLabel.setText(selectedImageFile.getName());
        }
    }

    /**
     * Met à jour la réclamation avec les nouvelles valeurs du formulaire
     */
    @FXML
    public void updateComplaint() throws IOException {
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
                documentTypeGroup.getSelectedToggle() == null ||
                proofTypeGroup.getSelectedToggle() == null ||
                complaintStatusComboBox.getSelectionModel().isEmpty()) {

            Alerts.errorAlert(
                    "Erreur de validation",
                    "Champs obligatoires manquants",
                    "Veuillez remplir tous les champs obligatoires."
            );
            return;
        }

        try {
            // ========== MISE À JOUR DE LA PERSONNE ==========
            Person person = currentComplaint.getPerson();
            person.setFirstName(firstNameField.getText().trim());
            person.setLastName(lastNameField.getText().trim());
            person.setEmail(emailField.getText().trim());
            person.setTelephone(phoneField.getText().trim());

            // Mise à jour du document d'identité
            IdentityDocument identityDocument = person.getIdentityDocument();
            identityDocument.setDocumentNumber(documentNumberField.getText().trim());
            if (cinRadio.isSelected()) {
                identityDocument.setType(DocumentType.CIN);
            } else if (passportRadio.isSelected()) {
                identityDocument.setType(DocumentType.PASSPORT);
            }

            // ========== MISE À JOUR DE L'OBJET PERDU ==========
            BaseObject Object = currentComplaint.getObject();
            String selectedType = objectTypeComboBox.getSelectionModel().getSelectedItem();

            switch (selectedType) {
                case "Électronique":
                    Object.setType(String.valueOf(TypeObjet.ELECTRONIQUE));
                    break;
                case "Documents":
                    Object.setType(String.valueOf(TypeObjet.DOCUMENTS));
                    break;
                case "Objets personnels":
                    Object.setType(String.valueOf(TypeObjet.OBJETS_PERSONNELS));
                    break;
                case "Vêtements":
                    Object.setType(String.valueOf(TypeObjet.VETEMENTS));
                    break;
                case "Sacs":
                    Object.setType(String.valueOf(TypeObjet.SACS));
                    break;
                case "Équipement sportif":
                    Object.setType(String.valueOf(TypeObjet.EQUIPEMENT_SPORTIF));
                    break;
                case "Accessoires":
                    Object.setType(String.valueOf(TypeObjet.ACCESSOIRES));
                    break;
                case "Divers":
                   Object.setType(String.valueOf(TypeObjet.DIVERS));
                    break;
                case "Autre / Inconnu":
                    Object.setType(String.valueOf(TypeObjet.AUTRE_OU_INCONNU));
                    break;
            }

            Object.setDescription(objectDescriptionArea.getText().trim());
            Object.setLostDate(lostDatePicker.getValue());

            // Récupérer le stade sélectionné
            String stadiumName = stadiumComboBox.getSelectionModel().getSelectedItem();
            Stadium selectedStadium = stadiums.stream()
                    .filter(s -> s.getStadiumName().equals(stadiumName))
                    .findFirst()
                    .orElse(null);

            if (selectedStadium != null) {
                Object.setZone(selectedStadium.getStadiumName());
            }

            // ========== MISE À JOUR DE LA PREUVE ==========
            Proof proof = currentComplaint.getProof();

            if (ticketRadio.isSelected()) {
                proof.setPresenceProofType(PresenceProofType.TICKET);
            } else if (badgeRadio.isSelected()) {
                proof.setPresenceProofType(PresenceProofType.BADGE);
            }

            // Mettre à jour l'image uniquement si une nouvelle image est sélectionnée
            if (selectedImageFile != null) {
                proof.setProofImage(ImageConverterUtil.convertImageToByte(selectedImageFile));
            }

            // ========== MISE À JOUR DU STATUT ==========
            String selectedStatus = complaintStatusComboBox.getSelectionModel().getSelectedItem();
            switch (selectedStatus) {
                case "Recherche en cours":
                    currentComplaint.setStatus(StatusComplaint.SEARCH_IN_PROGRESS);
                    break;
                case "Clôturée – objet non trouvé":
                    currentComplaint.setStatus(StatusComplaint.CLOSED_NOT_FOUND);
                    break;
                case "Objet retrouvé":
                    currentComplaint.setStatus(StatusComplaint.FOUND);
                    break;
            }

            // ========== ENREGISTREMENT DE LA MISE À JOUR ==========
            boolean isUpdated = complaintService.updateComplaint(currentComplaint);

            if (isUpdated) {
                Alerts.successAlert(
                        "Réclamation modifiée",
                        "Succès",
                        "La réclamation a été modifiée avec succès."
                );
                NavigationUtil.navigate(new ActionEvent(), "/fxml/ViewComplaintAgent.fxml");
            } else {
                Alerts.errorAlert(
                        "Erreur",
                        "Échec de la modification",
                        "La réclamation n'a pas pu être modifiée. Veuillez réessayer."
                );
            }

        } catch (InvalidEmailException e) {
            Alerts.errorAlert(
                    "Erreur de validation",
                    "Email invalide",
                    "Veuillez entrer un email valide."
            );
        } catch (InvalidPhoneException e) {
            Alerts.errorAlert(
                    "Erreur de validation",
                    "Numéro de téléphone invalide",
                    "Veuillez entrer un numéro de téléphone valide."
            );
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.errorAlert(
                    "Erreur",
                    "Erreur lors de la modification",
                    "Une erreur est survenue : " + e.getMessage()
            );
        }
    }

    /**
     * Retourne à la page précédente
     */
    @FXML
    public void goToDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/Agent.fxml");
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

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewComplaintAgent.fxml");
    }
}