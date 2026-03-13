package com.rutas.redtransporte.controllers;

import com.brunomnsilva.smartgraph.graphview.*;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Visual;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import java.net.URI;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

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
    Digraph<Parada, Ruta> grafoVisual = new DigraphEdgeList<>();

    public void initialize() {
        instance = this;
        Logico.crearDatos();
        setMenu();
        iniciarMapa();
    }

    private void iniciarMapa() {
        grafoVisual = new DigraphEdgeList<>();
        sincronizarGrafo();

        SmartGraphProperties properties = new SmartGraphProperties(
                getClass().getResourceAsStream("/smartgraph.properties")
        );

        URI cssURI = null;
        try {
            cssURI = Objects.requireNonNull(
                    getClass().getResource("/smartgraph.css")
            ).toURI();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SmartPlacementStrategy placement = new SmartRandomPlacementStrategy();
        panelMapa = new SmartGraphPanel<>(grafoVisual, properties, placement, cssURI);

        AnchorPane.setTopAnchor(panelMapa, 0.0);
        AnchorPane.setBottomAnchor(panelMapa, 0.0);
        AnchorPane.setLeftAnchor(panelMapa, 0.0);
        AnchorPane.setRightAnchor(panelMapa, 0.0);

        slider.getChildren().add(panelMapa);
        panelMapa.toBack();

        Platform.runLater(() -> {
                panelMapa.init();
                panelMapa.setAutomaticLayout(true);
        });
    }

    public void setMenu(){
        mainMenu.setTranslateX(-(mainMenu.getPrefWidth() + 12));
        menuManager.setVisible(true);
        menuManager.setOnMouseClicked(event -> showMenu());
        menuManagerInside.setOnMouseClicked(event -> showMenu());
    }

    private void showMenu() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(mainMenu);

        if (isMenuOpen) {
            slide.setToX(-(mainMenu.getPrefWidth() + 12));
            isMenuOpen = false;
            menuManager.setVisible(true);
        } else {
            slide.setToX(0);
            isMenuOpen = true;
            menuManager.setVisible(false);
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
        sincronizarGrafo();
        panelMapa.update();
        panelMapa.setAutomaticLayout(true);
    }

    private void sincronizarGrafo() {
        var mapaBackend = Grafo.getInstance().getMap();

        for (Parada p : mapaBackend.keySet()) {
            try { grafoVisual.insertVertex(p); } catch (Exception ignored) {}
        }
        for (Parada origen : mapaBackend.keySet()) {
            for (Ruta r : mapaBackend.get(origen)) {
                try { grafoVisual.insertEdge(origen, r.getDestino(), r); } catch (Exception ignored) {}
            }
        }
    }
}