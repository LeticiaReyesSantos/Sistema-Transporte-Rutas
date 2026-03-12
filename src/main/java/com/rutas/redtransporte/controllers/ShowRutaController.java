package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

    private Grafo grafo = Grafo.getInstance();
    private ObservableList<Ruta> rutas;

    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<Ruta, String>("nombreRuta"));
        colDistancia.setCellValueFactory(new PropertyValueFactory<Ruta, Double>("distancia"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<Ruta, Double>("tiempo"));
        colCosto.setCellValueFactory(new PropertyValueFactory<Ruta, Double>("costo"));

        rutas = FXCollections.observableArrayList(grafo.getListRutas());
        table.setItems(rutas);
    }
}
