package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.GrafoVisual;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;
import com.rutas.redtransporte.servicios.ClaseService;
import com.rutas.redtransporte.servicios.RoutingEngine;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Visual;

import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController {

    @FXML
    private AnchorPane panelPrincipal;

    @FXML
    private Pane panelGrafo;

    @FXML
    private VBox mainMenu;

    @FXML
    private Pane panelFiltro;

    @FXML
    private Pane panelDatos;

    @FXML
    private ImageView menuManager;

    @FXML
    private ImageView menuManagerInside;

    @FXML
    private Button btnDistancia;

    @FXML
    private Button btnTiempo;

    @FXML
    private Button btnCosto;

    @FXML
    private Button btnTransbordo;

    @FXML private Label lblDescripcionOptima;
    @FXML private Label lblDistanciaOptima;
    @FXML private Label lblTiempoOptimo;
    @FXML private Label lblCostoOptimo;
    @FXML private Label lblTransbordoOptimo;

    @FXML private Label lblDescripcionAlterna;
    @FXML private Label lblDistanciaAlterna;
    @FXML private Label lblTiempoAlterno;
    @FXML private Label lblCostoAlterno;
    @FXML private Label lblTransbordoAlterno;

    @FXML private Label lblOrigen;
    @FXML private Label lblDestino;
    @FXML private Pane dotOrigen;
    @FXML private Pane dotDestino;
    @FXML private Button btnCalcular;

    @FXML private Pane panelSimulador;
    @FXML private Label lblRutaSimulada;
    @FXML private ToggleGroup toggleEvento;
    @FXML private ToggleButton btnTrafico;
    @FXML private ToggleButton btnAccidente;
    @FXML private ToggleButton btnLluvia;
    @FXML private ToggleButton btnDescuento;

    @FXML
    private ImageView mapImage;

    @FXML
    private AnchorPane graphContainer;

    private Parada paradaOrigen;
    private Parada paradaDestino;

    private AtomicBoolean isMenuOpen = new AtomicBoolean(true);

    private final GrafoVisual grafoVisual = new GrafoVisual();
    private final RoutingEngine routingEngine = new RoutingEngine();

    public void initialize() {
        ClaseService.getInstance().registrarClase(MainController.class,this);
        Logico.crearDatosGrafo();
        setMenu();
        setButtonValues();
        grafoVisual.inicializar(graphContainer, this);

        mapImage.fitWidthProperty().bind(panelPrincipal.widthProperty());
        mapImage.fitHeightProperty().bind(panelPrincipal.heightProperty());
    }

    public MainController getMainController(){
        return this;
    }

    public GrafoVisual getGrafoVisual(){
        return grafoVisual;
    }

    /* Nombre: setMenu
        Funcion: Configuraciones iniciales para el menu.
        Retorno: void.
    */
    public void setMenu(){
        menuManager.setOnMouseClicked(event -> setMenuTranslations());
        menuManagerInside.setOnMouseClicked(event -> setMenuTranslations());
}

    private void setMenuTranslations(){
        ParallelTransition transitions = new ParallelTransition();

        boolean opening = isMenuOpen.get();

        transitions.getChildren().addAll(
                Visual.createTranslation(mainMenu, opening, Visual.Axis.X,-1),
                Visual.createTranslation(panelFiltro, opening, Visual.Axis.Y,-1),
                Visual.createTranslation(panelDatos, opening, Visual.Axis.Y,1)
        );

        menuManager.setVisible(opening);
        isMenuOpen.set(!opening);

        transitions.play();
    }

    /* Nombre: menuActions
   Funcion: Maneja las acciones del menu.
   Retorno: void.
    */
    public void menuActions(ActionEvent e){
        String id = ((Button) e.getSource()).getId();

        switch (id){
            case "crearParada" -> {
                Visual.Ventana<CrearParadaController> crearParada = Visual.prepararVentana("CrearParada.fxml", "Estilo.css");
                crearParada.stage.show();
            }
            case "mostrarParada" -> {
                Visual.Ventana<ShowParadaController> showParada = Visual.prepararVentana("ShowParada.fxml", "Estilo.css");
                showParada.stage.show();
            }
            case "crearRuta" -> {
                Visual.Ventana<CrearRutaController> crearRuta = Visual.prepararVentana("CrearRuta.fxml", "Estilo.css");
                crearRuta.stage.show();
            }
            case "mostrarRuta" -> {
                Visual.Ventana<ShowRutaController> showRuta = Visual.prepararVentana("ShowRuta.fxml", "Estilo.css");
                showRuta.stage.show();
            }
        }
    }

    public void seleccionCriterio(ActionEvent e){
        Button criterioSelected = (Button) e.getSource();

        btnDistancia.getStyleClass().remove("btnFiltroActivo");
        btnTiempo.getStyleClass().remove("btnFiltroActivo");
        btnCosto.getStyleClass().remove("btnFiltroActivo");
        btnTransbordo.getStyleClass().remove("btnFiltroActivo");

        criterioSelected.getStyleClass().add("btnFiltroActivo");

        grafoVisual.setCriterio((Ruta.Peso) criterioSelected.getUserData());
    }

    public void setButtonValues(){
        btnDistancia.setUserData(Ruta.Peso.DISTANCIA);
        btnTiempo.setUserData(Ruta.Peso.TIEMPO);
        btnCosto.setUserData(Ruta.Peso.COSTO);
        btnTransbordo.setUserData(Ruta.Peso.TRANSBORDO);
        btnTrafico.setUserData(Ruta.Evento.TRAFICO);
        btnAccidente.setUserData(Ruta.Evento.ACCIDENTE);
        btnLluvia.setUserData(Ruta.Evento.LLUVIA);
        btnDescuento.setUserData(Ruta.Evento.DESCUENTO);
    }

    public void mostrarResultados(ShortestPath optima, ShortestPath alterna) {
        if (optima != null) {
            actualizarPanelRuta(optima, lblDescripcionOptima, lblDistanciaOptima,
                    lblTiempoOptimo, lblCostoOptimo, lblTransbordoOptimo);
        } else {
            limpiarPanelRuta(lblDescripcionOptima, lblDistanciaOptima, lblTiempoOptimo, lblCostoOptimo, lblTransbordoOptimo);
        }

        if (alterna != null) {
            actualizarPanelRuta(alterna, lblDescripcionAlterna, lblDistanciaAlterna,
                    lblTiempoAlterno, lblCostoAlterno, lblTransbordoAlterno);
        } else {
            limpiarPanelRuta(lblDescripcionAlterna, lblDistanciaAlterna, lblTiempoAlterno, lblCostoAlterno, lblTransbordoAlterno);
        }
    }

    private void actualizarPanelRuta(ShortestPath path, Label desc, Label dist, Label tiempo, Label costo, Label trans) {
        List<Ruta> rutas = path.getRutasRecorridas();
        if (rutas == null || rutas.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        sb.append(rutas.get(0).getOrigen().getNombreParada());
        for (Ruta r : rutas) {
            sb.append(" → ").append(r.getDestino().getNombreParada());
        }

        desc.setText(sb.toString());
        dist.setText(Logico.limitarDecimales(path.getTotalDistance()) + " km");
        tiempo.setText(Logico.limitarDecimales(path.getTotalTime()) + " h");
        costo.setText("$ " + Logico.limitarDecimales(path.getTotalPrice()));
        trans.setText(String.valueOf(path.getTotalTranfers()));
    }
    private void limpiarPanelRuta(Label desc, Label dist, Label tiempo, Label costo, Label trans) {
        desc.setText("Ruta no disponible");
        dist.setText("—");
        tiempo.setText("—");
        costo.setText("—");
        trans.setText("—");
    }

    public void setOrigen(Parada parada) {
        this.paradaOrigen = parada;
        lblOrigen.setText(parada.getNombreParada());
        dotOrigen.setStyle("-fx-background-color:#22c55e; -fx-background-radius:5;");
        lblDestino.setText("Seleccionar...");
        this.paradaDestino = null;
    }

    public void setDestino(Parada parada) {
        this.paradaDestino = parada;
        lblDestino.setText(parada.getNombreParada());
    }

    public void calcularRuta(ActionEvent e) {
        if(esEntradaValida()){
            if (routingEngine.esGrafoConexo()) {
                Mensaje.showMessage(Alert.AlertType.ERROR, "Calculo Bloqueado", "El grafo no es conexo.", "Una los nodos aislados");
                mostrarResultados(null, null);
                grafoVisual.colorearRutas(null, null);
                grafoVisual.limpiarSeleccion();
                return;
            }
            grafoVisual.calcularDesdeController(paradaOrigen, paradaDestino);
        }
    }

    private boolean esEntradaValida() {
        String nombreOrigen = lblOrigen.getText();
        String nombreDestino = lblDestino.getText();

        if (nombreOrigen.equals("Seleccionar...") || nombreDestino.equals("Seleccionar...")
                || paradaOrigen == null || paradaDestino == null) {

            Mensaje.showMessage(Alert.AlertType.WARNING, "Datos incompletos", null,
                    "Por favor, seleccione un punto de origen y un destino en el mapa.");
            return false;
        }

        if (nombreOrigen.equals(nombreDestino)) {
            Mensaje.showMessage(Alert.AlertType.WARNING, "Ruta inválida", null,
                    "El punto de origen y destino no pueden ser el mismo.");
            return false;
        }

        return true;
    }

    public void limpiarSeleccionEliminada(Parada parada) {
        if (paradaOrigen != null && paradaOrigen.equals(parada)) {
            paradaOrigen = null;
            lblOrigen.setText("Seleccionar...");
            dotOrigen.setStyle("-fx-background-color:#ccc; -fx-background-radius:5;");
        }
        if (paradaDestino != null && paradaDestino.equals(parada)) {
            paradaDestino = null;
            lblDestino.setText("Seleccionar...");
        }
        grafoVisual.limpiarSeleccion();
    }

    public void onRutaSeleccionada(Ruta ruta) {
        lblRutaSimulada.setText(ruta.getOrigen().getNombreParada() + " → " + ruta.getDestino().getNombreParada());
    }

    public void aplicarEvento() {
        Toggle selected = toggleEvento.getSelectedToggle();
        if (selected == null) {
            Mensaje.showMessage(Alert.AlertType.WARNING, "Sin selección", null, "Selecciona un tipo de evento.");
            return;
        }
        if (grafoVisual.getRutaSelected() == null) {
            Mensaje.showMessage(Alert.AlertType.WARNING, "Sin ruta", null, "Haz doble clic en una ruta del mapa.");
            return;
        }

        Ruta.Evento evento = (Ruta.Evento) selected.getUserData();
        grafoVisual.aplicarEventoArista(evento);

        String descripcion = switch (evento) {
            case TRAFICO   -> "Tiempo y costo incrementados un 50%. La ruta sigue disponible.";
            case ACCIDENTE -> "Ruta bloqueada. Los algoritmos la ignoraran al calcular caminos.";
            case LLUVIA    -> "Tiempo y costo incrementados un 25%. La ruta sigue disponible.";
            case DESCUENTO -> "Descuento del 50%. El costo ha bajado.";
            default        -> "";
        };

        Mensaje.showMessage(Alert.AlertType.INFORMATION, "Evento aplicado", "Evento: " + evento.name(), descripcion);
        if (paradaOrigen != null && paradaDestino != null) {
            calcularRuta(null);
        }
    }

    public void limpiarSimulacion() {
        grafoVisual.limpiarEventoArista();
        toggleEvento.selectToggle(null);
        lblRutaSimulada.setText("Ninguna seleccionada");
    }
}