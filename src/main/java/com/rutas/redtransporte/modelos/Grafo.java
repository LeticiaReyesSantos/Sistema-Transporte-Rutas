package com.rutas.redtransporte.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Clase: Grafo
   Proposito: Modelar grafos dirigidos utilizando HashMaps
   Metodos:///
 */

public class Grafo {
    private Map<Parada, List<Ruta>> map;

    public Grafo() {
        map = new HashMap<>();
    }

    public Map<Parada, List<Ruta>> getMap() {
        return map;
    }

    /* Nombre: addParada
       Funcion: agrega un nodo (parada) al grafo
       Retorno: void
     */
    public void addParada(Parada parada) {
        if (parada == null) {
            throw new IllegalArgumentException("Parada debe existir.");
        }
        map.putIfAbsent(parada, new ArrayList<>());
    }

    /* Nombre: addRoute
      Funcion: crea una arista que une los nodos (origen/destino) y agrega la ruta a la lista de rutas disp para esa parada
      Retorno: Ruta creada
    */
    public Ruta addRoute(Parada origen, Parada destino, String nombre, double tiempo, double costo, double distancia) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("El origen o destino de la ruta debe existir.");
        }

        map.putIfAbsent(origen, new ArrayList<>());
        map.putIfAbsent(destino, new ArrayList<>());
        Ruta newRoute = new Ruta(nombre, costo, tiempo, distancia, origen, destino);

        map.get(origen).add(newRoute);
        origen.addRoute(newRoute);

        return newRoute;
    }

}
