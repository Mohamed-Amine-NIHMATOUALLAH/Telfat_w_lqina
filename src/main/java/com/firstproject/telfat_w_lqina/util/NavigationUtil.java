package com.firstproject.telfat_w_lqina.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for navigation between views.
 * Maintains the same Stage and Scene, only changes the root.
 */
public class NavigationUtil {

    private static Stage primaryStage;
    private static Scene primaryScene;

    private NavigationUtil() {}

    /**
     * Initialize the navigation system with the primary stage and scene.
     */
    public static void initialize(Stage stage, Scene scene) {
        primaryStage = stage;
        primaryScene = scene;
    }

    /**
     * Navigate to a new view by loading FXML and setting it as the scene root.
     * Does NOT create a new Scene - only changes the root content.
     */
    public static void navigate(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
        Parent root = loader.load();

        if (primaryScene != null) {
            // Just change the root, don't create new Scene
            primaryScene.setRoot(root);
        } else {
            // Fallback: get scene from event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        }
    }

    /**
     * Navigate to a new view without an ActionEvent.
     */
    public static void navigateTo(String fxmlPath) throws IOException {
        if (primaryScene == null) {
            throw new IllegalStateException("NavigationUtil not initialized. Call initialize() first.");
        }

        FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
        Parent root = loader.load();
        primaryScene.setRoot(root);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getPrimaryScene() {
        return primaryScene;
    }
}
