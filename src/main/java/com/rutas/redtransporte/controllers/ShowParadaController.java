package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ShowParadaController {

    @FXML
    private TableView<Parada> table;

    @FXML
    private TableColumn<Parada, String> colNombre;

    @FXML
    private TableColumn<Parada, String> colTransporte;

    private Grafo grafo = Grafo.getInstance();
    private ObservableList<Parada> paradas;

    public void initialize(){
        colNombre.setCellValueFactory(new PropertyValueFactory<Parada,String>("nombreParada"));
        colTransporte.setCellValueFactory(new PropertyValueFactory<Parada,String>("tipo"));

        paradas = FXCollections.observableArrayList(grafo.getListParadas());

        table.setItems(paradas);
        //table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void verDetalles() throws Exception{
        Parada selected = table.getSelectionModel().getSelectedItem();

        System.out.println(selected.getNombreParada());
        FXMLLoader loader = Visual.openNewWindow("CrearParada.fxml","Estilo.css",false);
        CrearParadaController controller = loader.getController();

        controller.loadInfo(selected);
    }

}
