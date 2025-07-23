package fr.esgi.pajavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.esgi.pajavafx.models.Technicien;
import fr.esgi.pajavafx.services.TechnicienService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TechnicienListController implements Initializable {
    @FXML private TableView<Technicien> tableTechniciens;
    @FXML private TableColumn<Technicien, Integer> colId;
    @FXML private TableColumn<Technicien, String> colEmail;
    @FXML private TableColumn<Technicien, Void> colActions;

    private final TechnicienService technicienService = new TechnicienService();
    private final ObservableList<Technicien> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadData();
        setupActionButtons();
    }

    private void loadData() {
        List<Technicien> techniciens = technicienService.fetchAll();
        data.setAll(techniciens);
        tableTechniciens.setItems(data);
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Technicien, Void> call(final TableColumn<Technicien, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("Éditer");
                    private final Button btnDelete = new Button("Supprimer");

                    {   // Init block
                        btnEdit.setOnAction(e -> showEditDialog(getTableView().getItems().get(getIndex())));
                        btnDelete.setOnAction(e -> {
                            Technicien t = getTableView().getItems().get(getIndex());
                            boolean ok = technicienService.delete(t.getId());
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
    private void onAddTechnicien() { showEditDialog(null); }

    private void showEditDialog(Technicien technicien) {
        Dialog<Technicien> dialog = new Dialog<>();
        dialog.setTitle(technicien == null ? "Ajouter technicien" : "Modifier technicien");

        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField(technicien != null ? technicien.getEmail() : "");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(lblEmail, 0, 0); grid.add(tfEmail, 1, 0);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                if (technicien == null)
                    return new Technicien(0, tfEmail.getText());
                else {
                    technicien.setEmail(tfEmail.getText());
                    return technicien;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (technicien == null) {
                Technicien added = technicienService.add(result);
                if (added != null) data.add(added);
            } else {
                boolean ok = technicienService.update(result);
                if (ok) loadData();
            }
        });
    }
}