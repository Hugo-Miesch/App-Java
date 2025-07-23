package fr.esgi.pajavafx.controllers;

import fr.esgi.pajavafx.ApiClient;
import fr.esgi.pajavafx.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginButton.setOnAction(event -> loginSuperAdmin());
    }

    private void loginSuperAdmin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation des champs
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs");
            return;
        }

        ApiClient.loginSuperAdmin(email, password, responseBody -> {
            Platform.runLater(() -> {
                System.out.println("Response Body: " + responseBody.trim());

                if (responseBody.equals("Connexion réussie !")) {
                    System.out.println("Connexion réussie pour " + email);
                    // CHANGEMENT ICI : Utilise la classe Main pour l'ouverture du Home.class
                    try {
                        Main.showHomeView();
                    } catch (Exception e) {
                        System.out.println("Erreur lors du chargement de la page Home : " + e.getMessage());
                    }
                } else {
                    System.out.println("Erreur lors de la connexion : " + responseBody);
                }
            });
        });
    }
}
