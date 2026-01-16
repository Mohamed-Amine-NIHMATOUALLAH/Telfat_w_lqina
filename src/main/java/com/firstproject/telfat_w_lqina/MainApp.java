package com.firstproject.telfat_w_lqina;

import com.firstproject.telfat_w_lqina.service.UserService;
import com.firstproject.telfat_w_lqina.util.HibernateUtil;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    UserService userService = new UserService();

    public static void main(String[] args) {
        System.out.println(">>> MAIN STARTED"); // Log pour debug jpackage
        launch(args);
        System.out.println(">>> AFTER LAUNCH (normalement jamais atteint)");
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println(">>> INIT() STARTED - Initialisation de l'application...");

        try {
            // Force l'initialisation d'Hibernate (création des tables)
            HibernateUtil.getEntityManager().close();
            System.out.println(">>> Base de données initialisée avec succès !");
        } catch (Exception e) {
            System.err.println(">>> ERREUR lors de l'initialisation de la base de données !");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(">>> START() CALLED - Chargement de la vue login...");

        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/fxml/login.fxml")
        );
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        NavigationUtil.initialize(stage, scene);

        stage.setTitle("Telfat W lqine CAN 2025");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);

        System.out.println(">>> Initialisation du premier utilisateur admin...");
        userService.firstUserAdmin();

        System.out.println(">>> SHOW STAGE");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println(">>> STOP() CALLED - Fermeture de la connexion à la base de données...");
        HibernateUtil.shutdown();
    }
}
