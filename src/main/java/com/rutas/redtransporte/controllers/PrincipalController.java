package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.utilidad.Funciones;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView imgMenu;

    @FXML
    private VBox vboxMenu;

    @FXML
    private ImageView imgMenuVbox;

    @FXML
    void showMenuBar(MouseEvent event) {
        vboxMenu.setVisible(true);
    }

    @FXML
    void hideMenuBar(MouseEvent event) {
        vboxMenu.setVisible(false);
    }

    @FXML
    void crearParada(MouseEvent event) throws IOException {
        Funciones.openWindow("/appvisuals/CrearParada.fxml");
    }

    public void iniLabels(){

    }

}



