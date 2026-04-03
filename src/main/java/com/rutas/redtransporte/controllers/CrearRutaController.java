package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Collection;
import java.util.function.Function;

public class CrearRutaController {

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> cbxOrigen;

    @FXML
    private ComboBox<String> cbxDestino;

    @FXML
    private TextField txtDistancia;

    @FXML
    private TextField txtTiempo;

    @FXML
    private TextField txtCosto;

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
    private Ruta rutaSelected = null;

    /* Nombre: initialize
        Funcion: Inicializar elementos.
        Retorno: void.
    */
    public void initialize(){
        iniciarCombo(cbxOrigen, grafo.getSetParadas(),Parada::getNombreParada);
        iniciarCombo(cbxDestino, grafo.getSetParadas(),Parada::getNombreParada);
    }

    /* Nombre: iniciarCombo
        Funcion: Guardar las opciones que se mostrarán en comboBox.
        Retorno: void.
    */
    public <T> void iniciarCombo(ComboBox<String> combo, Collection<T> data, Function<T,String> mapper){
        combo.setItems(new FilteredList<>(Logico.getAttributes(data,mapper)));
        combo.setPromptText("Seleccionar");
        combo.setVisibleRowCount(5);
    }


    /* Nombre: guardarRuta
        Funcion: Guardar la ruta creada en el Grafo.
        Retorno: void.
    */
    public void guardarRuta(){
        Ruta ruta = createRuta();

        if(ruta == null)
            return;

        if(Grafo.getInstance().addRoute(ruta) == null){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Esta ruta ya existe.");
            return;
        }

        Mensaje.defaultMessages(Mensaje.OpcionMensaje.SAVED,ruta.getNombreRuta());
        Logico.cleanFields(txtNombre,cbxOrigen,cbxDestino,txtDistancia,txtTiempo,txtCosto);

        mainController.getGrafoVisual().crearRuta(ruta);
    }

    /* Nombre: createRuta
    Funcion: Crear una ruta a partir de los datos ingresados por el usuario.
    Retorno: (Ruta) ruta creada.
    */
    public Ruta createRuta(){

        if(verificarRuta())
            return new Ruta(txtNombre.getText(),
                    grafo.getParada(cbxOrigen.getValue()),
                    grafo.getParada(cbxDestino.getValue()),
                    Double.parseDouble(txtDistancia.getText()),
                    Double.parseDouble(txtTiempo.getText()),
                    Double.parseDouble(txtCosto.getText()));

        return null;
    }

    /* Nombre: verificarRuta
    Funcion: Verifica los datos ingresados por el usuario.
    Retorno: (boolean) -true- si son válidos.
    */
    public boolean verificarRuta(){

        if(Logico.emptyFields(txtNombre,cbxOrigen,cbxDestino,txtDistancia,txtTiempo,txtCosto)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return false;

        } else if (cbxOrigen.getValue() != null && cbxOrigen.getValue().equals(cbxDestino.getValue())) {
            Mensaje.showMessage(Alert.AlertType.ERROR,"Error","Dato inválido.","El destino no puede ser el origen.");
            return false;

        }else return Logico.inputIsNumeric(txtDistancia.getText(), txtTiempo.getText(), txtCosto.getText());

    }

    /* Nombre: setScene
    Funcion: Organiza la ventana para mostrar el elemento seleccionado.
    Retorno: void.
    */
    public void setScene(Ruta ruta){

        btnEliminar.setVisible(true);
        btnModificar.setVisible(true);
        btnAceptar.setVisible(false);
        btnCancelar.setLayoutX(244);

        rutaSelected = ruta;
        loadRutaInfo(ruta);
    }

    /* Nombre: modificarRuta
    Funcion: Modificar una ruta.
    Retorno: void.
    */
    public void modificarRuta(){
        Ruta oldRuta = new Ruta(rutaSelected);

        if(verificarRuta()){
            rutaSelected.setNombreRuta(txtNombre.getText());
            rutaSelected.setOrigen(grafo.getParada(cbxOrigen.getValue()));
            rutaSelected.setDestino(grafo.getParada(cbxDestino.getValue()));
            rutaSelected.setDistancia(Double.parseDouble(txtDistancia.getText()));
            rutaSelected.setTiempo(Double.parseDouble(txtTiempo.getText()));
            rutaSelected.setCosto(Double.parseDouble(txtCosto.getText()));
        }

        grafo.modifyRoute(oldRuta,rutaSelected);

        Mensaje.defaultMessages(Mensaje.OpcionMensaje.MODIFIED,null);
        mainController.getGrafoVisual().modificarRuta(oldRuta,rutaSelected);

        Visual.openNewWindow("ShowRuta.fxml","Estilo.css",false);
        Visual.closeWindow(btnModificar);
    }

    /* Nombre: eliminarRuta
        Funcion: Elimina la ruta seleccionada.
        Retorno: void.
    */
    public void eliminarRuta() {
        Mensaje.defaultMessages(Mensaje.OpcionMensaje.DELETE, "Las paradas de esta ruta se desconectarán.");
        grafo.deleteRoute(rutaSelected);

        mainController.getGrafoVisual().eliminarRuta(rutaSelected);
    }

    /* Nombre: loadRutaInfo
       Funcion: Carga la información del elemento seleccionado.
       Retorno: void.
   */
    public void loadRutaInfo(Ruta ruta){
        txtNombre.setText(ruta.getNombreRuta());

        cbxOrigen.setValue(ruta.getOrigen().getNombreParada());
        cbxDestino.setValue(ruta.getDestino().getNombreParada());

        txtDistancia.setText(String.valueOf(ruta.getDistancia()));
        txtTiempo.setText(String.valueOf(ruta.getTiempo()));
        txtCosto.setText(String.valueOf(ruta.getCosto()));
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
