package fr.esgi.pajavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.esgi.pajavafx.models.Client;
import fr.esgi.pajavafx.services.ClientService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ClientListController implements Initializable {
    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colImage;
    @FXML private TableColumn<Client, Void> colActions;

    private final ClientService clientService = new ClientService();
    private final ObservableList<Client> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("name"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));

        loadData();
        setupActionButtons();
    }

    private void loadData() {
        List<Client> clients = clientService.fetchAll();
        data.setAll(clients);
        tableClients.setItems(data);
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("Éditer");
                    private final Button btnDelete = new Button("Supprimer");
                    {
                        btnEdit.setOnAction(e -> {
                            Client c = getTableView().getItems().get(getIndex());
                            showEditDialog(c);
                        });

                        btnDelete.setOnAction(e -> {
                            Client c = getTableView().getItems().get(getIndex());
                            boolean ok = clientService.delete(c.getId());
                            if(ok) {
                                data.remove(c);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur de suppression !");
                                alert.showAndWait();
                            }
                        });
                    }
                    HBox box = new HBox(5, btnEdit, btnDelete);

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : box);
                    }
                };
            }
        });
    }

    @FXML
    private void onAddClient() {
        showEditDialog(null);
    }

    private void showEditDialog(Client client) {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle(client == null ? "Ajouter client" : "Modifier client");


        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField(client != null ? client.getEmail() : "");
        Label lblNom = new Label("Nom:");
        TextField tfNom = new TextField(client != null ? client.getName() : "");
        Label lblImage = new Label("Image (url):");
        TextField tfImage = new TextField(client != null ? client.getImage() : "");
        Label lblPassword = new Label("Mot de passe:");
        PasswordField tfPassword = new PasswordField();
        if (client != null) tfPassword.setText(client.getPassword());

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(lblEmail, 0, 0); grid.add(tfEmail, 1, 0);
        grid.add(lblNom, 0, 1); grid.add(tfNom, 1, 1);
        grid.add(lblImage, 0, 2); grid.add(tfImage, 1, 2);
        grid.add(lblPassword, 0, 3); grid.add(tfPassword, 1, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                if (client == null)
                    return new Client(0, tfEmail.getText(), tfNom.getText(), tfImage.getText(), tfPassword.getText());
                else {
                    client.setEmail(tfEmail.getText());
                    client.setName(tfNom.getText());
                    client.setImage(tfImage.getText());
                    client.setPassword(tfPassword.getText());
                    return client;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (client == null) {
                Client added = clientService.add(result);
                if (added != null) data.add(added);
            } else {
                boolean ok = clientService.update(result);
                if (ok) loadData();
            }
        });
    }
}
