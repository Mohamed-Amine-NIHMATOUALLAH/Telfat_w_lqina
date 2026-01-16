package com.firstproject.telfat_w_lqina.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HibernateUtil {
    private static final EntityManagerFactory emFactory;

    static {
        try {
            // Charger le fichier database.properties
            Properties props = new Properties();
            try (InputStream input = HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("database.properties")) {
                if (input == null) {
                    throw new IOException("Fichier database.properties introuvable");
                }
                props.load(input);
                System.out.println("📄 Propriétés chargées depuis database.properties");
            }

            // Mapper les propriétés vers les clés JPA standard
            Map<String, String> configOverrides = new HashMap<>();
            configOverrides.put("jakarta.persistence.jdbc.driver", props.getProperty("db.driver"));
            configOverrides.put("jakarta.persistence.jdbc.url", props.getProperty("db.url"));
            configOverrides.put("jakarta.persistence.jdbc.user", props.getProperty("db.username"));
            configOverrides.put("jakarta.persistence.jdbc.password", props.getProperty("db.password"));
            configOverrides.put("hibernate.dialect", props.getProperty("hibernate.dialect"));
            configOverrides.put("hibernate.hbm2ddl.auto", props.getProperty("hibernate.hbm2ddl.auto"));
            configOverrides.put("hibernate.show_sql", props.getProperty("hibernate.show_sql"));
            configOverrides.put("hibernate.format_sql", props.getProperty("hibernate.format_sql"));

            System.out.println("🔧 Configuration Hibernate:");
            System.out.println("   URL: " + configOverrides.get("jakarta.persistence.jdbc.url"));
            System.out.println("   User: " + configOverrides.get("jakarta.persistence.jdbc.user"));

            // Créer l'EntityManagerFactory avec les propriétés
            emFactory = Persistence.createEntityManagerFactory("TelfatPU", configOverrides);
            System.out.println("✅ Hibernate initialisé avec succès !");
        } catch (Throwable ex) {
            System.err.println("❌ Erreur lors de l'initialisation d'Hibernate: " + ex.getMessage());
            System.err.println();
            System.err.println("📋 Détails complets de l'erreur:");

            // Afficher la cause racine
            Throwable cause = ex;
            int depth = 0;
            while (cause != null && depth < 10) {
                System.err.println("  [" + depth + "] " + cause.getClass().getSimpleName() + ": " + cause.getMessage());
                cause = cause.getCause();
                depth++;
            }

            System.err.println();
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    public static void shutdown() {
        if (emFactory != null) {
            emFactory.close();
        }
    }
}
