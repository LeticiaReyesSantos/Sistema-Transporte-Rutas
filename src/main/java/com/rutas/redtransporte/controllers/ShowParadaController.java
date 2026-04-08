package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
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
    private final ParadaService paradaService = new ParadaService();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("idParada"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreParada"));
        colTransporte.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        updateTable();
    }


    public void verDetalles(){
        Parada paradaSelected = table.getSelectionModel().getSelectedItem();

        switch (paradaService.verDetalles(paradaSelected)){
            case NO_EXISTE -> Mensaje.showMessage(Alert.AlertType.ERROR, "Opción inválida", "", "No hay paradas creadas.");
            case VACIO ->  Mensaje.showMessage(Alert.AlertType.ERROR, "Opción inválida", "", "Debe elegir una parada.");
            case EXITO -> updateTable();
        }

    }

    public void updateTable() {
        ObservableList<Parada> paradas = FXCollections.observableArrayList(grafo.getSetParadas());
        table.setItems(paradas);

        table.refresh();
    }

    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }
}
