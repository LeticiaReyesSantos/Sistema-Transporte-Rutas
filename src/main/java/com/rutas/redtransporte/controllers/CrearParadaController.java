package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.db.ParadaDAO;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
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

    private final Grafo grafo = Grafo.getInstance();
    private MainController mainController = null;
    private ShowParadaController showController = null;
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

        if(grafo.paradaExiste(parada)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+parada.getNombreParada()+"\" ");
            return;
        }

//        if(grafo.addParada(parada) != null){
//            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+parada.getNombreParada()+"\" ");
//            return;
//        }

        ParadaDAO.getInstance().guardarParada(parada);
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

    /* Nombre: modificarParada
    Funcion: Modifica una parada de manera lógica y visual.
    Retorno: void.
    */
    public void modificarParada(){
        Parada paradaModified = new Parada(paradaSelected);
        if(!modificarValido(paradaModified)){
            return;
        }

        mainController.getGrafoVisual().modificarParada(paradaSelected,paradaModified);
        paradaSelected.modificarParada(paradaModified);


        ParadaDAO.getInstance().actualizarParada(paradaModified);

        //Visual.openNewWindow("ShowParada.fxml","Estilo.css");
        Visual.closeWindow(btnModificar);
    }

    public boolean modificarValido(Parada paradaModified){
        if(Logico.emptyFields(txtNombre,cbxTipo)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return false;
        }

        paradaModified.setNombreParada(txtNombre.getText());
        paradaModified.setTipo(cbxTipo.getValue());

        if(!paradaSelected.cambiosParada(paradaModified)){
            Mensaje.showMessage(Alert.AlertType.ERROR,"Opción inválida","","No ha modificado la parada.");
            return false;
        }

        if(grafo.paradaExiste(paradaModified)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una parada en \""+paradaModified.getNombreParada()+"\" ");
            return false;
        }

        return true;

    }

//    public void modificarParada(){
//        Parada oldParada = new Parada(paradaSelected);
//
//        if(Logico.emptyFields(txtNombre,cbxTipo)){
//            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
//            return;
//        }
//
//        paradaSelected.setNombreParada(txtNombre.getText());
//        paradaSelected.setTipo(cbxTipo.getValue());
//
//        if(mainController == null)
//            System.out.println("MainController is null");
//
//        mainController.getGrafoVisual().modificarParada(oldParada,paradaSelected);
//        ParadaDAO.getInstance().actualizarParada(paradaSelected);
//
//        Visual.openNewWindow("ShowParada.fxml","Estilo.css");
//        Visual.closeWindow(btnModificar);
//    }

    /* Nombre: eliminarParada
    Funcion: Elimina una parada de manera lógica y visual.
    Retorno: void.
    */
    public void eliminarParada() {
        Mensaje.defaultMessages(Mensaje.OpcionMensaje.DELETE, "Todas las rutas relacionadas serán eliminadas.");

        grafo.deleteParada(paradaSelected);
        mainController.getGrafoVisual().eliminarParada(paradaSelected);
        ParadaDAO.getInstance().eliminarParada(paradaSelected.getIdParada());

        //Visual.openNewWindow("ShowParada.fxml","Estilo.css");
        showController.updateTable();
        Visual.closeWindow(btnModificar);
    }

    /* Nombre: setMainController
        Funcion: Asigna la instancia de mainController.
        Retorno: void.
    */
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }
    public void setShowController(ShowParadaController showController){this.showController = showController;};

    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }

}

