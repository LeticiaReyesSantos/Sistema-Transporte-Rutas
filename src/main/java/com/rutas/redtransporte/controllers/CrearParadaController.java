package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.OpcionMensaje;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrearParadaController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane pane;

    @FXML
    private ComboBox<String> cbxTipo;

    @FXML
    private TextField txtNombre;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    private Map<Parada, List<Ruta>> map = Grafo.getInstance().getMap();

    public void initialize(){
        cbxTipo.getItems().addAll("Carro","Bus","Monorriel");
    }

    /* Nombre: guardarParada
        Funcion: Guardar una parada creada.
                 *Verifica que el nombre no esté vacío y que la parada no exista.
        Retorno: void.
    */
    public void guardarParada(ActionEvent event) {
        if(Visual.emptyFields(txtNombre,cbxTipo)){
            Visual.defaultMessages(OpcionMensaje.EMPTY,"");
            return;
        }

        System.out.println("Inside guardarParada");

        Parada parada = new Parada(Visual.checkText(txtNombre),cbxTipo.getValue());

        if(!Grafo.getInstance().addParada(parada)){
            Visual.defaultMessages(OpcionMensaje.EXISTING,"Existe una parada en \""+parada.getNombreParada()+"\" ");
            return;
        }

        Visual.defaultMessages(OpcionMensaje.SAVED,parada.getNombreParada());
        Visual.cleanFields(txtNombre,cbxTipo);
    }

    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }

}

