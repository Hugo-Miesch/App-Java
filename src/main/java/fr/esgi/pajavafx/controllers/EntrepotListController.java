package fr.esgi.pajavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.esgi.pajavafx.models.Entrepot;
import fr.esgi.pajavafx.services.EntrepotService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EntrepotListController implements Initializable {
    @FXML private TableView<Entrepot> tableEntrepots;
    @FXML private TableColumn<Entrepot, Integer> colId;
    @FXML private TableColumn<Entrepot, String> colName;
    @FXML private TableColumn<Entrepot, Double> colLat;
    @FXML private TableColumn<Entrepot, Double> colLng;
    @FXML private TableColumn<Entrepot, Integer> colCapacity;
    @FXML private TableColumn<Entrepot, Void> colActions;

    private final EntrepotService entrepotService = new EntrepotService();
    private final ObservableList<Entrepot> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLat.setCellValueFactory(new PropertyValueFactory<>("location_lat"));
        colLng.setCellValueFactory(new PropertyValueFactory<>("location_lng"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("max_capacity"));
        loadData();
        setupActionButtons();
    }

    private void loadData() {
        List<Entrepot> entrepots = entrepotService.fetchAll();
        data.setAll(entrepots);
        tableEntrepots.setItems(data);
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Entrepot, Void> call(final TableColumn<Entrepot, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("Éditer");
                    private final Button btnDelete = new Button("Supprimer");

                    {
                        btnEdit.setOnAction(e -> showEditDialog(getTableView().getItems().get(getIndex())));
                        btnDelete.setOnAction(e -> {
                            Entrepot a = getTableView().getItems().get(getIndex());
                            boolean ok = entrepotService.delete(a.getId());
                            if(ok) data.remove(a);
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
    private void onAddEntrepot() { showEditDialog(null); }

    private void showEditDialog(Entrepot entrepot) {
        Dialog<Entrepot> dialog = new Dialog<>();
        dialog.setTitle(entrepot == null ? "Ajouter entrepôt" : "Modifier entrepôt");

        Label lblName = new Label("Nom:");
        TextField tfName = new TextField(entrepot != null ? entrepot.getName() : "");
        Label lblLat = new Label("Latitude:");
        TextField tfLat = new TextField(entrepot != null ? String.valueOf(entrepot.getLocation_lat()) : "");
        Label lblLng = new Label("Longitude:");
        TextField tfLng = new TextField(entrepot != null ? String.valueOf(entrepot.getLocation_lng()) : "");
        Label lblCap = new Label("Capacité:");
        TextField tfCap = new TextField(entrepot != null ? String.valueOf(entrepot.getMax_capacity()) : "");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(lblName, 0, 0); grid.add(tfName, 1, 0);
        grid.add(lblLat, 0, 1); grid.add(tfLat, 1, 1);
        grid.add(lblLng, 0, 2); grid.add(tfLng, 1, 2);
        grid.add(lblCap, 0, 3); grid.add(tfCap, 1, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                try {
                    String name = tfName.getText();
                    double lat = Double.parseDouble(tfLat.getText());
                    double lng = Double.parseDouble(tfLng.getText());
                    int cap = Integer.parseInt(tfCap.getText());
                    if (entrepot == null)
                        return new Entrepot(0, name, lat, lng, cap);
                    else {
                        entrepot.setName(name);
                        entrepot.setLocation_lat(lat);
                        entrepot.setLocation_lng(lng);
                        entrepot.setMax_capacity(cap);
                        return entrepot;
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
            if (entrepot == null) {
                Entrepot added = entrepotService.add(result);
                if (added != null) data.add(added);
            } else {
                boolean ok = entrepotService.update(result);
                if (ok) loadData();
            }
        });
    }
}
