package de.einausgaben.haushaltskasse1.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/einausgaben/haushaltskasse1/hello-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Haushaltskasse");
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);

    }
}