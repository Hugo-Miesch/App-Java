package fr.esgi.pajavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.esgi.pajavafx.models.Commande;
import fr.esgi.pajavafx.services.CommandeService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class CommandeListController implements Initializable {
    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colId;
    @FXML private TableColumn<Commande, Integer> colTicketId;
    @FXML private TableColumn<Commande, Integer> colTechnicienId;
    @FXML private TableColumn<Commande, String> colStatus;
    @FXML private TableColumn<Commande, Timestamp> colCreatedAt;
    @FXML private TableColumn<Commande, Void> colActions;

    private final CommandeService commandeService = new CommandeService();
    private final ObservableList<Commande> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTicketId.setCellValueFactory(new PropertyValueFactory<>("ticket_id"));
        colTechnicienId.setCellValueFactory(new PropertyValueFactory<>("technicien_id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        loadData();
        setupActionButtons();
    }

    private void loadData() {
        List<Commande> commandes = commandeService.fetchAll();
        data.setAll(commandes);
        tableCommandes.setItems(data);
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Commande, Void> call(final TableColumn<Commande, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("Éditer");
                    private final Button btnDelete = new Button("Supprimer");
                    {
                        btnEdit.setOnAction(e -> showEditDialog(getTableView().getItems().get(getIndex())));
                        btnDelete.setOnAction(e -> {
                            Commande commande = getTableView().getItems().get(getIndex());
                            boolean ok = commandeService.delete(commande.getId());
                            if(ok) data.remove(commande);
                            else new Alert(Alert.AlertType.ERROR, "Erreur de suppression !").showAndWait();
                        });
                    }
                    HBox box = new HBox(5, btnEdit, btnDelete);
                    @Override
                    public void updateItem(Void item, boolean empty) { setGraphic(empty ? null : box); }
                };
            }
        });
    }

    @FXML
    private void onAddCommande() { showEditDialog(null); }

    private void showEditDialog(Commande commande) {
        Dialog<Commande> dialog = new Dialog<>();
        dialog.setTitle(commande == null ? "Ajouter commande" : "Modifier commande");

        Label lblTicketId = new Label("ID ticket :");
        TextField tfTicketId = new TextField(commande != null ? String.valueOf(commande.getTicket_id()) : "");
        Label lblTechnicienId = new Label("ID technicien :");
        TextField tfTechnicienId = new TextField(commande != null ? String.valueOf(commande.getTechnicien_id()) : "");
        Label lblStatus = new Label("Statut :");
        TextField tfStatus = new TextField(commande != null ? commande.getStatus() : "");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(lblTicketId, 0, 0); grid.add(tfTicketId, 1, 0);
        grid.add(lblTechnicienId, 0, 1); grid.add(tfTechnicienId, 1, 1);
        grid.add(lblStatus, 0, 2); grid.add(tfStatus, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                try {
                    int ticketId = Integer.parseInt(tfTicketId.getText());
                    int technicienId = Integer.parseInt(tfTechnicienId.getText());
                    String status = tfStatus.getText();
                    // Pour l'ajout, created_at sera géré côté backend (typiquement "NOW()")
                    if (commande == null)
                        return new Commande(0, ticketId, technicienId, status, null);
                    else {
                        commande.setTicket_id(ticketId);
                        commande.setTechnicien_id(technicienId);
                        commande.setStatus(status);
                        // commande.setCreated_at(commande.getCreated_at()); // on ne touche pas à la date
                        return commande;
                    }
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Champs invalides !").showAndWait();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result == null) return;
            if (commande == null) {
                Commande added = commandeService.add(result);
                if (added != null) data.add(added);
            } else {
                boolean ok = commandeService.update(result);
                if (ok) loadData();
            }
        });
    }
}
