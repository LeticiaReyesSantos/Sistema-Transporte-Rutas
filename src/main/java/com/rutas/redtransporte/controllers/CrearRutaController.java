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


    private Grafo grafo = Grafo.getInstance();

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

        if (MainController.instance != null) {
            MainController.instance.actualizarMapa();
        }

        Mensaje.defaultMessages(Mensaje.OpcionMensaje.SAVED,ruta.getNombreRuta());
        Logico.cleanFields(txtNombre,cbxOrigen,cbxDestino,txtDistancia,txtTiempo,txtCosto);

    }

    /* Nombre: createRuta
        Funcion: Crear una ruta a partir de los datos ingresados por el usuario.
        Retorno: Ruta: ruta creada.
    */
    public Ruta createRuta(){

        if(Logico.emptyFields(txtNombre,cbxOrigen,cbxDestino,txtDistancia,txtTiempo,txtCosto)){
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
            return null;
        } else if (cbxOrigen.getValue() != null && cbxOrigen.getValue().equals(cbxDestino.getValue())) {
            Mensaje.showMessage(Alert.AlertType.ERROR,"Error","Dato inválido.","El destino no puede ser el origen.");
            return null;
        }

        Double distancia = Logico.checkNumeric(txtDistancia.getText());
        Double tiempo = Logico.checkNumeric(txtTiempo.getText());
        Double costo = Logico.checkNumeric(txtCosto.getText());

        if(distancia < 0 || tiempo < 0 || costo == 0)
            return null;

        return new Ruta(txtNombre.getText(),
                        grafo.getParada(cbxOrigen.getValue()),
                        grafo.getParada(cbxDestino.getValue()),
                        costo,tiempo,distancia);
    }



    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }
}
