package com.firstproject.telfat_w_lqina.util;

import javafx.scene.control.Alert;

public class Alerts {

    public static void errorAlert(String title, String header, String content){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    public static void successAlert(String title,String header,String content){
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(title);
        successAlert.setHeaderText(header);
        successAlert.setContentText(content);
        successAlert.showAndWait();
    }

}
