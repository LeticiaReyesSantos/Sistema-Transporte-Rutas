package com.rutas.redtransporte.controllers;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;
import com.rutas.redtransporte.servicios.ClaseService;
import com.rutas.redtransporte.servicios.ParadaService;
import com.rutas.redtransporte.servicios.RoutingEngine;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.net.URI;
import java.util.Objects;

public class GrafoVisual {

    private SmartGraphPanel<Parada, Ruta> panelMapa;
    private Digraph<Parada, Ruta> grafoVisual;
    private AnchorPane panelPrincipal;
    private RoutingEngine routingEngine;
    private MainController mainController;

    private SmartGraphVertex<Parada> vertexSelected = null;
    private Ruta rutaSelected = null;
    private Ruta.Peso criterio = Ruta.Peso.DISTANCIA;

    private ContextMenu menuParada;

    public Ruta getRutaSelected() {
        return rutaSelected;
    }

    public void inicializar(AnchorPane panelPrincipal){
        ClaseService.getInstance().registrarClase(GrafoVisual.class,this);
        mainController = ClaseService.getInstance().getClase(MainController.class);
        this.panelPrincipal = panelPrincipal;

        iniciarMapa();
        iniciarMenu();
        setNodeInteraction();
    }

    private void iniciarMapa() {
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
        panelMapa.setStyle("-fx-background-color: transparent;");

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

    private void iniciarMenu() {
        menuParada = new ContextMenu();
        menuParada.getStyleClass().add("btnParadaMenu");

        MenuItem editItem = new MenuItem("Editar");
        editItem.getStyleClass().add("btnParadaMenu");
        editItem.setGraphic(Visual.setIcon("/imagenes/modificar.png"));

        MenuItem deleteItem = new MenuItem("Eliminar");
        deleteItem.getStyleClass().add("btnParadaMenu");
        deleteItem.setGraphic(Visual.setIcon("/imagenes/eliminar.png"));

        editItem.setOnAction(event -> {
            ParadaService paradaService = new ParadaService();
            Vertex<Parada> actualVertex = (Vertex<Parada>) menuParada.getUserData();
            paradaService.verDetalles(actualVertex.element());
        });

        deleteItem.setOnAction(event -> {
            Vertex<Parada> actualVertex = (Vertex<Parada>) menuParada.getUserData();
            mainController.eliminarParadaDesdeMapa(actualVertex.element());
        });

        menuParada.getItems().addAll(editItem, deleteItem);
    }

    public void sincronizarGrafo() {
        var mapaBackend = Grafo.getInstance().getMap();

        // Se usa try-catch porque SmartGraph lanza excepciones si el nodo o arista ya existe
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

    public void actualizarMapa() {
        sincronizarGrafo();
        panelMapa.update();
        panelMapa.setAutomaticLayout(true);
    }

    private void setNodeInteraction() {
        panelMapa.setVertexDoubleClickAction(graphVertex -> Platform.runLater(() -> {
            try {
                SmartGraphVertexNode<Parada> smartVertex = (SmartGraphVertexNode<Parada>) graphVertex;
                Vertex<Parada> vertex = smartVertex.getUnderlyingVertex();
                menuParada.setUserData(vertex);

                javafx.geometry.Bounds bounds = smartVertex.localToScreen(smartVertex.getBoundsInLocal());

                if (bounds != null) {
                    double centerX = bounds.getMinX() + (bounds.getWidth() / 2);
                    double centerY = bounds.getMinY() + (bounds.getHeight() / 2);

                    menuParada.show(smartVertex.getScene().getWindow(), centerX + 10, centerY);
                }
            }catch (ClassCastException e) {
                System.err.println("Error: SmartGraph no devolvió un Node visual esperado.");
            }
        }));

        panelMapa.setEdgeDoubleClickAction(graphEdge -> {
            rutaSelected = graphEdge.getUnderlyingEdge().element();

            for (Edge<Ruta, Parada> edge : grafoVisual.edges()) {
                String css = edge.element().equals(rutaSelected) ? "edge-seleccionada" : "edge-default";
                panelMapa.getStylableEdge(edge).setStyleClass(css);
            }

            mainController.onRutaSeleccionada(rutaSelected);
        });
    }

    private void setVertexSelection(){
        for (SmartGraphVertex<Parada> currentVertex : panelMapa.getSmartVertices()) {
            Node node = (Node) currentVertex;

            node.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {

                    for (SmartGraphVertex<Parada> vertex : panelMapa.getSmartVertices()) {
                        if(vertex.equals(vertexSelected)) continue;
                        vertex.setStyleClass("vertex");
                    }

                    currentVertex.setStyleClass("vertex-selected");

                    if(vertexSelected == null){
                        vertexSelected = currentVertex;
                        mainController.setOrigen(currentVertex.getUnderlyingVertex().element());
                    } else {
                        mainController.setDestino(currentVertex.getUnderlyingVertex().element());
                        getOptimizedPath(vertexSelected, currentVertex);
                        vertexSelected = null;
                    }

                    event.consume();
                }
            });
        }
    }

    public void limpiarSeleccion() {
        this.vertexSelected = null;

        for (SmartGraphVertex<Parada> vertex : panelMapa.getSmartVertices()) {
            vertex.setStyleClass("vertex");
        }
    }

    public void crearParada(Parada parada){
        grafoVisual.insertVertex(parada);
        panelMapa.update();
        Platform.runLater(this::setVertexSelection);
    }

