package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;

import java.util.*;

/*
- Genero la red minima (MST) Minimum Spanning Tree
- Devuelvo null si no es conexo, si lo es, devuelve la lista
-Prim es muy parecido a dijkstra
 */
public class Prim {
    public List<Ruta> construirRed(Grafo graph, Ruta.Peso criterio){
        if(graph == null || criterio == null) return null;

        List<Parada> paradasList = new ArrayList<>(graph.getMap().keySet());
        if (paradasList.isEmpty()) return new ArrayList<>();

        List<Ruta> spanningTree = new ArrayList<>();
        Set<Parada> visited = new HashSet<>();
        int cantParadas = paradasList.size();

        PriorityQueue<Ruta> cola = new PriorityQueue<>(Comparator.comparingDouble(r -> r.obtenerCriterio(criterio)));

        Parada start = paradasList.getFirst();
        visited.add(start);

        for(Ruta route: graph.buscarRutasSalida(start)){
            if(route.isDisponibilidad()) cola.add(route);
        }

        while(!cola.isEmpty() && visited.size() < cantParadas){
            Ruta aristaMasCorta = cola.poll(); //obtengo la ruta mas barata ya que prim es greedy, solo le interesa el mejor
            Parada destino = aristaMasCorta.getDestino();

            if(visited.contains(destino)) continue; //convierto de grafo a arbol asegurandome de que no existan ciclos

            visited.add(destino);
            spanningTree.add(aristaMasCorta);

            for(Ruta route : graph.buscarRutasSalida(destino)){
                if(!visited.contains(route.getDestino()) && route.isDisponibilidad()){ //si no la he visitado y esta disponible la agrego a mi cola como opcion
                    cola.add(route);
                }
            }
        }
        if(visited.size() < cantParadas) return null; //el grafo no es conexo
        return spanningTree;
    }
}
