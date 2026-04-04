package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.servicios.ClaseService;
import com.rutas.redtransporte.servicios.ParadaService;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CrearParadaController {

    @FXML
    private ComboBox<String> cbxTipo;

    @FXML
    private TextField txtNombre;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    private final Grafo grafo = Grafo.getInstance();
    private final ParadaService paradaService = new ParadaService();
    private Parada paradaSelected = null;

    public void initialize(){
        ClaseService.getInstance().registrarClase(CrearParadaController.class,this);
        cbxTipo.getItems().addAll("Carro","Bus","Monorriel");
    }

    public void guardarParada(){

        if(Logico.emptyFields(txtNombre,cbxTipo)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return;
        }

        Parada parada = new Parada(Logico.checkText(txtNombre),cbxTipo.getValue());

        if(!paradaService.crear(parada)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+parada.getNombreParada()+"\" ");
            return;
        }

        Mensaje.defaultMessages(Mensaje.OpcionMensaje.SAVED,parada.getNombreParada());
        Logico.cleanFields(txtNombre,cbxTipo);
    }

    /* Nombre: setScene
       Funcion: Organiza la ventana para mostrar el elemento seleccionado.
       Retorno: void.
   */
    public void setScene(Parada parada){
        btnEliminar.setVisible(true);
        btnModificar.setVisible(true);
        btnAceptar.setVisible(false);
        btnCancelar.setLayoutX(201);

        txtNombre.setText(parada.getNombreParada());
        cbxTipo.setValue(parada.getTipo());

        paradaSelected = parada;
    }

    public void modificarParada(){
        Parada paradaModificada = new Parada(paradaSelected);

        if(Logico.emptyFields(txtNombre,cbxTipo)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return;
        }

        paradaModificada.setNombreParada(txtNombre.getText());
        paradaModificada.setTipo(cbxTipo.getValue());

        if(!paradaService.modificar(paradaSelected,paradaModificada)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+paradaModificada.getNombreParada()+"\" ");
            return;
        }
            Visual.closeWindow(btnModificar);
    }

    public void eliminarParada() {
        Mensaje.defaultMessages(Mensaje.OpcionMensaje.DELETE, "Todas las rutas relacionadas serán eliminadas.");
        paradaService.eliminar(paradaSelected);

    }

    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }

}

