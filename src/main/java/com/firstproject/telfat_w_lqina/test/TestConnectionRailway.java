package com.firstproject.telfat_w_lqina.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionRailway {
    public static void main(String[] args) {
        // Infos de connexion Railway
        String url = "jdbc:mysql://yamanote.proxy.rlwy.net:37128/railway?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "DJqQSiBNYcuQuOKYOWecHrFPqLcEXgXL";

        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("✅ Connexion réussie à la base Railway !");
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver MySQL introuvable !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion :");
            e.printStackTrace();
        }
    }
}
