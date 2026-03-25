package com.rutas.redtransporte.algoritmos;

/*
    Clase usando el patron de diseno Strategy, para evitar la duplicacion del calculo en dijkstra y bellman
    modularizando el codigo y aportando a su escalabilidad
    No aplica para Floyd porque este trabaja con matrices
 */

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface EstrategiaDeRuta {

    ShortestPath bestRoute(Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio);
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
