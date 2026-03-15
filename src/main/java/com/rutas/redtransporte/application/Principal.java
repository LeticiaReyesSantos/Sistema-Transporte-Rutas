package com.rutas.redtransporte.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Principal extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(Principal.class.getResource("/appvisuals/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/appvisuals/Estilo.css").toExternalForm());
        stage.setTitle("Red de Transporte");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
