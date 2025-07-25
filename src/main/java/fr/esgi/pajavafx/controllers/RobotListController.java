package fr.esgi.pajavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import fr.esgi.pajavafx.models.Robot;
import fr.esgi.pajavafx.services.RobotService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RobotListController implements Initializable {
    @FXML private TableView<Robot> tableRobots;
    @FXML private TableColumn<Robot, Integer> colId;
    @FXML private TableColumn<Robot, String> colName;
    @FXML private TableColumn<Robot, Integer> colEntrepotId;
    @FXML private TableColumn<Robot, String> colImage;
    @FXML private TableColumn<Robot, Void> colActions;

    private final RobotService robotService = new RobotService();
    private final ObservableList<Robot> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEntrepotId.setCellValueFactory(new PropertyValueFactory<>("entrepot_id"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image_path"));
        loadData();
        setupActionButtons();
    }

    private void loadData() {
        List<Robot> robots = robotService.fetchAll();
        data.setAll(robots);
        tableRobots.setItems(data);
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Robot, Void> call(final TableColumn<Robot, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("Éditer");
                    private final Button btnDelete = new Button("Supprimer");
                    {
                        btnEdit.setOnAction(e -> showEditDialog(getTableView().getItems().get(getIndex())));
                        btnDelete.setOnAction(e -> {
                            Robot r = getTableView().getItems().get(getIndex());
                            boolean ok = robotService.delete(r.getId());
                            if (ok) data.remove(r);
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
    private void onAddRobot() { showEditDialog(null); }

    private void showEditDialog(Robot robot) {
        Dialog<Robot> dialog = new Dialog<>();
        dialog.setTitle(robot == null ? "Ajouter robot" : "Modifier robot");

        Label lblName = new Label("Nom:");
        TextField tfName = new TextField(robot != null ? robot.getName() : "");
        Label lblEntrepotId = new Label("ID entrepôt:");
        TextField tfEntrepotId = new TextField(robot != null ? String.valueOf(robot.getEntrepot_id()) : "");
        Label lblImage = new Label("Image (url):");
        TextField tfImage = new TextField(robot != null ? robot.getImage_path() : "");
        Label lblImageFile = new Label("Fichier image :");
        TextField tfImageFile = new TextField();
        tfImageFile.setEditable(false);
        Button btnImageFile = new Button("Sélectionner une image");
        FileChooser fileChooser = new FileChooser();
        btnImageFile.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (file != null) {
                tfImageFile.setText(file.getAbsolutePath());
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(lblName, 0, 0); grid.add(tfName, 1, 0);
        grid.add(lblEntrepotId, 0, 1); grid.add(tfEntrepotId, 1, 1);
        grid.add(lblImage, 0, 2); grid.add(tfImage, 1, 2);
        grid.add(lblImageFile, 0, 3); grid.add(tfImageFile, 1, 3); grid.add(btnImageFile, 2, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == okButton) {
                try {
                    String name = tfName.getText();
                    int entrepotId = Integer.parseInt(tfEntrepotId.getText());
                    String imagePath = tfImage.getText();
                    String imageFile = tfImageFile.getText();
                    if (robot == null) {
                        Robot newRobot = new Robot(0, name, entrepotId, imagePath);
                        Robot added = robotService.add(newRobot);
                        if (added != null) {
                            // Upload de l'image
                            if (!imageFile.isEmpty()) {
                                robotService.uploadImage(added.getId(), imageFile);
                            }
                            return added;
                        } else {
                            return null;
                        }
                    } else {
                        robot.setName(name);
                        robot.setEntrepot_id(entrepotId);
                        robot.setImage_path(imagePath);
                        boolean ok = robotService.update(robot);
                        if (ok) {
                            // Upload de l'image si elle a changé
                            if (!imageFile.isEmpty() && !imageFile.equals(robot.getImage_path())) {
                                robotService.uploadImage(robot.getId(), imageFile);
                            }
                            return robot;
                        } else {
                            return null;
                        }
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
            if (robot == null) {
                data.add(result);
            } else {
                loadData();
            }
        });
    }
}
