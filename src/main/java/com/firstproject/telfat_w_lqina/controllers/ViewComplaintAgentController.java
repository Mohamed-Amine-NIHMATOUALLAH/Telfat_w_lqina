package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.Enum.StatusComplaint;
import com.firstproject.telfat_w_lqina.Enum.TypeObjet;
import com.firstproject.telfat_w_lqina.exception.NotFoundException.ObjectNotFoundException;
import com.firstproject.telfat_w_lqina.models.Agent;
import com.firstproject.telfat_w_lqina.models.Complaint;
import com.firstproject.telfat_w_lqina.models.Stadium;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.service.ComplaintService;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ViewComplaintAgentController {

    @FXML
    private TableView<Complaint> complaintTableView;
    @FXML
    private TableColumn<Complaint, String> nameComplaintTableColumn;
    @FXML
    private TableColumn<Complaint, String> stadeLosteObjectTableColumn;
    @FXML
    private TableColumn<Complaint, String> dateLosteObjectTableColumn;
    @FXML
    private TableColumn<Complaint, String> typeProofTableColumn;
    @FXML
    private TableColumn<Complaint, String> stateComplaintTableColumn;
    @FXML
    private TableColumn<Complaint, String> typeLosteObjectTableColumn;

    // Info Complaint selected
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label teleLabel;
    @FXML
    private Label documentTypeLabel;
    @FXML
    private Label documentNumberLabel;
    @FXML
    private Label typeObjetLabel;
    @FXML
    private Label lostDateLabel;
    @FXML
    private Label zoneLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label proofTypeLabel;
    @FXML
    private Label StatusComplaintLabel;
    @FXML
    private ImageView ImageProofImageView;

    @FXML
    private ComboBox<String> typeObjetComboBox;
    @FXML
    private ComboBox<String> stadiumComboBox;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private DatePicker dateFilterDatePicker;

    @FXML private Label nb;

    @FXML private Label labelUser;

    private ComplaintService complaintService = new ComplaintService();
    private ObservableList<Complaint> complaintObservableList;
    private List<Complaint> complaintList;
    private Complaint selectedComplaint;
    private List<Stadium> listStadium = StadiumService.getAllStadiums();
    private Agent currentUser = (Agent) SessionManager.getInstance().getCurrentUser();

    @FXML
    public void initialize() {

        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser2 = SessionManager.getInstance().getCurrentUser();
        labelUser.setText(currentUser2.getUsername());

        // Configuration des colonnes
        nameComplaintTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getPerson().getFirstName() + " " +
                                cellData.getValue().getPerson().getLastName()
                )
        );

        stadeLosteObjectTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getObject().getZone())
        );

        dateLosteObjectTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getObject().getLostDate() != null
                                ? cellData.getValue().getObject().getLostDate().toString()
                                : ""
                )
        );

        typeProofTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getProof() != null &&
                                cellData.getValue().getProof().getPresenceProofType() != null
                                ? cellData.getValue().getProof().getPresenceProofType().toString()
                                : ""
                )
        );

        stateComplaintTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        getStatusInFrench(cellData.getValue().getStatus())
                )
        );

        typeLosteObjectTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getObject() != null &&
                                cellData.getValue().getObject().getType() != null
                                ? cellData.getValue().getObject().getType().toString()
                                : ""
                )
        );

        // Charger les données initiales
        search();

        // Listener pour la sélection dans le tableau
        complaintTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayComplaintDetails(newValue);
                    }
                }
        );

        // Initialiser les ComboBox
        typeObjetComboBox.getItems().addAll(
                "Tous", "ELECTRONIQUE", "DOCUMENTS", "OBJETS PERSONNELS",
                "VETEMENTS", "SACS", "EQUIPEMENT SPORTIF", "ACCESSOIRES",
                "DIVERS", "AUTRE OU INCONNU"
        );
        typeObjetComboBox.setValue("Tous");

        stadiumComboBox.getItems().clear();
        stadiumComboBox.getItems().addAll(
                listStadium.stream().map(Stadium::getStadiumName).toList()
        );
        stadiumComboBox.getItems().add(0, "Tous");
        stadiumComboBox.setValue(currentUser.getStadium().getStadiumName());

        statutComboBox.getItems().clear();
        statutComboBox.getItems().addAll(
                "Tous", "Recherche en cours", "Objet retrouvé", "Objet non retrouvé"
        );
        statutComboBox.setValue("Recherche en cours");
        nb.setText(String.valueOf(complaintObservableList.size()));
    }

    private void displayComplaintDetails(Complaint complaint) {
        try {
            selectedComplaint = complaint;

            // Informations personnelles
            lastNameLabel.setText(complaint.getPerson().getLastName());
            firstNameLabel.setText(complaint.getPerson().getFirstName());
            emailLabel.setText(complaint.getPerson().getEmail());
            teleLabel.setText(complaint.getPerson().getTelephone());

            // Document d'identité
            documentTypeLabel.setText(
                    complaint.getPerson().getIdentityDocument().getType().toString()
            );
            documentNumberLabel.setText(
                    complaint.getPerson().getIdentityDocument().getDocumentNumber()
            );

            // Objet perdu
            typeObjetLabel.setText(complaint.getObject().getType().toString());
            lostDateLabel.setText(complaint.getObject().getLostDate().toString());
            zoneLabel.setText(complaint.getObject().getZone());
            descriptionLabel.setText(complaint.getObject().getDescription());

            // Preuve et statut
            proofTypeLabel.setText(
                    complaint.getProof().getPresenceProofType().toString()
            );
            StatusComplaintLabel.setText(getStatusInFrench(complaint.getStatus()));

            // Image de preuve
            if (complaint.getProof().getProofImage() != null) {
                ImageProofImageView.setImage(
                        ImageConverterUtil.convertByteToImage(
                                complaint.getProof().getProofImage()
                        )
                );
            } else {
                ImageProofImageView.setImage(null);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des détails: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void goTDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/Agent.fxml");
    }

    @FXML
    public void goToAddLostObject(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddLostObject.fxml");
    }


    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewLostObjectsAgent.fxml");
    }

    @FXML
    public void goToAddComplaint(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddComplaint.fxml");
    }

    @FXML
    public void seDeconnecter(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }

    public void removeComplaint(ActionEvent event) {
        if (selectedComplaint == null) {
            Alerts.errorAlert("Erreur", null, "Veuillez sélectionner une réclamation à supprimer.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer cette réclamation ?");
        confirmationAlert.setContentText("Cette action est irréversible.");
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                complaintService.removeComplaint(selectedComplaint.getId());
                complaintObservableList.remove(selectedComplaint);
                clearComplaintDetails();
                Alerts.successAlert("Succès", null, "La réclamation a été supprimée avec succès.");
            } catch (ObjectNotFoundException objectNotFoundException) {
                Alerts.errorAlert("Erreur", null, "La réclamation n'a pas été trouvée.");
            } catch (Exception e) {
                Alerts.errorAlert("Erreur", null, "Une erreur est survenue lors de la suppression: " + e.getMessage());
            }
        }
    }

    private void clearComplaintDetails() {
        lastNameLabel.setText("---");
        firstNameLabel.setText("---");
        emailLabel.setText("---");
        teleLabel.setText("---");
        documentTypeLabel.setText("---");
        documentNumberLabel.setText("---");
        typeObjetLabel.setText("---");
        lostDateLabel.setText("---");
        zoneLabel.setText("---");
        descriptionLabel.setText("---");
        proofTypeLabel.setText("---");
        StatusComplaintLabel.setText("---");
        ImageProofImageView.setImage(null);
        selectedComplaint = null;
    }

    @FXML
    public void refreshTableView(ActionEvent event) {
        search();
        clearComplaintDetails();
    }

    private void search() {
        complaintList = complaintService.getAllComplaints();

        // Filtre par date
        if (dateFilterDatePicker.getValue() != null) {
            String selectedDate = dateFilterDatePicker.getValue().toString();
            complaintList = complaintList.stream()
                    .filter(c -> c.getObject().getLostDate() != null &&
                            c.getObject().getLostDate().toString().equals(selectedDate))
                    .toList();
        }

        // Filtre par type d'objet
        if (typeObjetComboBox.getValue() != null &&
                !typeObjetComboBox.getValue().equals("Tous")) {
            TypeObjet typeEnum = mapStringToTypeObjet(typeObjetComboBox.getValue());
            if (typeEnum != null) {
                complaintList = complaintList.stream()
                        .filter(c -> c.getObject().getType() != null &&
                                c.getObject().getType().toString().equals(typeEnum.toString()))
                        .toList();
            }
        }

        // Filtre par stade
        if (stadiumComboBox.getValue() != null &&
                !stadiumComboBox.getValue().equals("Tous")) {
            String selectedStadium = stadiumComboBox.getValue();
            complaintList = complaintList.stream()
                    .filter(c -> c.getObject().getZone() != null &&
                            c.getObject().getZone().equals(selectedStadium))
                    .toList();
        }

        // Filtre par statut
        if (statutComboBox.getValue() != null &&
                !statutComboBox.getValue().equals("Tous")) {
            StatusComplaint statusEnum = mapStringToStatusComplaint(statutComboBox.getValue());
            if (statusEnum != null) {
                complaintList = complaintList.stream()
                        .filter(c -> c.getStatus() != null &&
                                c.getStatus().equals(statusEnum))
                        .toList();
            }
        }

        // Mettre à jour le TableView
        complaintObservableList = FXCollections.observableArrayList(complaintList);
        complaintTableView.setItems(complaintObservableList);
    }

    private TypeObjet mapStringToTypeObjet(String value) {
        return switch (value) {
            case "ELECTRONIQUE" -> TypeObjet.ELECTRONIQUE;
            case "DOCUMENTS" -> TypeObjet.DOCUMENTS;
            case "OBJETS PERSONNELS" -> TypeObjet.OBJETS_PERSONNELS;
            case "VETEMENTS" -> TypeObjet.VETEMENTS;
            case "SACS" -> TypeObjet.SACS;
            case "EQUIPEMENT SPORTIF" -> TypeObjet.EQUIPEMENT_SPORTIF;
            case "ACCESSOIRES" -> TypeObjet.ACCESSOIRES;
            case "DIVERS" -> TypeObjet.DIVERS;
            case "AUTRE OU INCONNU" -> TypeObjet.AUTRE_OU_INCONNU;
            default -> null;
        };
    }

    private StatusComplaint mapStringToStatusComplaint(String value) {
        return switch (value) {
            case "Recherche en cours" -> StatusComplaint.SEARCH_IN_PROGRESS;
            case "Objet retrouvé" -> StatusComplaint.FOUND;
            case "Objet non retrouvé" -> StatusComplaint.CLOSED_NOT_FOUND;
            default -> null;
        };
    }
    private String getStatusInFrench(StatusComplaint status) {
        if (status == null) {
            return "";
        }

        switch (status) {
            case SEARCH_IN_PROGRESS:
                return "Recherche en cours";
            case CLOSED_NOT_FOUND:
                return "Objet non retrouvé";
            case FOUND:
                return "Objet retrouvé";
            default:
                return status.toString();
        }
    }

    @FXML
    public void goToUpdateComplaint(ActionEvent event) throws IOException {
        if (selectedComplaint == null) {
            Alerts.errorAlert("Erreur", null, "Veuillez sélectionner une réclamation à modifier.");
            return;
        }

        // Sauvegarder la réclamation sélectionnée dans la session
        SessionComplaint.getInstance().setCurentComplaint(selectedComplaint);

        // Naviguer vers la page de mise à jour
        NavigationUtil.navigate(event, "/fxml/UpdateComplaint.fxml");
    }

    public void clearFilters(ActionEvent event) {
        dateFilterDatePicker.setValue(null);
        typeObjetComboBox.setValue("Tous");
        stadiumComboBox.setValue("Tous");
        statutComboBox.setValue("Tous");
        search();
    }
}