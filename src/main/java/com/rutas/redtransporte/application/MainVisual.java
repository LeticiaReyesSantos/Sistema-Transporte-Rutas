package com.rutas.redtransporte.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainVisual extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(MainVisual.class.getResource("/appvisuals/Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/appvisuals/Estilo.css").toExternalForm());
        stage.setTitle("PathFinder");
        Image iconoApp = new Image(getClass().getResourceAsStream("/imagenes/LogoOficial.png"));
        stage.getIcons().add(iconoApp);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
