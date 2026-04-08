package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.application.MainVisual;
import com.rutas.redtransporte.controllers.CrearParadaController;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.security.Principal;

public class Visual {
    /*
    Esta clase representa las ventanas que se crean en el programa.
    Mantiene acceso separado al controller y al stage de una ventana.
     */
    public static class Ventana<T> {
        public final T controller;
        public final Stage stage;

        public Ventana(T controller, Stage stage) {
            this.controller = controller;
            this.stage = stage;
        }
    }

    /* Nombre: prepararVentana
   Funcion: Se encarga la crear la ventana.
   Retorno: Ventana<T>
    */
    public static <T> Ventana<T> prepararVentana(String fxml, String css) {
        try {
            FXMLLoader loader = new FXMLLoader(Visual.class.getResource("/appvisuals/" + fxml));
            Scene scene = new Scene(loader.load());
            if (css != null)
                scene.getStylesheets().add(Visual.class.getResource("/appvisuals/" + css).toExternalForm());

            Stage stage = new Stage();
            stage.initOwner(MainVisual.mainStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);

            return new Ventana<>(loader.getController(), stage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /* Nombre: closeWindow
         Funcion: Cerrar una ventana.
         Retorno: void.
     */
   public static void closeWindow(ActionEvent e){
       Stage stage = (Stage)(((Node)e.getSource()).getScene().getWindow());
       stage.close();

   }

    public static void closeWindow(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public static FXMLLoader getLoader(String fxml){
        FXMLLoader loader = new FXMLLoader(Visual.class.getResource("/appvisuals/"+fxml));
        return loader;
    }

    public enum Axis { X, Y };

    /* Nombre: showMenu
         Funcion: Mostrar y ocultar el menu.
         Retorno: void.
     */
    public static TranslateTransition createTranslation(Region region, boolean isCurrentlyOpen, Axis axis, double sign) {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), region);

        double size = (axis == Axis.X) ? region.getPrefWidth() + 20 : region.getPrefHeight() + 50;
        double target = (isCurrentlyOpen ? (size * sign) : 0);

        if (axis == Axis.X) slide.setToX(target);
        else slide.setToY(target);

        return slide;
    }

    public static ImageView setIcon(String path){
        Image image = new Image(Visual.class.getResourceAsStream(path));

        ImageView imageView = new ImageView(image);
        imageView.setImage(image);
        imageView.setPreserveRatio(true);

        imageView.setFitWidth(15);
        imageView.setFitHeight(15);

        return imageView;
    }

}
