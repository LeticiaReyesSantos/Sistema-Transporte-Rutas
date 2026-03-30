package com.rutas.redtransporte.modelos;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import com.rutas.redtransporte.servicios.RoutingEngine;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URI;
import java.util.Objects;

public class GrafoVisual {

    private SmartGraphPanel<Parada, Ruta> panelMapa;
    private Digraph<Parada, Ruta> grafoVisual;
    private RoutingEngine routingEngine;

    private SmartGraphVertex<Parada> vertexSelected = null;
    private Ruta.Peso criterio = Ruta.Peso.DISTANCIA;

    public void iniciarMapa(AnchorPane panelPrincipal) {
        routingEngine = new RoutingEngine();
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

        panelPrincipal.getChildren().add(panelMapa);
        panelMapa.toBack();

        Platform.runLater(() -> {
            panelMapa.init();
            panelMapa.setAutomaticLayout(true);
            setVertexSelection();
        });
    }

    private void setVertexSelection(){

        for (SmartGraphVertex<Parada> currentVertex : panelMapa.getSmartVertices()) {

            Node node = (Node) currentVertex;

            node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {

                    for (SmartGraphVertex<Parada> vertex : panelMapa.getSmartVertices()) {
                        if(vertex.equals(vertexSelected))
                            continue;

                        vertex.setStyleClass("vertex");
                    }

                    currentVertex.setStyleClass("vertex-selected");

                    if(vertexSelected ==  null){
                        vertexSelected = currentVertex;
                    }else{
                        getOptimizedPath(vertexSelected,currentVertex);
                        vertexSelected = null;
                    }


                    event.consume();
                }
            });
        }
    }

    public void getOptimizedPath(SmartGraphVertex<Parada>  vertexOrigen, SmartGraphVertex<Parada>  vertexDestino){
        Parada origen = vertexOrigen.getUnderlyingVertex().element();
        Parada destino = vertexDestino.getUnderlyingVertex().element();

        ShortestPath rutaPrincipal = routingEngine.optimizedPath(origen,destino,criterio);
        ShortestPath rutaAlternativa = routingEngine.alternativePath(origen,destino,criterio);

        colorearRutas(rutaPrincipal, rutaAlternativa);
    }

    public void setCriterio(Ruta.Peso criterio){
        this.criterio = criterio;
    }

    public void crearParada(Parada parada){
        grafoVisual.insertVertex(parada);
        panelMapa.update();
    }

    public void modificarParada(Parada oldParada, Parada newParada){
        Vertex<Parada> vertex = getVertex(oldParada);

        vertex = new Vertex<Parada>() {
            @Override
            public Parada element() {
                return newParada;
            }
        };

        panelMapa.update();
    }

    public void eliminarParada(Parada parada){
        grafoVisual.removeVertex(getVertex(parada));
        panelMapa.update();
    }

    public void crearRuta(Ruta ruta){
        insertEdge(ruta);
        panelMapa.update();
    }

    public void modificarRuta(Ruta oldRuta, Ruta newRuta){
        grafoVisual.removeEdge(getEdge(oldRuta));
        insertEdge(newRuta);
        panelMapa.update();

    }

    public void eliminarRuta(Ruta ruta){
        grafoVisual.removeEdge(getEdge(ruta));
        panelMapa.update();
    }

    public void insertEdge(Ruta ruta){
        grafoVisual.insertEdge(ruta.getOrigen(),ruta.getDestino(),ruta);
    }

    private Edge<Ruta,Parada> getEdge(Ruta rutaBuscada){
        return grafoVisual.edges()
                .stream()
                .filter(edge -> edge.element().equals(rutaBuscada))
                .findFirst()
                .orElse(null);
    }

    public Vertex<Parada> getVertex(Parada parada){
        return grafoVisual.vertices()
                .stream()
                .filter(vertex -> vertex.element().equals(parada))
                .findFirst()
                .orElse(null);

    }

    public void sincronizarGrafo() {
        var mapaBackend = Grafo.getInstance().getMap();

        for (Parada p : mapaBackend.keySet()) {
            try { grafoVisual.insertVertex(p); } catch (Exception ignored) {}
        }
        for (Parada origen : mapaBackend.keySet()) {
            for (Ruta r : mapaBackend.get(origen)) {

                try {
                    grafoVisual.insertEdge(origen, r.getDestino(), r);
                } catch (Exception ignored) {
                }
            }
        }
    }

    /* Nombre: resaltarRutas
       Objetivo: Cambia las clases CSS de las aristas en el SmartGraph
                para colorear el camino encontrado
    */
    public void colorearRutas(ShortestPath principal, ShortestPath alternativa){
        for (Edge<Ruta, Parada> edge : grafoVisual.edges()) {
            panelMapa.getStylableEdge(edge).setStyleClass("edge-default");
        }

        if (alternativa != null && alternativa.getRutasRecorridas() != null) {
            for (Ruta r : alternativa.getRutasRecorridas()) {
                Edge<Ruta, Parada> edgeGrafico = getEdge(r);
                if (edgeGrafico != null) {
                    panelMapa.getStylableEdge(edgeGrafico).setStyleClass("edge-alternativa");
                }
            }
        }
        if (principal != null && principal.getRutasRecorridas() != null) {
            for (Ruta r : principal.getRutasRecorridas()) {
                Edge<Ruta, Parada> edgeGrafico = getEdge(r);
                if (edgeGrafico != null) {
                    panelMapa.getStylableEdge(edgeGrafico).setStyleClass("edge-principal");
                }
            }
        }
    }
    public void actualizarMapa() {
        sincronizarGrafo();
        panelMapa.update();
        panelMapa.setAutomaticLayout(true);
    }
}
