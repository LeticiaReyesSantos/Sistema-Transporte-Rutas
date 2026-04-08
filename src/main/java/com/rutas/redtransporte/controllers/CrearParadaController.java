package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.servicios.ClaseService;
import com.rutas.redtransporte.servicios.ParadaService;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Resultado;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        switch (paradaService.guardar(parada)){
            case Resultado.EXITO -> {
                Mensaje.defaultMessages(Mensaje.OpcionMensaje.SAVED,parada.getNombreParada());
                Logico.cleanFields(txtNombre,cbxTipo);
            }

            case Resultado.EXISTE -> Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+parada.getNombreParada()+"\" ");
        }

    }

    public void modificarParada(){
        Parada paradaModificada = new Parada(paradaSelected);

        if(Logico.emptyFields(txtNombre,cbxTipo)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return;
        }

        paradaModificada.setNombreParada(txtNombre.getText());
        paradaModificada.setTipo(cbxTipo.getValue());

        switch (paradaService.modificar(paradaSelected,paradaModificada)){
            case EXITO -> Visual.closeWindow(btnModificar);
            case NO_CAMBIOS -> Mensaje.showMessage(Alert.AlertType.WARNING,"Modificación","No hay cambios en la parada."," ");
            case EXISTE -> Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING, "Existe una parada en \"" + paradaModificada.getNombreParada() + "\" ");
        }

    }

    public void eliminarParada() {
        Mensaje.defaultMessages(Mensaje.OpcionMensaje.DELETE, "Todas las rutas relacionadas serán eliminadas.");
        paradaService.eliminar(paradaSelected);
        Visual.closeWindow(btnEliminar);

    }

    /* Nombre: loadDatos
   Funcion: Organiza la ventana para mostrar el elemento seleccionado.
   Retorno: void.
    */
    public void loadDatos(Parada parada){

        btnEliminar.setVisible(true);
        btnModificar.setVisible(true);
        btnAceptar.setVisible(false);
        btnCancelar.setLayoutX(201);

        txtNombre.setText(parada.getNombreParada());
        cbxTipo.setValue(parada.getTipo());

        paradaSelected = parada;
    }

    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }

}

