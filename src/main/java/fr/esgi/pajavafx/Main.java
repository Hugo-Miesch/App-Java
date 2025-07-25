package fr.esgi.pajavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;

        // Démarre sur la page Login
        showLoginView();
    }

    public static void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fr/esgi/pajavafx/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // Applique le CSS
        scene.getStylesheets().add(Main.class.getResource("/fr/esgi/pajavafx/style.css").toExternalForm());
        mainStage.setTitle("Connexion | Jaegers Ops");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showHomeView() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fr/esgi/pajavafx/Home.fxml")); // à adapter au nom exact de ton FXML principal
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // Applique le CSS
        scene.getStylesheets().add(Main.class.getResource("/fr/esgi/pajavafx/style.css").toExternalForm());
        mainStage.setTitle("Backoffice Jaegers Ops");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
