package fr.esgi.pajavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.esgi.pajavafx.models.SuperAdmin;
import fr.esgi.pajavafx.services.SuperAdminService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SuperAdminListController implements Initializable {
    @FXML
    private TableView<SuperAdmin> tableSuperAdmins;
    @FXML
    private TableColumn<SuperAdmin, Integer> colId;
    @FXML
    private TableColumn<SuperAdmin, String> colEmail;
    @FXML
    private TableColumn<SuperAdmin, Void> colActions;

    private final SuperAdminService superAdminService = new SuperAdminService();
    private final ObservableList<SuperAdmin> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadData();
        setupActionButtons();
    }

    private void loadData() {
        List<SuperAdmin> superAdmins = superAdminService.fetchAll();
        data.setAll(superAdmins);
        tableSuperAdmins.setItems(data);
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<SuperAdmin, Void> call(final TableColumn<SuperAdmin, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("Éditer");
                    private final Button btnDelete = new Button("Supprimer");

                    {
                        btnEdit.setOnAction(e -> showEditDialog(getTableView().getItems().get(getIndex())));
                        btnDelete.setOnAction(e -> {
                            SuperAdmin sa = getTableView().getItems().get(getIndex());
                            boolean ok = superAdminService.delete(sa.getId());
                            if (ok) data.remove(sa);
                            else new Alert(Alert.AlertType.ERROR, "Erreur de suppression !").showAndWait();
                        });
                    }

                    HBox box = new HBox(5, btnEdit, btnDelete);

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        setGraphic(empty ? null : box);
                    }
                };
            }
        });
    }

    @FXML
    private void onAddSuperAdmin() {
        showEditDialog(null);
    }

    private void showEditDialog(SuperAdmin admin) {
        Dialog<SuperAdmin> dialog = new Dialog<>();
        dialog.setTitle(admin == null ? "Ajouter super admin" : "Modifier super admin");

        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField(admin != null ? admin.getEmail() : "");
        Label lblPassword = new Label("Mot de passe:");
        PasswordField tfPassword = new PasswordField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(lblEmail, 0, 0);
        grid.add(tfEmail, 1, 0);
        grid.add(lblPassword, 0, 1);
        grid.add(tfPassword, 1, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                if (admin == null)
                    return new SuperAdmin(0, tfEmail.getText(), tfPassword.getText());
                else {
                    admin.setEmail(tfEmail.getText());
                    admin.setPassword(tfPassword.getText());
                    return admin;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (admin == null) {
                SuperAdmin added = superAdminService.add(result);
                if (added != null) data.add(added);
            } else {
                boolean ok = superAdminService.update(result);
                if (ok) loadData();
            }
        });
    }
}
