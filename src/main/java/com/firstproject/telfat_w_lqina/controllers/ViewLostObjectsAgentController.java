package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.Enum.*;
import com.firstproject.telfat_w_lqina.models.LostObject;
import com.firstproject.telfat_w_lqina.models.Stadium;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.models.Person;
import com.firstproject.telfat_w_lqina.models.Proof;
import com.firstproject.telfat_w_lqina.models.IdentityDocument;
import com.firstproject.telfat_w_lqina.service.LostObjectService;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionLostObject;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import com.firstproject.telfat_w_lqina.util.ImageConverterUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.firstproject.telfat_w_lqina.util.ImageConverterUtil.convertByteToImage;

public class ViewLostObjectsAgentController {

    @FXML
    private Label labelUser;

    @FXML
    private Label totalLabel;

    @FXML
    private Label userTypeLabel;

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

    @FXML
    private ComboBox<String> typeComboBox;

    private final LostObjectService lostObjectService = new LostObjectService();
    private List<LostObject> lostObjectsList;
    private List<Stadium> stadiums;

    @FXML
    public void initialize() {

        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        labelUser.setText(currentUser.getUsername());

        finedByComboBox.setItems(FXCollections.observableArrayList("Tout le monde", "moi-même"));
        finedByComboBox.getSelectionModel().selectFirst();

        stadiums = StadiumService.getAllStadiums();
        stadiumComboBox.getItems().clear();
        stadiumComboBox.getItems().add("Sélectionner un Stade");
        for (Stadium stadium : stadiums) {
            stadiumComboBox.getItems().add(stadium.getStadiumName());
        }
        stadiumComboBox.getSelectionModel().selectFirst();

        stateComboBox.getItems().addAll("Tous", "En Stockage", "Rendu");
        stateComboBox.getSelectionModel().selectFirst();

        loadLostObjects();
    }

    private void loadLostObjects() {
        lostObjectsList = lostObjectService.getAllLostObjects();
        searsh();
        displayCards();
    }

