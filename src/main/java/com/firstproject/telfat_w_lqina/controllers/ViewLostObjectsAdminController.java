package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.Enum.TypeState;
import com.firstproject.telfat_w_lqina.Enum.UserType;
import com.firstproject.telfat_w_lqina.models.*;
import com.firstproject.telfat_w_lqina.service.LostObjectService;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.service.UserService;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionLostObject;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.firstproject.telfat_w_lqina.util.ImageConverterUtil.convertByteToImage;

public class ViewLostObjectsAdminController {


    @FXML
    private Label labelUser;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private VBox emptyStateContainer;

    @FXML
    private ComboBox<String> finedByComboBox;

    @FXML
    private DatePicker dateFilterDatePicker;
    @FXML
    private ComboBox<String> stadiumComboBox;
    @FXML
    private ComboBox<String> stateComboBox;



    private final LostObjectService lostObjectService = new LostObjectService();
    private List<LostObject> lostObjectsList;
    private List<Stadium> stadiums ;
    private  List<Agent> agents;
    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            labelUser.setText(currentUser.getUsername());
        }
        finedByComboBox.getItems().clear();
        finedByComboBox.getItems().add("Tout le mande");
        agents = userService.getAllUsers();
        for (Agent agent : agents){
            finedByComboBox.getItems().add(agent.getUsername());
        }
        // Set default selection
        finedByComboBox.getSelectionModel().selectFirst();

        stadiums = StadiumService.getAllStadiums();
        stadiumComboBox.getItems().clear();
        stadiumComboBox.getItems().add("Selectionner un Stade");
        for (Stadium stadium : stadiums) {
            stadiumComboBox.getItems().add(stadium.getStadiumName());
        }
        stadiumComboBox.getSelectionModel().selectFirst();

        stateComboBox.getItems().addAll("Tous","En Stockage","Rendu");
        stateComboBox.getSelectionModel().selectFirst();

        // Charger les données
        loadLostObjects();
    }

    private void loadLostObjects() {
        lostObjectsList = lostObjectService.getAllLostObjects();
        searsh();
        displayCards();
    }

    public void searsh() {
        if (finedByComboBox.getValue() != null && !finedByComboBox.getValue().equals("Tout le mande")) {
            lostObjectsList = lostObjectService.getLostObjectsByOwnerName(finedByComboBox.getValue(), lostObjectsList);
        }

        if (dateFilterDatePicker.getValue() != null) {
            lostObjectsList = lostObjectService.getLostObjectsByDate(dateFilterDatePicker.getValue(), lostObjectsList);
        }
        if (stadiumComboBox.getValue() !=null && !stadiumComboBox.getValue().equals("Selectionner un Stade")) {
            lostObjectsList = lostObjectService.getLostObjectsByStadium(stadiumComboBox.getValue(), lostObjectsList);
        }
        if (stateComboBox.getValue() !=null && !stateComboBox.getValue().equals("Tous")) {
            TypeState selectedState = stateComboBox.getValue().equals("En Stockage") ? TypeState.IN_STORAGE : TypeState.RETURNED;
            lostObjectsList = lostObjectService.getLostObjectsByState(selectedState, lostObjectsList);
        }
    }

    private void displayCards() {
        cardsContainer.getChildren().clear();

        if (lostObjectsList == null || lostObjectsList.isEmpty()) {
            emptyStateContainer.setVisible(true);
            emptyStateContainer.setManaged(true);
            cardsContainer.setVisible(false);

            return;
        }

        emptyStateContainer.setVisible(false);
        emptyStateContainer.setManaged(false);
        cardsContainer.setVisible(true);


        for (LostObject obj : lostObjectsList) {
            VBox card = createCard(obj);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createCard(LostObject lostObject) {
        VBox card = new VBox();
        card.setPrefWidth(280);
        card.setSpacing(0);
        card.setStyle("""
        -fx-background-color: #ffffff;
        -fx-background-radius: 16;
        -fx-border-color: #dee2e6;
        -fx-border-width: 1.2;
        -fx-border-radius: 16;
    """);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#00000026"));
        shadow.setRadius(12);
        shadow.setOffsetY(4);
        card.setEffect(shadow);

        // Zone image
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(180);
        imageContainer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 16 16 0 0;");
        imageContainer.setAlignment(Pos.CENTER);

        if (lostObject.getImage() != null && lostObject.getImage().length > 0) {
            try {
                ImageView imageView = new ImageView(convertByteToImage(lostObject.getImage()));
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                // largeur max sans déformation
                imageView.setFitWidth(260);
                imageView.setFitHeight(180);

                // coins arrondis en haut
                Rectangle clip = new Rectangle(260, 180);
                clip.setArcWidth(16);
                clip.setArcHeight(16);
                imageView.setClip(clip);

                imageContainer.getChildren().add(imageView);
            } catch (Exception e) {
                Label placeholder = new Label("📦");
                placeholder.setStyle("-fx-font-size: 48; -fx-text-fill: #dee2e6;");
                imageContainer.getChildren().add(placeholder);
            }
        } else {
            Label placeholder = new Label("📦");
            placeholder.setStyle("-fx-font-size: 48; -fx-text-fill: #dee2e6;");
            imageContainer.getChildren().add(placeholder);
        }

        // Zone contenu
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        // Type
        String typeText = (lostObject.getType() != null && !lostObject.getType().isEmpty()) ? lostObject.getType() : "Non spécifié";
        Label typeLabel = new Label(typeText);
        typeLabel.setStyle("""
        -fx-font-family: 'Segoe UI Semibold';
        -fx-font-size: 17px;
        -fx-text-fill: #006233;
    """);
        typeLabel.setMaxWidth(240);

        // Description
        Label descLabel = new Label(lostObject.getDescription());
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(240);
        descLabel.setStyle("""
        -fx-font-family: 'Segoe UI';
        -fx-font-size: 13px;
        -fx-text-fill: #495057;
    """);

        // Informations
        VBox infoBox = new VBox(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = lostObject.getLostDate().format(formatter);

        HBox dateBox = createInfoRow("📅", formattedDate);
        HBox zoneBox = createInfoRow("📍", lostObject.getZone());
        HBox agentBox = createInfoRow("👤", lostObject.getAgentName());
        infoBox.getChildren().addAll(dateBox, zoneBox, agentBox);

        // Séparateur
        Line separator = new Line(0, 0, 240, 0);
        separator.setStroke(Color.web("#ced4da"));
        separator.setStrokeWidth(0.8);
        separator.setOpacity(0.6);

            // Boutons
            HBox buttonsBox = new HBox(10);
            buttonsBox.setAlignment(Pos.CENTER);

            Button updateBtn = new Button("Modifier");
            updateBtn.setStyle("""
                           -fx-background-color: #006233;
                           -fx-text-fill: white;
                           -fx-font-size: 12px;
                           -fx-font-weight: bold;
                           -fx-background-radius: 8;
                           -fx-padding: 8 20;
                           -fx-cursor: hand;
                       """);
            updateBtn.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(updateBtn, Priority.ALWAYS);
            updateBtn.setOnAction(e -> {
                try {
                    updateObject(new ActionEvent(),lostObject);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Button deleteBtn = new Button("Supprimer");
            deleteBtn.setStyle("""
                           -fx-background-color: #dc3545;
                           -fx-text-fill: white;
                           -fx-font-size: 12px;
                           -fx-font-weight: bold;
                           -fx-background-radius: 8;
                           -fx-padding: 8 20;
                           -fx-cursor: hand;
                       """);
            deleteBtn.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(deleteBtn, Priority.ALWAYS);
            deleteBtn.setOnAction(e -> removeObject(lostObject));

            buttonsBox.getChildren().addAll(updateBtn, deleteBtn);

            content.getChildren().addAll(typeLabel, descLabel, infoBox, separator, buttonsBox);

        card.getChildren().addAll(imageContainer, content);

        return card;
    }

    private HBox createInfoRow(String icon, String text) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d;");

        Label textLabel = new Label(text != null ? text : "N/A");
        textLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #343a40;");

        row.getChildren().addAll(iconLabel, textLabel);
        return row;
    }

    @FXML
    public void refreshCards() {
        loadLostObjects();
    }

    public void removeObject(LostObject selectedObject) {
        if (selectedObject != null) {
            Alert confirmationRemove = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationRemove.setTitle("Confirmation de suppression");
            confirmationRemove.setHeaderText(null);
            confirmationRemove.setContentText("Êtes-vous sûr de vouloir supprimer cet objet perdu ?");

            Optional<ButtonType> result = confirmationRemove.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                lostObjectService.removeLostObject(selectedObject);
                loadLostObjects();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Objet supprimé avec succès!");
                successAlert.showAndWait();
            }
        }
    }

    public void updateObject(ActionEvent actionEvent ,LostObject lostObject) throws IOException {
        SessionLostObject.getInstance().setCurentLostObject(lostObject);
        NavigationUtil.navigate(actionEvent, "/fxml/UpdateLostObjectAdmin.fxml");
    }


    @FXML
    public void goToDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/Admin.fxml");
    }

    @FXML
    public void createUser(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AddUsers.fxml");
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

    public void clearDateFilter(ActionEvent event) {
        dateFilterDatePicker.setValue(null);
    }

    public void clearAllFilters(ActionEvent event) {
        finedByComboBox.getSelectionModel().selectFirst();
        dateFilterDatePicker.setValue(null);
        stadiumComboBox.getSelectionModel().selectFirst();
        stateComboBox.getSelectionModel().selectFirst();
        loadLostObjects();
    }
}

