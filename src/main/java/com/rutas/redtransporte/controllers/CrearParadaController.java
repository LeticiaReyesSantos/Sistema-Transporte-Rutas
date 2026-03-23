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
    private Button btnEliminar;

    private final Grafo grafo = Grafo.getInstance();
    private MainController mainController = null;
    private Parada paradaSelected = null;

    public void initialize(){
        cbxTipo.getItems().addAll("Carro","Bus","Monorriel");
    }

    /* Nombre: guardarParada
        Funcion: Guardar una parada creada.
                 *Verifica que la parada no exista.
        Retorno: void.
    */
    public void guardarParada(){

        Parada parada = crearParada();

        if(grafo.addParada(parada) != null){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+parada.getNombreParada()+"\" ");
            return;
        }

        mainController.getGrafoVisual().crearParada(parada);

        Mensaje.defaultMessages(Mensaje.OpcionMensaje.SAVED,parada.getNombreParada());
        Logico.cleanFields(txtNombre,cbxTipo);
    }

    /* Nombre: createParada
        Funcion: Crea una parada con los datos ingresados por el usuario.
                 *Verifica que no haya campos vacíos.
        Retorno: (Parada) creada.
    */
    public Parada crearParada(){
        if(Logico.emptyFields(txtNombre,cbxTipo)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return null;
        }

        return new Parada(Logico.checkText(txtNombre),cbxTipo.getValue());
    }

    /* Nombre: eliminarParada
        Funcion: Elimina una parada de manera lógica y visual.
        Retorno: void.
    */
    public void eliminarParada() {
        Mensaje.defaultMessages(Mensaje.OpcionMensaje.DELETE, "Si elimina esta parada, todas las rutas relacionadas serán eliminadas.");
        grafo.deleteParada(paradaSelected);

        mainController.getGrafoVisual().eliminarParada(paradaSelected);
    }

    /* Nombre: setScene
       Funcion: Organiza la ventana para mostrar el elemento seleccionado.
       Retorno: void.
   */
    public void setScene(Parada parada){

        btnAceptar.setLayoutX(111);
        btnAceptar.setText("Modificar");

        btnCancelar.setLayoutX(195);

        btnEliminar.setVisible(true);

        txtNombre.setText(parada.getNombreParada());
        cbxTipo.setValue(parada.getTipo());

        paradaSelected = parada;
    }

    /* Nombre: setMainController
        Funcion: Asigna la instancia de mainController.
        Retorno: void.
    */
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }


    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }

}

