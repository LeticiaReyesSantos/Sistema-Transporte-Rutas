package com.rutas.redtransporte.controllers;

import com.brunomnsilva.smartgraph.graphview.*;
import com.rutas.redtransporte.modelos.GrafoVisual;
import com.rutas.redtransporte.utilidad.Logico;
import com.rutas.redtransporte.utilidad.Visual;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Objects;

public class MainController {

    @FXML
    private AnchorPane panelPrincipal;

    @FXML
    private ImageView menuManager;

    @FXML
    private ImageView menuManagerInside;

    @FXML
    private VBox mainMenu;

    private boolean isMenuOpen = false;

    private final GrafoVisual grafoVisual = new GrafoVisual();

    public void initialize() {
        Logico.crearDatosGrafo();
        setMenu();
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
        mainMenu.setTranslateX(-(mainMenu.getPrefWidth() + 12));
        menuManager.setVisible(true);
        menuManager.setOnMouseClicked(event -> showMenu());
        menuManagerInside.setOnMouseClicked(event -> showMenu());
    }

    /* Nombre: showMenu
        Funcion: Mostrar y ocultar el menu.
        Retorno: void.
    */
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
        FXMLLoader loader = Visual.openNewWindow(fxml, style);

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