package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.exception.validationexception.IncorrectPasswordException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidInputLoginException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidUsernameException;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.service.AuthService;
import com.firstproject.telfat_w_lqina.util.Alerts;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.firstproject.telfat_w_lqina.Enum.UserType.ADMIN;
import static com.firstproject.telfat_w_lqina.Enum.UserType.AGENT;

public class loginController {

    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordField;

    AuthService authService = new AuthService();


    public void login() throws IOException {
        try {
            authService.login(usernameTextField.getText(),passwordField.getText());
        }
        catch (InvalidInputLoginException invalidInputLoginException){
            Alerts.errorAlert("Login Failed" ,"Missing Credentials","Please enter both username and password !" );
            return;
        }
        catch (InvalidUsernameException invalidUsernameException){
            Alerts.errorAlert("Login Failed" ,"Invalid Username" ,"The username you entered does not exist !" );
            return;
        }
        catch (IncorrectPasswordException incorrectPasswordException){
            Alerts.errorAlert("Login Failed", "Incorrect Password", "The password you entered is incorrect !");
            return;
        }

        User user = authService.commonUser(usernameTextField.getText());

        // Stocker l'utilisateur dans la session
        SessionManager.getInstance().setCurrentUser(user);

        ActionEvent event = new ActionEvent(usernameTextField, usernameTextField);

        // Vérifier si c'est la première connexion d'un agent
        if (user.getUserType() == AGENT && user.isFirstLogin()) {
            showChangePasswordDialog();
            // Après le changement de mot de passe, rediriger vers la page agent
            goToAgentScene(event);
        } else if (user.getUserType() == AGENT) {
            goToAgentScene(event);
        } else if (user.getUserType() == ADMIN){
            goToAdminScene(event);
        }
    }

    private void showChangePasswordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChangePassword.fxml"));
            Parent root = loader.load();

            ChangePasswordController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Changement de mot de passe requis");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            controller.setDialogStage(dialogStage);

            // Centrer le popup sur l'écran
            dialogStage.centerOnScreen();

            // Afficher le popup et attendre qu'il soit fermé
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alerts.errorAlert("Erreur", "Erreur de chargement", "Impossible de charger le formulaire de changement de mot de passe");
        }

    }

    public void goToAdminScene(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/Admin.fxml");
    }

    public void goToAgentScene(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event,"/fxml/Agent.fxml");
    }
}
