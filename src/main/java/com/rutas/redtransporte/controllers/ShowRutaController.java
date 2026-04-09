package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.servicios.RutaService;
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

public class ShowRutaController {
    @FXML
    private TableView<Ruta> table;

    @FXML
    private TableColumn<Ruta, Integer> colId;

    @FXML
    private TableColumn<Ruta, String> colNombre;

    @FXML
    private TableColumn<Ruta, Double> colDistancia;

    @FXML
    private TableColumn<Ruta, Double> colTiempo;

    @FXML
    private TableColumn<Ruta, Double> colCosto;

    private final Grafo grafo = Grafo.getInstance();
    private final RutaService rutaService = new RutaService();

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idRuta"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreRuta"));
        colDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        updateTable();
    }

    public void verDetalles(){
        Ruta rutaSelected = table.getSelectionModel().getSelectedItem();

        if(rutaSelected == null){
            Mensaje.showMessage(Alert.AlertType.ERROR, "Opción inválida", "", "Debe elegir una ruta.");
            return;
        }

        switch (rutaService.verDetalles(rutaSelected)){
            case NO_EXISTE -> Mensaje.showMessage(Alert.AlertType.ERROR, "Opción inválida", "", "No hay rutas creadas.");
            case EXITO -> updateTable();
        }
    }

    public void updateTable() {
        ObservableList<Ruta> rutas = FXCollections.observableArrayList(grafo.getListRutas());
        table.setItems(rutas);
        table.refresh();
    }

    public void volver(ActionEvent e) {
        Visual.closeWindow(e);
    }
}
