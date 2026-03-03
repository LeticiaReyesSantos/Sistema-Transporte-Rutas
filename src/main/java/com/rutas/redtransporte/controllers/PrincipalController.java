package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.utilidad.Visual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private VBox vboxMenu;

    @FXML
    private Button btnMenuParada;

    @FXML
    private Button btnMenu;

    public void showMenu(ActionEvent e){
        vboxMenu.setVisible(true);
        btnMenu.setVisible(false);
    }

    public void hideMenu(ActionEvent e){
        vboxMenu.setVisible(false);
        btnMenu.setVisible(true);
    }

    public void crearParada(ActionEvent e) throws IOException {
        Visual.openNewWindow("CrearParada.fxml","CrearParada.css");
    }


}



