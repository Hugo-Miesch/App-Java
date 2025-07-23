package fr.esgi.pajavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;

import java.io.IOException;

public class HomeController {
    @FXML
    private BorderPane root;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button btnClient, btnTechnicien, btnEntrepot, btnRobot, btnTicket, btnCommande;
    @FXML
    private Label displayLabel;

    @FXML
    public void initialize() {
    }

    @FXML
    private void goClients() {
        loadTab("Clients", "/fr/esgi/pajavafx/client_list.fxml");
    }

    @FXML
    private void goTechniciens() {
        loadTab("Techniciens", "/fr/esgi/pajavafx/technicien_list.fxml");
    }

    @FXML
    private void goSuperAdmins() {
        loadTab("Super Admins", "/fr/esgi/pajavafx/superadmin_list.fxml");
    }

    @FXML
    private void goEntrepots() {
        loadTab("Entrepôts", "/fr/esgi/pajavafx/entrepot_list.fxml");
    }

    @FXML
    private void goRobots() {
        loadTab("Robots", "/fr/esgi/pajavafx/robot_list.fxml");
    }

    @FXML
    private void goTickets() {
        loadTab("Tickets", "/fr/esgi/pajavafx/ticket_list.fxml");
    }

    @FXML
    private void goCommandes() {
        loadTab("Commandes", "/fr/esgi/pajavafx/commande_list.fxml");
    }

    private void loadTab(String tabTitle, String fxmlPath) {
        for (Tab t : tabPane.getTabs()) {
            if (t.getText().equals(tabTitle)) {
                tabPane.getSelectionModel().select(t);
                return;
            }
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node content = loader.load();
            Tab tab = new Tab(tabTitle, content);
            tab.setClosable(true);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            displayLabel.setText("");
        } catch (IOException e) {
            displayLabel.setText("Erreur de chargement (" + tabTitle + ")");
            e.printStackTrace();
        }
    }
}
