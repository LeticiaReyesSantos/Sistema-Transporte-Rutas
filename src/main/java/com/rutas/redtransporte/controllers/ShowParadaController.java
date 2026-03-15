package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private Grafo grafo = Grafo.getInstance();
    private ObservableList<Parada> paradas;

    public void initialize(){
        colNombre.setCellValueFactory(new PropertyValueFactory<Parada,String>("nombreParada"));
        colTransporte.setCellValueFactory(new PropertyValueFactory<Parada,String>("tipo"));

        paradas = FXCollections.observableArrayList(new ArrayList<>(grafo.getSetParadas()));

        table.setItems(paradas);
    }

    public void verDetalles() throws Exception{
        Parada selected = table.getSelectionModel().getSelectedItem();

        System.out.println(selected.getNombreParada());
        FXMLLoader loader = Visual.openNewWindow("CrearParada.fxml","Estilo.css");
        CrearParadaController controller = loader.getController();

        controller.setScene(selected);

        Visual.closeWindow(table);

    }

}
