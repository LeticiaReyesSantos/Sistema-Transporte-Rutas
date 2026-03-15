package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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

    @FXML
    private Button btnEliminar;

    private Grafo grafo = Grafo.getInstance();
    private Parada paradaSelect = null;

    public void initialize(){
        cbxTipo.getItems().addAll("Carro","Bus","Monorriel");
    }

    /* Nombre: guardarParada
        Funcion: Guardar una parada creada.
                 *Verifica que el nombre no esté vacío y que la parada no exista.
        Retorno: void.
    */
    public void guardarParada(ActionEvent event) {

        Parada parada = createParada();

        if(grafo.addParada(parada) != null){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+parada.getNombreParada()+"\" ");
            return;
        }

        if (MainController.instance != null) {
            MainController.instance.actualizarMapa();
        }

        Mensaje.defaultMessages(Mensaje.OpcionMensaje.SAVED,parada.getNombreParada());
        Logico.cleanFields(txtNombre,cbxTipo);
    }

    public Parada createParada(){
        if(Logico.emptyFields(txtNombre,cbxTipo)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return null;
        }

        return new Parada(Logico.checkText(txtNombre),cbxTipo.getValue());
    }

    public void eliminarParada(Parada parada) {
        Mensaje.defaultMessages(Mensaje.OpcionMensaje.DELETE, "Si elimina esta parada, todas las rutas relacionadas serán eliminadas.");
        grafo.deleteParade(paradaSelect);
    }

    public void setScene(Parada parada){

        btnAceptar.setLayoutX(111);
        btnAceptar.setText("Modificar");

        btnCancelar.setLayoutX(195);

        btnEliminar.setVisible(true);

        txtNombre.setText(parada.getNombreParada());
        cbxTipo.setValue(parada.getTipo());

        paradaSelect = parada;
    }

    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }

}

