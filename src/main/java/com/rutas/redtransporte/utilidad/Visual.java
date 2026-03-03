package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.application.Principal;
import com.rutas.redtransporte.modelos.Parada;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Visual {

    /* Nombre: openNewWindow
       Funcion: Abrir una ventana sobre la ventana principal.
       Retorno: void
    */
   public static void openNewWindow(String fxml, String css) throws IOException{
       Stage stage = new Stage();

       stage.initOwner(Principal.mainStage);
       stage.initModality(Modality.APPLICATION_MODAL);

       FXMLLoader fxmlLoader = new FXMLLoader(Visual.class.getResource("/appvisuals/"+fxml));
       Scene scene = new Scene(fxmlLoader.load());

       if(css != null)
           scene.getStylesheets().add(Visual.class.getResource("/appvisuals/"+css).toExternalForm());

       stage.setScene(scene);
       stage.show();
   }

    /* Nombre: closeWindow
         Funcion: Cerrar una ventana.
         Retorno: void.
     */
   public static void closeWindow(ActionEvent e){
       Stage stage = (Stage)(((Node)e.getSource()).getScene().getWindow());
       stage.close();
   }


    /* Nombre: showMessage
        Funcion: Mostrar mensajes en las ventanas.
        Retorno: Optional<ButtonType> (Respuesta a la ventana).
    */
   public static Optional<ButtonType> showMessage(Alert.AlertType alertType, String title, String headerText, String contentText){
       Alert alert = new Alert(alertType);
       alert.setTitle(title);
       alert.setHeaderText(headerText);
       alert.setContentText(contentText);

       return alert.showAndWait();
   }

    /* Nombre: manageResponse
         Funcion: Manejar la respuesta obtenida en una ventana de eliminación.
         Retorno: void.
     */
   public static void manageResponse(Optional<ButtonType> response){
       if (response.isPresent() && response.get() == ButtonType.OK){
           showMessage(Alert.AlertType.INFORMATION,"Eliminación","","El elemento ha sido eliminado.");
       }
   }

    /* Nombre: defaultMessages
          Funcion: Mostrar los mensajes más comunes para ventanas.
          Retorno: void.
      */
   public static void defaultMessages(OpcionMensaje error, String text){
       switch (error){
           case OpcionMensaje.EMPTY -> showMessage(Alert.AlertType.ERROR,"Error","Campos vacíos.","Debe completar la información.");
           case OpcionMensaje.EXISTING -> showMessage(Alert.AlertType.ERROR,"Error",text,"");//"Dato existente."
           case OpcionMensaje.SAVED -> showMessage(Alert.AlertType.INFORMATION,"Registro","","Registro existoso.");
           case OpcionMensaje.MODIFIED -> showMessage(Alert.AlertType.INFORMATION,"Modificación","","Modificación existosa.");
           case OpcionMensaje.DELETE -> manageResponse(showMessage(Alert.AlertType.CONFIRMATION,"Eliminar","","¿Desea eliminar este elemento?"));
       }
   }

    /* Nombre: checkText
           Funcion: Revisar si el texto está vacío.
           Retorno: String (El texto obtenido del textField).
       */
    public static String checkText(TextField txtField){
        String txt = txtField.getText().trim();

        if(txt.equals(""))
            defaultMessages(OpcionMensaje.EMPTY,"");
        return txt;
    }

    /* Nombre: cleanFields
           Funcion: Permite limipiar un número variable de campos.
           Retorno: void.
       */
    public static void cleanFields(Node... components){
        for (Node component : components) {
            if(component instanceof TextInputControl txt){
                txt.clear();

            }else if (component instanceof ComboBox<?> cbx) {
                cbx.getSelectionModel().clearSelection();
                cbx.setValue(null);

            }else if (component instanceof CheckBox checkBox) {
                checkBox.setSelected(false);
            }
        }
    }

}
