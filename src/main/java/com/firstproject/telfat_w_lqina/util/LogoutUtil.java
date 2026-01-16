package com.firstproject.telfat_w_lqina.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class LogoutUtil {

    @FXML
    public static void logout(ActionEvent event) throws IOException {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Logout");
        confirmationAlert.setHeaderText("Logout Confirmation");
        confirmationAlert.setContentText("Are you sure you want to logout?");

        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionManager.getInstance().clearSession();
            NavigationUtil.navigateTo("/fxml/login.fxml");
        }
    }
}
