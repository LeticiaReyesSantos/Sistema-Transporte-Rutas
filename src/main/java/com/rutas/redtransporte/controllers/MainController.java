package com.rutas.redtransporte.controllers;

import com.rutas.redtransporte.modelos.GrafoVisual;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Visual;

import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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

    private AtomicBoolean isMenuOpen = new AtomicBoolean(false);

    private final GrafoVisual grafoVisual = new GrafoVisual();

    public void initialize() {
        Logico.crearDatosGrafo();
        setMenu();
        setButtonValues();
        grafoVisual.iniciarMapa(panelPrincipal);
    }

    public GrafoVisual getGrafoVisual(){
        return grafoVisual;
    }

    /* Nombre: setMenu
        Funcion: Configuraciones iniciales para el menu.
        Retorno: void.
    */
    public void setMenu(){

        mainMenu.setTranslateX(-(mainMenu.getPrefWidth() + 20));
        panelFiltro.setTranslateY(-(panelFiltro.getPrefHeight() + 50));
        panelDatos.setTranslateY((panelDatos.getPrefHeight() + 50));

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

    public void seleccionCriterio(ActionEvent e){
        Button criterioSelected = (Button)e.getSource();
        grafoVisual.setCriterio((Ruta.Peso) criterioSelected.getUserData());
    }

    public void setButtonValues(){
        btnDistancia.setUserData(Ruta.Peso.DISTANCIA);
        btnTiempo.setUserData(Ruta.Peso.TIEMPO);
        btnCosto.setUserData(Ruta.Peso.COSTO);
        btnTransbordo.setUserData(Ruta.Peso.TRANSBORDO);
    }

    /* Nombre: menuActions
       Funcion: Maneja las acciones del menu.
       Retorno: void.
   */
    public void menuActions(ActionEvent e){
        String id = ((Button) e.getSource()).getId();

        switch (id){
            case "crearParada" -> openWindow("CrearParada.fxml","Estilo.css");
            case "mostrarParada" -> openWindow("ShowParada.fxml","Estilo.css");
            case "crearRuta" -> openWindow("CrearRuta.fxml","Estilo.css");
            case "mostrarRuta" -> openWindow("ShowRuta.fxml","Estilo.css");
        }
    }

    /* Nombre: openWindow
       Funcion: Se encarga de abrir las ventanas y asginar la instancia de MainController.
       Retorno: void.
   */
    private void openWindow(String fxml, String style){
        FXMLLoader loader = Visual.openNewWindow(fxml, style,false);

        Object controller = (Objects.requireNonNull(loader).getController());

        if(controller instanceof CrearParadaController)
            ((CrearParadaController) controller).setMainController(this);

        else if(controller instanceof ShowParadaController)
            ((ShowParadaController) controller).setMainController(this);

        else if(controller instanceof CrearRutaController)
            ((CrearRutaController) controller).setMainController(this);

        else if(controller instanceof ShowRutaController)
            ((ShowRutaController) controller).setMainController(this);
    }

}