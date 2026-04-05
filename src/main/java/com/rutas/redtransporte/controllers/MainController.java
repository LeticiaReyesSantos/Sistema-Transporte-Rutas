package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.GrafoVisual;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;
import com.rutas.redtransporte.servicios.ClaseService;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Visual;

import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;
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

    @FXML
    private ImageView mapImage;

    @FXML
    private AnchorPane graphContainer;

    private Parada paradaOrigen;
    private Parada paradaDestino;

    private AtomicBoolean isMenuOpen = new AtomicBoolean(true);

    private final GrafoVisual grafoVisual = new GrafoVisual();

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
                FXMLLoader loader = Visual.openNewWindow("CrearParada.fxml","Estilo.css");
                CrearParadaController ctrl = loader.getController();
            }
            case "mostrarParada" -> Visual.openNewWindow("ShowParada.fxml","Estilo.css");
            case "crearRuta" -> {
                FXMLLoader loader = Visual.openNewWindow("CrearRuta.fxml","Estilo.css");
                CrearRutaController ctrl = loader.getController();
                ctrl.setMainController(this);
            }
            case "mostrarRuta" -> Visual.openNewWindow("ShowRuta.fxml","Estilo.css");
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
    }

    public void mostrarResultados(ShortestPath optima, ShortestPath alterna) {
        if (optima != null) {
            actualizarCajaRuta(optima, lblDescripcionOptima, lblDistanciaOptima,
                    lblTiempoOptimo, lblCostoOptimo, lblTransbordoOptimo);
        }

        if (alterna != null) {
            actualizarCajaRuta(alterna, lblDescripcionAlterna, lblDistanciaAlterna,
                    lblTiempoAlterno, lblCostoAlterno, lblTransbordoAlterno);
        } else {
            limpiarCajaRuta(lblDescripcionAlterna, lblDistanciaAlterna, lblTiempoAlterno, lblCostoAlterno, lblTransbordoAlterno);
        }
    }

    private void actualizarCajaRuta(ShortestPath path, Label desc, Label dist, Label tiempo, Label costo, Label trans) {
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
    private void limpiarCajaRuta(Label desc, Label dist, Label tiempo, Label costo, Label trans) {
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
}