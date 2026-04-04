package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.servicios.ClaseService;
import com.rutas.redtransporte.servicios.ParadaService;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.ArrayList;

public class ShowParadaController {

    @FXML
    private TableView<Parada> table;

    @FXML
    private TableColumn<Parada, Integer> colId;

    @FXML
    private TableColumn<Parada, String> colNombre;

    @FXML
    private TableColumn<Parada, String> colTransporte;

    private final Grafo grafo = Grafo.getInstance();
    ParadaService paradaService = new ParadaService();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("idParada"));
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
        Parada paradaSelected = table.getSelectionModel().getSelectedItem();

        if(grafo.getMap().isEmpty()){
            Mensaje.showMessage(Alert.AlertType.ERROR, "Opción inválida", "", "No hay paradas creadas.");
            return;

        }else if(paradaSelected == null) {
            Mensaje.showMessage(Alert.AlertType.ERROR, "Opción inválida", "", "Debe elegir una parada.");
            return;
        }

        paradaService.verDetalles(paradaSelected);
    }

    public void updateTable(){
        table.refresh();
//        ObservableList<Parada> paradasUpdated = FXCollections.observableArrayList(new ArrayList<>(grafo.getSetParadas()));
//        table.setItems(paradasUpdated);
    }

    /* Nombre: volver
        Funcion: Volver a la ventana principal.
        Retorno: void.
    */
    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }
}
