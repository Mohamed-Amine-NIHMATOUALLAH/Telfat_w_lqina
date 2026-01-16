package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.models.Stadium;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.List;

public class ViewStadiumsController {

    @FXML
    private FlowPane stadiumContainer;

    @FXML
    private VBox emptyState;

    @FXML
    private Label labelAdmin;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            labelAdmin.setText(currentUser.getUsername());
        }
        loadStadiums();
    }

    @FXML
    public void goToDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/Admin.fxml");
    }

    @FXML
    public void goToStatistique(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/AdminStatistics.fxml");
    }

    @FXML
    public void createUser(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddUsers.fxml");
    }

    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewLostObjectsAdmin.fxml");
    }

    @FXML
    public void addStadium(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddStadium.fxml");
    }

    @FXML
    public void goToStadiumList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewStadiums.fxml");
    }

    @FXML
    public void goToComplaintList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/ViewComplaintAdmin.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }

    private void loadStadiums() {
        List<Stadium> stadiums = StadiumService.getAllStadiums();

        if (stadiums.isEmpty()) {
            emptyState.setVisible(true);
            stadiumContainer.setVisible(false);
        } else {
            emptyState.setVisible(false);
            stadiumContainer.setVisible(true);
            stadiumContainer.getChildren().clear();

            for (Stadium stadium : stadiums) {
                stadiumContainer.getChildren().add(createCard(stadium));
            }
        }
    }

    private VBox createCard(Stadium stadium) {
        // ========== CONTENEUR PRINCIPAL - Design moderne conforme au guide ==========
        VBox card = new VBox(0);
        card.setPrefSize(240, 250);
        card.setMinSize(240, 250);
        card.setMaxSize(240, 250);
        card.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);");

        // ========== PARTIE SUPÉRIEURE - Fond VERT FONCÉ #2d6a2e avec icône ==========
        StackPane header = new StackPane();
        header.setPrefHeight(85);
        header.setMinHeight(85);
        header.setMaxHeight(85);
        header.setStyle("-fx-background-color: #006233; " +
                "-fx-background-radius: 11 11 0 0;");

        // Icône stade BLANCHE 40px centrée
        ImageView stadiumIcon = new ImageView();
        try {
            var imageStream = getClass().getResourceAsStream("/images/stade.png");
            if (imageStream != null) {
                Image stadiumImage = new Image(imageStream);
                stadiumIcon.setImage(stadiumImage);
                stadiumIcon.setFitWidth(40);
                stadiumIcon.setFitHeight(40);
                stadiumIcon.setPreserveRatio(true);
            } else {
                System.err.println("ATTENTION: Impossible de trouver l'image /images/stade.png");
                // Créer un label de secours avec emoji
                Label fallbackIcon = new Label("🏟️");
                fallbackIcon.setStyle("-fx-font-size: 40px; -fx-text-fill: WHITE;");
                StackPane.setAlignment(fallbackIcon, Pos.CENTER);
                header.getChildren().add(fallbackIcon);
                return card; // Retourner tôt pour éviter d'ajouter stadiumIcon
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image stade.png: " + e.getMessage());
            e.printStackTrace();
            // Créer un label de secours avec emoji
            Label fallbackIcon = new Label("🏟️");
            fallbackIcon.setStyle("-fx-font-size: 40px; -fx-text-fill: WHITE;");
            StackPane.setAlignment(fallbackIcon, Pos.CENTER);
            header.getChildren().add(fallbackIcon);
            return card; // Retourner tôt pour éviter d'ajouter stadiumIcon
        }
        StackPane.setAlignment(stadiumIcon, Pos.CENTER);
        header.getChildren().add(stadiumIcon);

        // ========== PARTIE CENTRALE - Fond BLANC avec toutes les infos ==========
        VBox body = new VBox(5);
        body.setPrefHeight(165);
        body.setMinHeight(165);
        body.setAlignment(Pos.TOP_CENTER);
        body.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 0 0 11 11; " +
                "-fx-padding: 12 15 12 15;");

        // Nom du stade - NOIR #212121, BOLD, 15px
        Label nameLabel = new Label(stadium.getStadiumName());
        nameLabel.setFont(Font.font("System Bold", 15));
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(210);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #212121;");

        // Badge Ville - Style capsule avec fond gris clair
        HBox cityBadge = new HBox(4);
        cityBadge.setAlignment(Pos.CENTER);
        cityBadge.setStyle("-fx-background-color: #f4f4f4; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 5 12 5 12;");
        cityBadge.setMaxWidth(Region.USE_PREF_SIZE);

        Label villePrefix = new Label("Ville:");
        villePrefix.setFont(Font.font("System Bold", 12));
        villePrefix.setStyle("-fx-font-weight: bold; -fx-text-fill: #db3a34;");

        Label cityValue = new Label(stadium.getCity() != null ? stadium.getCity() : "Non spécifiée");
        cityValue.setFont(Font.font("System", 12));
        cityValue.setStyle("-fx-text-fill: #424242;");

        cityBadge.getChildren().addAll(villePrefix, cityValue);

        // Conteneur centré pour le badge
        HBox cityContainer = new HBox();
        cityContainer.setAlignment(Pos.CENTER);
        cityContainer.getChildren().add(cityBadge);

        // Barre de progression horizontale ROUGE-VERT (90px + 90px, hauteur 3px)
        HBox progressBar = new HBox(0);
        progressBar.setAlignment(Pos.CENTER);
        progressBar.setPrefHeight(3);
        progressBar.setMaxWidth(180);

        javafx.scene.shape.Rectangle redBar = new javafx.scene.shape.Rectangle();
        redBar.setWidth(90);
        redBar.setHeight(3);
        redBar.setFill(Color.web("#d32f2f"));
        redBar.setArcWidth(3);
        redBar.setArcHeight(3);

        javafx.scene.shape.Rectangle greenBar = new javafx.scene.shape.Rectangle();
        greenBar.setWidth(90);
        greenBar.setHeight(3);
        greenBar.setFill(Color.web("#2d6a2e"));
        greenBar.setArcWidth(3);
        greenBar.setArcHeight(3);

        progressBar.getChildren().addAll(redBar, greenBar);

        // Marge de 10px au-dessus de la barre de progression
        VBox.setMargin(progressBar, new Insets(10, 0, 0, 0));

        // Bouton "Modifier" - Style CAPSULE rouge-orange #e53935, 170x38px
        Button detailsButton = new Button("Modifier");
        detailsButton.setPrefHeight(38);
        detailsButton.setPrefWidth(180);
        detailsButton.setMaxWidth(180);
        detailsButton.setStyle("-fx-background-color: #C1272D; " +
                "-fx-background-radius: 19; " +
                "-fx-text-fill: white; " +
                "-fx-cursor: hand; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px;");
        detailsButton.setFont(Font.font("System Bold", 13));

        // Ombre portée rouge subtile sur le bouton
        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setColor(Color.web("#e5393550"));
        buttonShadow.setOffsetY(3);
        buttonShadow.setRadius(6);
        detailsButton.setEffect(buttonShadow);

        // Action du bouton - navigation vers UpdateStadium
        detailsButton.setOnAction(event -> {
            try {
                StadiumService.setSelectedStadium(stadium);
                NavigationUtil.navigate(event, "/fxml/UpdateStadium.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Marge au-dessus du bouton
        VBox.setMargin(detailsButton, new Insets(10, 0, 0, 0));

        // Assembler le corps de la carte (ordre : nom, badge ville, barre, bouton - sans spacer!)
        body.getChildren().addAll(nameLabel, cityContainer, progressBar, detailsButton);

        // Assembler la carte complète (header vert + body blanc)
        card.getChildren().addAll(header, body);


        return card;
    }

}