package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.ArrayList;

public class ShowParadaController {

    @FXML
    private TableView<Parada> table;

    @FXML
    private TableColumn<Parada, String> colNombre;

    @FXML
    private TableColumn<Parada, String> colTransporte;

    private final Grafo grafo = Grafo.getInstance();
    private MainController mainController = null;

    public void initialize(){
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreParada"));
        colTransporte.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        ObservableList<Parada> paradas = FXCollections.observableArrayList(new ArrayList<>(grafo.getSetParadas()));

        table.setItems(paradas);
    }

    /* Nombre: verDetalles
        Funcion: Abrir la ventana de Ver Detalles.
        Retorno: void.
    */
    public void verDetalles(){
        Parada selected = table.getSelectionModel().getSelectedItem();

        FXMLLoader loader = Visual.openNewWindow("CrearParada.fxml","Estilo.css");

        CrearParadaController controller = loader.getController();


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
