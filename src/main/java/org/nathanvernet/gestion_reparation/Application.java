package org.nathanvernet.gestion_reparation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Gestion Réparation by Nathan VERNET");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}