    /*
    Los vertex tienen una referencia a la parada,
    por lo que simplemente se debe actualizar el mapa.
     */
    public void modificarParada(){
        panelMapa.update();
    }

    public void eliminarParada(Parada parada){
        Vertex<Parada> vertex = getVertex(parada);

        if(vertex != null){
            grafoVisual.removeVertex(vertex);
            panelMapa.update();
        }
    }

    public void crearRuta(Ruta ruta){
        insertEdge(ruta);
        panelMapa.update();
        Platform.runLater(this::setVertexSelection);
    }

    public void modificarRuta(Ruta ruta){
        eliminarRuta(ruta);
        crearRuta(ruta);
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
                .filter(edge -> edge.element().getIdRuta() == rutaBuscada.getIdRuta())
                .findFirst()
                .orElse(null);
    }

    public Edge<Ruta,Parada> newEdge(Ruta ruta){
        return new Edge<>() {
            @Override
            public Ruta element() {
                return ruta;
            }

            @Override
            public Vertex<Parada>[] vertices() {
                Vertex<Parada>[] paradas = new Vertex[2];
                paradas[0] = newVertex(ruta.getOrigen());
                paradas[1] = newVertex(ruta.getDestino());

                return paradas;
            }
        };
    }

    public Vertex<Parada> getVertex(Parada parada){
        return grafoVisual.vertices()
                .stream()
                .filter(vertex -> vertex.element().getIdParada() == parada.getIdParada())
                .findFirst()
                .orElse(null);
    }

    public Vertex<Parada> newVertex(Parada parada){
        return () -> parada;
    }

    public void setCriterio(Ruta.Peso criterio){
        this.criterio = criterio;
    }

    public void calcularDesdeController(Parada origen, Parada destino) {
        SmartGraphVertex<Parada> vOrigen = null, vDestino = null;
        for (SmartGraphVertex<Parada> v : panelMapa.getSmartVertices()) {
            if (v.getUnderlyingVertex().element().equals(origen)) vOrigen = v;
            if (v.getUnderlyingVertex().element().equals(destino)) vDestino = v;
        }
        if (vOrigen != null && vDestino != null)
            getOptimizedPath(vOrigen, vDestino);
    }

    public void getOptimizedPath(SmartGraphVertex<Parada>  vertexOrigen, SmartGraphVertex<Parada>  vertexDestino){
        if (routingEngine.esGrafoConexo()) {
            mainController.mostrarResultados(null, null);
            colorearRutas(null, null);
            return;
        }

        Parada origen = vertexOrigen.getUnderlyingVertex().element();
        Parada destino = vertexDestino.getUnderlyingVertex().element();

        ShortestPath rutaPrincipal = routingEngine.optimizedPath(origen,destino,criterio);
        ShortestPath rutaAlternativa = routingEngine.alternativePath(origen,destino,criterio);

        mainController.mostrarResultados(rutaPrincipal,rutaAlternativa);
        colorearRutas(rutaPrincipal, rutaAlternativa);
    }

    public void colorearRutas(ShortestPath principal, ShortestPath alternativa) {
        for (Edge<Ruta, Parada> edge : grafoVisual.edges()) {
            Ruta ruta = edge.element();
            if (ruta.getEventoTrafico() != Ruta.Evento.STANDARD) {
                colorearSimulacionEventos();
            } else {
                panelMapa.getStylableEdge(edge).setStyleClass("edge-default");
            }
        }

        if (alternativa != null && alternativa.getRutasRecorridas() != null) {
            for (Ruta r : alternativa.getRutasRecorridas()) {
                Edge<Ruta, Parada> e = getEdge(r);
                if (e != null) panelMapa.getStylableEdge(e).setStyleClass("edge-alternativa");
            }
        }
        if (principal != null && principal.getRutasRecorridas() != null) {
            for (Ruta r : principal.getRutasRecorridas()) {
                Edge<Ruta, Parada> e = getEdge(r);
                if (e != null) panelMapa.getStylableEdge(e).setStyleClass("edge-principal");
            }
        }
    }

    public void colorearSimulacionEventos() {
        for (Edge<Ruta, Parada> edge : grafoVisual.edges()) {
            Ruta ruta = edge.element();
            String cssClass = "edge-default";

            switch (ruta.getEventoTrafico()) {
                case TRAFICO -> cssClass = "edge-trafico";
                case LLUVIA -> cssClass = "edge-lluvia";
                case ACCIDENTE -> cssClass = "edge-accidente";
                case DESCUENTO -> cssClass = "edge-descuento";
                case STANDARD -> cssClass = "edge-default";
            }
            panelMapa.getStylableEdge(edge).setStyleClass(cssClass);
        }
    }

    public void aplicarEventoArista(Ruta.Evento evento) {
        if (rutaSelected == null) return;
        Grafo.getInstance().aplicarEvento(rutaSelected, evento);
        colorearSimulacionEventos();
    }

    public void limpiarEventoArista() {
        if (rutaSelected == null) return;
        Grafo.getInstance().aplicarEvento(rutaSelected, Ruta.Evento.STANDARD);
        colorearSimulacionEventos();
        rutaSelected = null;

        for (Edge<Ruta, Parada> edge : grafoVisual.edges()) {
            panelMapa.getStylableEdge(edge).setStyleClass("edge-default");
        }
    }
}