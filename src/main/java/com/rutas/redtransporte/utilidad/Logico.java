package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Logico {

    /* Nombre: crearDatosGrafo
             Funcion: Crear datos iniciales para generar Grafo.
             Retorno: void.
         */
    public static void crearDatosGrafo(){

        Grafo grafo = Grafo.getInstance();
        Random random = new Random();

        List<Parada> paradas = crearParadas(grafo, random);
        crearRutas(grafo, random, paradas);

    }

    /* Nombre: crearParadas
             Funcion: Crear paradas iniciales.
             Retorno: void.
         */
    private static List<Parada> crearParadas(Grafo grafo, Random random){

        List<Parada> paradas = new ArrayList<>();

        String[] transporte = {"Carro", "Bus", "Monorriel"};

        for (char c = 'A'; c <= 'G'; c++) {
            String nombre = String.valueOf(c);
            String tipo = transporte[random.nextInt(transporte.length)];

            Parada parada = new Parada(nombre, tipo);
            grafo.addParada(parada);
            paradas.add(parada);
        }

        return paradas;
    }

    /* Nombre: crearRutas
             Funcion: Crear rutas iniciales.
             Retorno: void.
         */
    private static void crearRutas(Grafo grafo, Random random, List<Parada> paradas){
        String[] nombreRutas = {
                "first", "second", "third", "fourth",
                "fifth", "sixth", "seventh"
        };

        for (String nombre : nombreRutas) {

            Parada origen = paradas.get(random.nextInt(paradas.size()));
            Parada destino = paradas.get(random.nextInt(paradas.size()));

            while (origen == destino) {
                destino = paradas.get(random.nextInt(paradas.size()));
            }

            double distancia = limitarDecimales(1 + random.nextDouble() * (200 - 1));
            double costo = limitarDecimales(1 + random.nextDouble() * (500 - 1)); // 1–200
            double tiempo = limitarDecimales(1 + random.nextDouble() * (30 - 1));

            grafo.addRoute(new Ruta(nombre, origen, destino, costo, tiempo, distancia));
        }
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
