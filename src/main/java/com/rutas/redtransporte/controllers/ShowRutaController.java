package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShowRutaController {
    @FXML
    private TableView<Ruta> table;

    @FXML
    private TableColumn<Ruta, String> colNombre;

    @FXML
    private TableColumn<Ruta, Double> colDistancia;

    @FXML
    private TableColumn<Ruta, Double> colTiempo;

    @FXML
    private TableColumn<Ruta, Double> colCosto;

    private final Grafo grafo = Grafo.getInstance();
    private MainController mainController = null;

    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreRuta"));
        colDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        ObservableList<Ruta> rutas = FXCollections.observableArrayList(grafo.getListRutas());
        table.setItems(rutas);
    }

    /* Nombre: verDetalles
        Funcion: Abrir la ventana de Ver Detalles.
        Retorno: void.
    */
    public void verDetalles(){
        Ruta selected = table.getSelectionModel().getSelectedItem();

        FXMLLoader loader = Visual.openNewWindow("CrearRuta.fxml","Estilo.css");
        CrearRutaController controller = loader.getController();

        controller.setScene(selected);
        controller.setMainController(mainController);

        Visual.closeWindow(table);
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
