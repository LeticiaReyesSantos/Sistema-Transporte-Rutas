package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Visual;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane slider;

    @FXML
    private ImageView menuManager;

    @FXML
    private ImageView menuManagerInside;

    @FXML
    private VBox mainMenu;

    public static MainController instance;

    private boolean isMenuOpen = false;

    private SmartGraphPanel<Parada, Ruta> panelMapa;

    public void initialize() {
        instance = this;
        Logico.crearDatos();
        setMenu();
        iniciarMapa();
    }

    private void iniciarMapa() {
        Digraph<Parada, Ruta> grafoVisual = new DigraphEdgeList<>();

        var mapaBackend = com.rutas.redtransporte.modelos.Grafo.getInstance().getMap();

        for (Parada parada : mapaBackend.keySet()) {
            grafoVisual.insertVertex(parada);
        }

        for (Parada origen : mapaBackend.keySet()) {
            for (Ruta ruta : mapaBackend.get(origen)) {
                Parada destino = ruta.getDestino();
                grafoVisual.insertEdge(origen, destino, ruta);
            }
        }

        panelMapa = new SmartGraphPanel<>(grafoVisual);

        AnchorPane.setTopAnchor(panelMapa, 0.0);
        AnchorPane.setBottomAnchor(panelMapa, 0.0);
        AnchorPane.setLeftAnchor(panelMapa, 0.0);
        AnchorPane.setRightAnchor(panelMapa, 0.0);

        slider.getChildren().add(panelMapa);

        panelMapa.toBack();

        Platform.runLater(() -> {
            panelMapa.init();
        });
    }

    public void setMenu(){
        mainMenu.setTranslateX(-mainMenu.getPrefWidth());
        menuManager.setOnMouseClicked(event -> showMenu());
        menuManagerInside.setOnMouseClicked(event -> showMenu());
    }

    private void showMenu() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(mainMenu);

        if (isMenuOpen) {
            slide.setToX(-mainMenu.getPrefWidth());
            isMenuOpen = false;
        } else {
            slide.setToX(0);
            isMenuOpen = true;
        }

        slide.play();
    }

    public void crearParada(ActionEvent e) throws IOException {
        Visual.openNewWindow("CrearParada.fxml","Estilo.css",true);
    }

    public void mostrarParada(ActionEvent e) throws IOException {
        Visual.openNewWindow("ShowParada.fxml","Estilo.css",true);
    }

    public void crearRuta(ActionEvent e) throws IOException {
        Visual.openNewWindow("CrearRuta.fxml","Estilo.css", true);
    }

    public void mostrarRuta(ActionEvent e) throws IOException {
        Visual.openNewWindow("ShowRuta.fxml","Estilo.css", true);
    }

    public void actualizarMapa() {
        slider.getChildren().remove(panelMapa);
        iniciarMapa();
    }
}