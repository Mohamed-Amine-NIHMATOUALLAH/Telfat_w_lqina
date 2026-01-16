package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.service.UserService;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class ChangePasswordController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    private final UserService userService = new UserService();
    private Stage dialogStage;
    private boolean passwordChanged = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

    @FXML
    private void handleChangePassword() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("⚠️ Veuillez remplir tous les champs");
            return;
        }

        // Vérifier que le mot de passe actuel est correct
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (!BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
            showError("❌ Le mot de passe actuel est incorrect");
            currentPasswordField.clear();
            return;
        }

        // Vérifier que le nouveau mot de passe a au moins 6 caractères
        if (newPassword.length() < 6) {
            showError("⚠️ Le nouveau mot de passe doit contenir au moins 6 caractères");
            return;
        }

        // Vérifier que les deux nouveaux mots de passe correspondent
        if (!newPassword.equals(confirmPassword)) {
            showError("❌ Les nouveaux mots de passe ne correspondent pas");
            newPasswordField.clear();
            confirmPasswordField.clear();
            return;
        }

        // Vérifier que le nouveau mot de passe est différent de l'ancien
        if (newPassword.equals(currentPassword)) {
            showError("⚠️ Le nouveau mot de passe doit être différent de l'ancien");
            return;
        }

        try {
            // Hasher le nouveau mot de passe
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            // Mettre à jour le mot de passe
            currentUser.setPassword(hashedPassword);
            currentUser.setFirstLogin(false); // Marquer que ce n'est plus la première connexion

            // Sauvegarder dans la base de données
            userService.updateUser(currentUser);

            // Mettre à jour la session
            SessionManager.getInstance().setCurrentUser(currentUser);

            passwordChanged = true;
            showSuccess("✅ Mot de passe modifié avec succès !");

            // Fermer le popup après 1.5 secondes
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(() -> {
                        if (dialogStage != null) {
                            dialogStage.close();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            showError("❌ Erreur lors de la modification du mot de passe");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        // Ne pas permettre d'annuler lors de la première connexion
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser.isFirstLogin()) {
            showError("⚠️ Vous devez changer votre mot de passe avant de continuer");
            return;
        }

        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
    }

    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setVisible(true);
        errorLabel.setVisible(false);
    }
}

