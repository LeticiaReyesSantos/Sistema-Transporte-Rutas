package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
    Patron de diseno: Strategy
    Define el contrato estandar para los algoritmos Bellman - Ford y Dijkstra.
    Se excluyo Floyd-Warshall de este contrato ya que el mismo trabaja con matriz de adyacencia en lugar de utilizar la logica
    origen-destino de Bellman y Dijkstra
 */
public interface EstrategiaDeRuta {

    ShortestPath bestRoute(Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio);

    //Se ha implementado como default para evitar la repeticion de codigo, obteniendo asi mas limpieza en los algoritmos que lo implementan
    //La logica de reconstruir el camino hacia atras es indentica en ambos
    default ShortestPath rebuildRoute(Parada origen, Parada destino, Ruta.Peso criterio, Map<Parada, Double> peso, Map<Parada, Ruta> anterior){
        List<Ruta> path = new LinkedList<>();
        Parada current = destino;

        while (current != null && !current.equals(origen)){
            Ruta route = anterior.get(current);
            if(route == null) break;

            path.addFirst(route);
            current = route.getOrigen();
        }

        return new ShortestPath(path, criterio, peso.get(destino));
    }
}
