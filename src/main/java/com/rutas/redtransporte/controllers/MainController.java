package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane slider;

    @FXML
    private ImageView menuManager;

    @FXML
    private ImageView menuManagerInside;

    @FXML
    private VBox mainMenu;

    private boolean isMenuOpen = false;
    public void initialize() {
        Logico.crearDatos();
        setMenu();
    }

    public void setMenu(){
        mainMenu.setTranslateX(-mainMenu.getPrefWidth());
        menuManager.setOnMouseClicked(event -> showMenu());
        menuManagerInside.setOnMouseClicked(event -> showMenu());
    }

    private void showMenu() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(mainMenu);

        if (isMenuOpen) {
            slide.setToX(-mainMenu.getPrefWidth());
            isMenuOpen = false;
        } else {
            slide.setToX(0);
            isMenuOpen = true;
        }

        slide.play();
    }
    public void crearParada(ActionEvent e) throws IOException {
        Visual.openNewWindow("CrearParada.fxml","Estilo.css",true);
    }

    public void mostrarParada(ActionEvent e) throws IOException {
        Visual.openNewWindow("ShowParada.fxml","Estilo.css",true);
    }

    public void crearRuta(ActionEvent e) throws IOException {
        //Visual.openNewWindow("CrearRuta.fxml","Estilo.css");
    }

    public void mostrarRuta(ActionEvent e) throws IOException {
        //Visual.openNewWindow("ShowRuta.fxml","Estilo.css");
    }
}