    public void searsh() {
        if (finedByComboBox.getValue() != null && finedByComboBox.getValue().equals("moi-même")) {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            lostObjectsList = lostObjectService.getLostObjectsByOwnerName(currentUser.getUsername(), lostObjectsList);
        }

        if (dateFilterDatePicker.getValue() != null) {
            lostObjectsList = lostObjectService.getLostObjectsByDate(dateFilterDatePicker.getValue(), lostObjectsList);
        }

        if (stadiumComboBox.getValue() != null && !stadiumComboBox.getValue().equals("Sélectionner un Stade")) {
            lostObjectsList = lostObjectService.getLostObjectsByStadium(stadiumComboBox.getValue(), lostObjectsList);
        }

        if (stateComboBox.getValue() != null && !stateComboBox.getValue().equals("Tous")) {
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
            totalLabel.setText("Total: 0 objets");
            return;
        }

        emptyStateContainer.setVisible(false);
        emptyStateContainer.setManaged(false);
        cardsContainer.setVisible(true);
        totalLabel.setText("Total: " + lostObjectsList.size() + " objets");

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

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(180);
        imageContainer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 16 16 0 0;");
        imageContainer.setAlignment(Pos.CENTER);

        if (lostObject.getImage() != null && lostObject.getImage().length > 0) {
            try {
                ImageView imageView = new ImageView(convertByteToImage(lostObject.getImage()));
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setFitWidth(260);
                imageView.setFitHeight(180);

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

        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        String typeText = (lostObject.getType() != null && !lostObject.getType().isEmpty()) ? lostObject.getType() : "Non spécifié";
        Label typeLabel = new Label(typeText);
        typeLabel.setStyle("""
        -fx-font-family: 'Segoe UI Semibold';
        -fx-font-size: 17px;
        -fx-text-fill: #006233;
    """);
        typeLabel.setMaxWidth(240);

        Label descLabel = new Label(lostObject.getDescription());
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(240);
        descLabel.setStyle("""
        -fx-font-family: 'Segoe UI';
        -fx-font-size: 13px;
        -fx-text-fill: #495057;
    """);

        VBox infoBox = new VBox(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = lostObject.getLostDate().format(formatter);

        HBox dateBox = createInfoRow("📅", formattedDate);
        HBox zoneBox = createInfoRow("📍", lostObject.getZone());
        HBox agentBox = createInfoRow("👤", lostObject.getAgentName());
        infoBox.getChildren().addAll(dateBox, zoneBox, agentBox);

        Line separator = new Line(0, 0, 240, 0);
        separator.setStroke(Color.web("#ced4da"));
        separator.setStrokeWidth(0.8);
        separator.setOpacity(0.6);

        if (lostObject.getAgentName().equals(SessionManager.getInstance().getCurrentUser().getUsername())) {
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
                    updateObject(new ActionEvent(), lostObject);
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

            if (lostObject.getTypeState() == TypeState.IN_STORAGE) {
                Button markFoundBtn = new Button("✅ Marquer trouvé");
                markFoundBtn.setStyle("""
                        -fx-background-color: linear-gradient(to right, #006233, #007d42);
                        -fx-text-fill: white;
                        -fx-font-size: 11px;
                        -fx-font-weight: bold;
                        -fx-background-radius: 8;
                        -fx-padding: 8 15;
                        -fx-cursor: hand;
                    """);
                markFoundBtn.setMaxWidth(Double.MAX_VALUE);
                markFoundBtn.setOnAction(e -> showMarkAsFoundPopup(lostObject));

                VBox allButtons = new VBox(8);
                allButtons.getChildren().addAll(buttonsBox, markFoundBtn);
                content.getChildren().addAll(typeLabel, descLabel, infoBox, separator, allButtons);
            } else {
                Label returnedBadge = new Label("✅ RENDU");
                returnedBadge.setStyle("""
                        -fx-background-color: #d4edda;
                        -fx-text-fill: #155724;
                        -fx-font-size: 11px;
                        -fx-font-weight: bold;
                        -fx-padding: 5 15;
                        -fx-background-radius: 15;
                    """);
                returnedBadge.setAlignment(Pos.CENTER);

                VBox allButtons = new VBox(8);
                allButtons.setAlignment(Pos.CENTER);
                allButtons.getChildren().addAll(returnedBadge, buttonsBox);
                content.getChildren().addAll(typeLabel, descLabel, infoBox, separator, allButtons);
            }
        } else {
            if (lostObject.getTypeState() == TypeState.RETURNED) {
                Label returnedBadge = new Label("✅ RENDU");
                returnedBadge.setStyle("""
                    -fx-background-color: #d4edda;
                    -fx-text-fill: #155724;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    -fx-padding: 5 15;
                    -fx-background-radius: 15;
                """);
                content.getChildren().addAll(typeLabel, descLabel, infoBox, separator, returnedBadge);
            } else {
                Label storageBadge = new Label("📦 EN STOCKAGE");
                storageBadge.setStyle("""
                    -fx-background-color: #fff3cd;
                    -fx-text-fill: #856404;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    -fx-padding: 5 15;
                    -fx-background-radius: 15;
                """);
                content.getChildren().addAll(typeLabel, descLabel, infoBox, separator, storageBadge);
            }
        }

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

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Objet supprimé avec succès!");
                successAlert.showAndWait();
            }
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
    public void updateObject(ActionEvent actionEvent, LostObject lostObject) throws IOException {
        SessionLostObject.getInstance().setCurentLostObject(lostObject);
        NavigationUtil.navigate(actionEvent, "/fxml/UpdateLostObjectAgent.fxml");
    }

    @FXML
    public void addNewObject(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddLostObject.fxml");
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
    public void seDeconnecter(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }

    private void showMarkAsFoundPopup(LostObject lostObject) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.setTitle("Marquer comme trouvé");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);
        """);
        mainContainer.setPrefWidth(550);
        mainContainer.setMaxWidth(550);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 25, 20, 25));
        header.setStyle("-fx-background-color: linear-gradient(to right, #006233, #007d42); -fx-background-radius: 16 16 0 0;");

        Label titleLabel = new Label("✅ Marquer l'objet comme trouvé");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> popupStage.close());

        header.getChildren().addAll(titleLabel, spacer, closeBtn);

        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: #fafafa;");

        HBox objectInfo = new HBox(15);
        objectInfo.setAlignment(Pos.CENTER_LEFT);
        objectInfo.setPadding(new Insets(15));
        objectInfo.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 10;");

        Label objectIcon = new Label("📦");
        objectIcon.setStyle("-fx-font-size: 24px;");

        VBox objectDetails = new VBox(3);
        Label objectType = new Label(lostObject.getType());
        objectType.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #006233;");
        Label objectDesc = new Label(lostObject.getDescription());
        objectDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        objectDesc.setWrapText(true);
        objectDetails.getChildren().addAll(objectType, objectDesc);

        objectInfo.getChildren().addAll(objectIcon, objectDetails);

        Label ownerSectionLabel = new Label("📋 INFORMATIONS DU PROPRIÉTAIRE");
        ownerSectionLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #006233;");

        GridPane ownerGrid = new GridPane();
        ownerGrid.setHgap(15);
        ownerGrid.setVgap(12);
        ownerGrid.setPadding(new Insets(10, 0, 0, 0));

        TextField firstNameField = createStyledTextField("Prénom");
        TextField lastNameField = createStyledTextField("Nom");
        TextField emailField = createStyledTextField("Email");
        TextField phoneField = createStyledTextField("Téléphone (+212...)");

        ownerGrid.add(createFieldWithLabel("Prénom *", firstNameField), 0, 0);
        ownerGrid.add(createFieldWithLabel("Nom *", lastNameField), 1, 0);
        ownerGrid.add(createFieldWithLabel("Email *", emailField), 0, 1);
        ownerGrid.add(createFieldWithLabel("Téléphone *", phoneField), 1, 1);

        Label docSectionLabel = new Label("🪪 DOCUMENT D'IDENTITÉ");
        docSectionLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #006233;");
        VBox.setMargin(docSectionLabel, new Insets(10, 0, 0, 0));

        GridPane docGrid = new GridPane();
        docGrid.setHgap(15);
        docGrid.setVgap(12);
        docGrid.setPadding(new Insets(10, 0, 0, 0));

        ComboBox<DocumentType> docTypeCombo = new ComboBox<>();
        docTypeCombo.getItems().addAll(DocumentType.values());
        docTypeCombo.setPromptText("Type de document");
        docTypeCombo.setStyle("-fx-pref-width: 200; -fx-pref-height: 40; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ddd;");
        docTypeCombo.getSelectionModel().selectFirst();

        TextField docNumberField = createStyledTextField("Numéro du document");

        docGrid.add(createFieldWithLabel("Type *", docTypeCombo), 0, 0);
        docGrid.add(createFieldWithLabel("Numéro *", docNumberField), 1, 0);

        Label proofSectionLabel = new Label("🎫 PREUVE DE PRÉSENCE AU STADE");
        proofSectionLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #006233;");
        VBox.setMargin(proofSectionLabel, new Insets(10, 0, 0, 0));

        GridPane proofGrid = new GridPane();
        proofGrid.setHgap(15);
        proofGrid.setVgap(12);
        proofGrid.setPadding(new Insets(10, 0, 0, 0));

        ComboBox<PresenceProofType> proofTypeCombo = new ComboBox<>();
        proofTypeCombo.getItems().addAll(PresenceProofType.values());
        proofTypeCombo.setPromptText("Type de preuve");
        proofTypeCombo.setStyle("-fx-pref-width: 200; -fx-pref-height: 40; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ddd;");
        proofTypeCombo.getSelectionModel().selectFirst();

        final File[] selectedProofImage = {null};
        ImageView proofImageView = new ImageView();
        proofImageView.setFitWidth(80);
        proofImageView.setFitHeight(80);
        proofImageView.setPreserveRatio(true);

        Button chooseImageBtn = new Button("📷 Choisir image");
        chooseImageBtn.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-text-fill: #333;
            -fx-font-size: 12px;
            -fx-background-radius: 8;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
        """);

        Label imageStatusLabel = new Label("Aucune image sélectionnée");
        imageStatusLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 11px;");

        chooseImageBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner l'image de preuve");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(popupStage);
            if (file != null) {
                selectedProofImage[0] = file;
                try {
                    Image image = new Image(file.toURI().toString());
                    proofImageView.setImage(image);
                    imageStatusLabel.setText("✅ " + file.getName());
                    imageStatusLabel.setStyle("-fx-text-fill: #006233; -fx-font-size: 11px;");
                } catch (Exception ex) {
                    imageStatusLabel.setText("❌ Erreur de chargement");
                    imageStatusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 11px;");
                }
            }
        });

