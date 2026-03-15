package com.rutas.redtransporte.utilidad;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Mensaje {
    /* Proposito: Mostrar las posibles opciones de mensaje para las ventanas.
     */
    public enum OpcionMensaje {
        EMPTY,
        EXISTING,
        SAVED,
        MODIFIED,
        DELETE;
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
    public static void defaultMessages(Mensaje.OpcionMensaje error, String text){
        switch (error){
            case Mensaje.OpcionMensaje.EMPTY -> showMessage(Alert.AlertType.ERROR,"Error","Campos vacíos.","Debe completar todos los datos.");
            case Mensaje.OpcionMensaje.EXISTING -> showMessage(Alert.AlertType.ERROR,"Error",text,"");//"Dato existente."
            case Mensaje.OpcionMensaje.SAVED -> showMessage(Alert.AlertType.INFORMATION,"Registro","","Registro existoso.");
            case Mensaje.OpcionMensaje.MODIFIED -> showMessage(Alert.AlertType.INFORMATION,"Modificación","","Modificación existosa.");
            case Mensaje.OpcionMensaje.DELETE -> manageResponse(showMessage(Alert.AlertType.CONFIRMATION,"Eliminar","¿Desea eliminar este elemento?",text));
        }
    }
}
