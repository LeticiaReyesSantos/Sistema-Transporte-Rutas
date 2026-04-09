package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.modelos.Grafo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Logico {

    public static void crearDatosGrafo(){

        Grafo grafo = Grafo.getInstance();
        grafo.cargarDesdeDB();

    }

    public static double limitarDecimales(double numero){
        return Math.round(numero * 100.0) / 100.0;
    }

    /* Nombre: checkText
          Funcion: Revisar si el texto está vacío.
          Retorno: String (El texto obtenido del textField).
      */
    public static String checkText(TextField txtField){
        String txt = txtField.getText().trim();

        if(txt.isEmpty())
            Mensaje.defaultMessages(Mensaje.OpcionMensaje.EMPTY,"");
        return txt;
    }

    /* Nombre: inputIsNumeric
              Funcion: Revisar que los strings recibido sea un número.
              Retorno: double (Número obtenido).
          */
    public static boolean inputIsNumeric(String... values){
        double numero = 0;

        for (String value : values) {
            try{
                numero = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                Mensaje.showMessage(Alert.AlertType.ERROR,"Error","Dato inválido.","Escriba un valor númerico.");
            }

            if(numero < 1)
                return false;
        }

        return true;
    }

    /* Nombre: emptyFields
              Funcion: Revisar que un número variable de campos no estén vacíos.
              Retorno: boolean(true si los campos están vacíos).
          */
    public static boolean emptyFields(Node... components){
        for (Node component : components) {
            if(component instanceof TextInputControl txt){
                if(txt.getText().isEmpty())
                    return true;

            }else if (component instanceof ComboBox<?> cbx) {
                if(cbx.getValue() == null)
                    return true;

            }else if (component instanceof CheckBox checkBox) {
                if(!checkBox.isSelected()){
                    return true;
                }
            }
        }
        return false;
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

    /* Nombre: getAttributes
        Funcion: Crear una ObservableList para mostrar las opciones en comboBox.

                 Utiliza un stream que recorre la estructura de datos enviada, conviertiendo
                 cada elemento según indica la  función mapper.

        Retorno: ObservableList.
    */
    public static <T> ObservableList<String> getAttributes(Collection<T> data, Function<T,String> mapper){
        List<String> list = data.stream()
                .map(mapper)
                .collect(Collectors.toList());

        return FXCollections.observableArrayList(list);
    }
}