        HBox imageBox = new HBox(10);
        imageBox.setAlignment(Pos.CENTER_LEFT);
        imageBox.getChildren().addAll(chooseImageBtn, proofImageView, imageStatusLabel);

        proofGrid.add(createFieldWithLabel("Type *", proofTypeCombo), 0, 0);
        proofGrid.add(createFieldWithLabel("Image preuve *", imageBox), 0, 1, 2, 1);

        content.getChildren().addAll(
                objectInfo,
                ownerSectionLabel, ownerGrid,
                docSectionLabel, docGrid,
                proofSectionLabel, proofGrid
        );

        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(20, 25, 20, 25));
        footer.setStyle("-fx-background-color: white; -fx-border-color: #eee; -fx-border-width: 1 0 0 0;");

        Button cancelBtn = new Button("Annuler");
        cancelBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #666;
            -fx-font-size: 14px;
            -fx-padding: 12 30;
            -fx-cursor: hand;
            -fx-border-color: #ddd;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
        """);
        cancelBtn.setOnAction(e -> popupStage.close());

        Button confirmBtn = new Button("✅ Confirmer la remise");
        confirmBtn.setStyle("""
            -fx-background-color: linear-gradient(to right, #006233, #007d42);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 12 30;
            -fx-cursor: hand;
            -fx-background-radius: 8;
        """);

        confirmBtn.setOnAction(e -> {
            IdentityDocument identityDocument = new IdentityDocument();
            identityDocument.setType(docTypeCombo.getValue());
            identityDocument.setDocumentNumber(docNumberField.getText());

            Person owner = new Person();
            owner.setFirstName(firstNameField.getText());
            owner.setLastName(lastNameField.getText());
            owner.setEmail(emailField.getText());
            owner.setTelephone(phoneField.getText());
            owner.setIdentityDocument(identityDocument);

            Proof proof = new Proof();
            proof.setPresenceProofType(proofTypeCombo.getValue());

            if (selectedProofImage[0] != null) {
                try {
                    byte[] imageBytes = ImageConverterUtil.convertImageToByte(selectedProofImage[0]);
                    proof.setProofImage(imageBytes);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            boolean success = lostObjectService.markAsReturned(lostObject, owner, proof);

            if (success) {
                popupStage.close();
                loadLostObjects();
            }
        });

        footer.getChildren().addAll(cancelBtn, confirmBtn);

        mainContainer.getChildren().addAll(header, content, footer);

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setMaxHeight(650);

        Scene scene = new Scene(scrollPane);
        scene.setFill(Color.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.initStyle(StageStyle.TRANSPARENT);

        popupStage.centerOnScreen();
        popupStage.showAndWait();
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("""
            -fx-pref-width: 220;
            -fx-pref-height: 40;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-color: #ddd;
            -fx-padding: 8 12;
            -fx-font-size: 13px;
        """);
        return field;
    }

    private VBox createFieldWithLabel(String labelText, javafx.scene.Node field) {
        VBox container = new VBox(5);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        container.getChildren().addAll(label, field);
        return container;
    }

    public void clearDateFilter(ActionEvent event) {
        dateFilterDatePicker.setValue(null);
        loadLostObjects();
    }

    public void clearAllFilters(ActionEvent event) {
        finedByComboBox.getSelectionModel().selectFirst();
        dateFilterDatePicker.setValue(null);
        stadiumComboBox.getSelectionModel().selectFirst();
        stateComboBox.getSelectionModel().selectFirst();
        typeComboBox.getSelectionModel().selectFirst();
        loadLostObjects();
    }
}