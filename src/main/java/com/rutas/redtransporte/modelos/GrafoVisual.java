package com.rutas.redtransporte.modelos;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartRandomPlacementStrategy;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

import java.net.URI;
import java.util.Objects;

public class GrafoPanel{

    private SmartGraphPanel<Parada, Ruta> panelMapa;

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

    private Vertex<Parada> getVertex(Parada parada){
        return grafoVisual.vertices()
                .stream()
                .filter(vertex -> vertex.element().equals(parada))
                .findFirst()
                .orElse(null);

    }

    private Edge<Ruta,Parada> getEdge(Ruta ruta){
        return grafoVisual.edges()
                .stream()
                .filter(edge -> edge.element().equals(ruta))
                .findFirst()
                .orElse(null);
    }
}
