package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.servicios.RutaService;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Resultado;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

//@SuppressWarnings("unused")
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
    private final RutaService rutaService = new RutaService();
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

    public void guardarRuta(){
        Ruta ruta = createRuta();
        if(ruta == null) return;

        Resultado result = rutaService.guardar(ruta);

        switch (result){
            case EXITO -> {
                Mensaje.defaultMessages(Mensaje.OpcionMensaje.SAVED, ruta.getNombreRuta());
                Logico.cleanFields(txtNombre, cbxOrigen, cbxDestino, txtDistancia, txtTiempo, txtCosto);
            }

            case NO_CAMBIOS -> Mensaje.showMessage(Alert.AlertType.WARNING,"Modificación","No hay cambios en la ruta."," ");

            case NOMBRE_EXISTE -> Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una ruta con el nombre "+ruta.getNombreRuta());

            case EXISTE -> Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una ruta de "+ruta.getOrigen()+" a "+ruta.getDestino());
        }
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

    /*
    Crea una ruta con el mismo id de la original para mantener la referencia en el mapa.
    Una vez se verifica la modificación, se reemplaza los datos en la ruta original.
    */
    public void modificarRuta(){
        if(!verificarRuta()) return;

        Ruta rutaModificada = new Ruta(rutaSelected);
        rutaModificada.setNombreRuta(txtNombre.getText());
        rutaModificada.setOrigen(grafo.getParada(cbxOrigen.getValue()));
        rutaModificada.setDestino(grafo.getParada(cbxDestino.getValue()));
        rutaModificada.setDistancia(Double.parseDouble(txtDistancia.getText()));
        rutaModificada.setTiempo(Double.parseDouble(txtTiempo.getText()));
        rutaModificada.setCosto(Double.parseDouble(txtCosto.getText()));

        switch (rutaService.modificar(rutaSelected,rutaModificada)){
            case EXITO -> {
                Mensaje.defaultMessages(Mensaje.OpcionMensaje.MODIFIED,null);
                Visual.closeWindow(btnModificar);
            }

            case NO_CAMBIOS -> Mensaje.showMessage(Alert.AlertType.WARNING,"Modificación","No hay cambios en la ruta."," ");

            case NOMBRE_EXISTE -> Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una ruta con el nombre "+rutaModificada.getNombreRuta());

            case EXISTE -> Mensaje.defaultMessages(Mensaje.OpcionMensaje.EXISTING,"Existe una ruta de "+rutaModificada.getOrigen()+" a "+rutaModificada.getDestino());
        }
    }

    /* Nombre: eliminarRuta
        Funcion: Elimina la ruta seleccionada.
        Retorno: void.
    */
    public void eliminarRuta() {
        Optional<ButtonType> respuesta = Mensaje.showMessage(
                Alert.AlertType.CONFIRMATION,
                "Eliminar", "¿Desea eliminar este elemento?",
                "Las paradas de esta ruta se desconectarán."
        );
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            rutaService.eliminar(rutaSelected);
            Visual.closeWindow(btnEliminar);
        }
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

    /* Nombre: loadDatos
    Funcion: Organiza la ventana para mostrar el elemento seleccionado.
    Retorno: void.
    */
    public void loadDatos(Ruta ruta){

        btnEliminar.setVisible(true);
        btnModificar.setVisible(true);
        btnAceptar.setVisible(false);
        btnCancelar.setLayoutX(244);

        rutaSelected = ruta;
        loadRutaInfo(ruta);
    }

    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }
}
