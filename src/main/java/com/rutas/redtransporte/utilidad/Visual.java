package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.application.Principal;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Visual {

    /* Nombre: openNewWindow
       Funcion: Abrir una ventana sobre la ventana principal.
       Retorno: void
    */
   public static FXMLLoader openNewWindow(String fxml, String css){

       try{
           Stage stage = new Stage();

           stage.initOwner(Principal.mainStage);
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

   public static void changePossition(Node node, double x, double y){
       node.setLayoutX(x);
       node.setLayoutX(y);
   }

}
