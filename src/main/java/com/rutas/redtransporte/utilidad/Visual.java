package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.application.MainVisual;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Visual {

    /* Nombre: openNewWindow
       Funcion: Abrir una ventana sobre la ventana principal.
       Retorno: void
    */
   public static FXMLLoader openNewWindow(String fxml, String css, boolean wait){

       try{
           Stage stage = new Stage();

           stage.initOwner(MainVisual.mainStage);
           stage.initModality(Modality.APPLICATION_MODAL);

           FXMLLoader fxmlLoader = new FXMLLoader(Visual.class.getResource("/appvisuals/"+fxml));
           Scene scene = new Scene(fxmlLoader.load());

           if(css != null)
               scene.getStylesheets().add(Visual.class.getResource("/appvisuals/"+css).toExternalForm());

           stage.setScene(scene);
           stage.show();

           return fxmlLoader;

       }catch (IOException e) {
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

    private static void setNodesVisibility(Node[] nodes, boolean visibility){
        for (Node node : nodes) {
            node.setVisible(visibility);
        }
    }

}
