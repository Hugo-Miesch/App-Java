package fr.esgi.pajavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.esgi.pajavafx.models.Ticket;
import fr.esgi.pajavafx.services.TicketService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class TicketListController implements Initializable {
    @FXML private TableView<Ticket> tableTickets;
    @FXML private TableColumn<Ticket, Integer> colId;
    @FXML private TableColumn<Ticket, Integer> colRobotId;
    @FXML private TableColumn<Ticket, Integer> colEntrepotId;
    @FXML private TableColumn<Ticket, String> colIssue;
    @FXML private TableColumn<Ticket, String> colPriority;
    @FXML private TableColumn<Ticket, String> colStatus;
    @FXML private TableColumn<Ticket, Timestamp> colCreatedAt;
    @FXML private TableColumn<Ticket, Timestamp> colResolvedAt;
    @FXML private TableColumn<Ticket, Integer> colTechnicienId;
    @FXML private TableColumn<Ticket, Void> colActions;

    private final TicketService ticketService = new TicketService();
    private final ObservableList<Ticket> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRobotId.setCellValueFactory(new PropertyValueFactory<>("robot_id"));
        colEntrepotId.setCellValueFactory(new PropertyValueFactory<>("entrepot_id"));
        colIssue.setCellValueFactory(new PropertyValueFactory<>("issue"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        colResolvedAt.setCellValueFactory(new PropertyValueFactory<>("resolved_at"));
        colTechnicienId.setCellValueFactory(new PropertyValueFactory<>("technicien_id"));
        loadData();
        setupActionButtons();
    }

    private void loadData() {
        List<Ticket> tickets = ticketService.fetchAll();
        data.setAll(tickets);
        tableTickets.setItems(data);
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Ticket, Void> call(final TableColumn<Ticket, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("Éditer");
                    private final Button btnDelete = new Button("Supprimer");
                    {
                        btnEdit.setOnAction(e -> showEditDialog(getTableView().getItems().get(getIndex())));
                        btnDelete.setOnAction(e -> {
                            Ticket t = getTableView().getItems().get(getIndex());
                            boolean ok = ticketService.delete(t.getId());
                            if(ok) data.remove(t);
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
    private void onAddTicket() { showEditDialog(null); }

    private void showEditDialog(Ticket ticket) {
        Dialog<Ticket> dialog = new Dialog<>();
        dialog.setTitle(ticket == null ? "Ajouter ticket" : "Modifier ticket");

        Label lblRobotId = new Label("Id robot:");
        TextField tfRobotId = new TextField(ticket != null ? String.valueOf(ticket.getRobot_id()) : "");
        Label lblEntrepotId = new Label("Id entrepot:");
        TextField tfEntrepotId = new TextField(ticket != null ? String.valueOf(ticket.getEntrepot_id()) : "");
        Label lblIssue = new Label("Issue:");
        TextField tfIssue = new TextField(ticket != null ? ticket.getIssue() : "");
        Label lblPriority = new Label("Priorité:");
        TextField tfPriority = new TextField(ticket != null ? ticket.getPriority() : "");
        Label lblStatus = new Label("Statut:");
        TextField tfStatus = new TextField(ticket != null ? ticket.getStatus() : "");
        Label lblTechnicien = new Label("Id technicien:");
        TextField tfTechnicien = new TextField(ticket != null ? String.valueOf(ticket.getTechnicien_id()) : "");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        grid.add(lblRobotId, 0, 0);      grid.add(tfRobotId, 1, 0);
        grid.add(lblEntrepotId, 0, 1);   grid.add(tfEntrepotId, 1, 1);
        grid.add(lblIssue, 0, 2);        grid.add(tfIssue, 1, 2);
        grid.add(lblPriority, 0, 3);     grid.add(tfPriority, 1, 3);
        grid.add(lblStatus, 0, 4);       grid.add(tfStatus, 1, 4);
        grid.add(lblTechnicien, 0, 5);   grid.add(tfTechnicien, 1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                try {
                    int robotId = Integer.parseInt(tfRobotId.getText());
                    int entrepotId = Integer.parseInt(tfEntrepotId.getText());
                    String issue = tfIssue.getText();
                    String priority = tfPriority.getText();
                    String status = tfStatus.getText();
                    int technicienId = Integer.parseInt(tfTechnicien.getText());
                    if (ticket == null)
                        return new Ticket(0, robotId, entrepotId, issue, priority, status, null, null, technicienId);
                    else {
                        ticket.setRobot_id(robotId);
                        ticket.setEntrepot_id(entrepotId);
                        ticket.setIssue(issue);
                        ticket.setPriority(priority);
                        ticket.setStatus(status);
                        ticket.setTechnicien_id(technicienId);
                        return ticket;
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
            if (ticket == null) {
                Ticket added = ticketService.add(result);
                if (added != null) data.add(added);
            } else {
                boolean ok = ticketService.update(result);
                if (ok) loadData();
            }
        });
    }
}
