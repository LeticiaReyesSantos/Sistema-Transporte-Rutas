package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.OpcionMensaje;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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


    private Map<Parada, List<Ruta>> map = Grafo.getInstance().getMap();

    /* Nombre: initialize
        Funcion: Inicializar elementos.
        Retorno: void.
    */
    public void initialize(){
        iniciarCombo(cbxOrigen, map.keySet(),Parada::getNombreParada);
        iniciarCombo(cbxDestino, map.keySet(),Parada::getNombreParada);
    }

    /* Nombre: iniciarCombo
        Funcion: Guardar las opciones que se mostrarán en comboBox.
        Retorno: void.
    */
    public <T> void iniciarCombo(ComboBox<String> combo, Collection<T> data, Function<T,String> mapper){
        combo.setItems(new FilteredList<>(Visual.getAttributes(data,mapper)));
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
            Visual.defaultMessages(OpcionMensaje.EXISTING,"Esta ruta ya existe.");
            return;
        }

        if (MainController.instance != null) {
            MainController.instance.actualizarMapa();
        }

        Visual.defaultMessages(OpcionMensaje.SAVED,ruta.getNombreRuta());
        Visual.cleanFields(txtNombre,cbxOrigen,cbxDestino,txtDistancia,txtTiempo,txtCosto);

        System.out.println(ruta.getNombreRuta()+", "+ruta.getDistancia());

    }

    /* Nombre: createRuta
        Funcion: Crear una ruta a partir de los datos ingresados por el usuario.
        Retorno: Ruta: ruta creada.
    */
    public Ruta createRuta(){

        if(Visual.emptyFields(txtNombre,cbxOrigen,cbxDestino,txtDistancia,txtTiempo,txtCosto)){
            Visual.defaultMessages(OpcionMensaje.EMPTY,"");
            return null;
        } else if (cbxOrigen.getValue() != null && cbxOrigen.getValue().equals(cbxDestino.getValue())) {
            Visual.showMessage(Alert.AlertType.ERROR,"Error","Dato inválido.","El destino no puede ser el origen.");
            return null;
        }

        String nombre = txtNombre.getText();

        Parada origen = getParada(cbxOrigen.getValue());
        Parada destino = getParada(cbxDestino.getValue());

        Double distancia = Visual.checkNumeric(txtDistancia.getText());
        Double tiempo = Visual.checkNumeric(txtTiempo.getText());
        Double costo = Visual.checkNumeric(txtCosto.getText());

        if(distancia < 0 || tiempo < 0 || costo == 0)
            return null;

        return new Ruta(nombre,origen,destino,costo,tiempo,distancia);
    }

    /* Nombre: getParada
        Funcion: Buscar parada a partir de su nombre.
        Retorno: Parada.
    */
    public Parada getParada(String nombre){
        List<Parada> list = new ArrayList<>(map.keySet());

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getNombreParada().equals(nombre))
                return list.get(i);
        }

        return null;
    }

    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }
}
