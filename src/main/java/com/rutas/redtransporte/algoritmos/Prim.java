package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;

import java.util.*;

/*
    Implementacion algoritmo de Prim
    Se utiliza para encontrar el arbol de expansion minima, y es una buena opcion para verificar la conectividad de un
    grafo no dirigido, por lo que no esta conectado a la UI y solo se muestra su implementacion y comprension a la hora de
    buscar las rutas que conectan a todas las paradas del grafo con el costo minimo
    Al igual que dijkstra es un algoritmo greedy, por lo que hace uso del priority queue y sus implementaciones
    son similares
 */
public class Prim {
    public List<Ruta> construirRed(Grafo graph, Ruta.Peso criterio){
        if(graph == null || criterio == null) return null;

        List<Parada> paradasList = new ArrayList<>(graph.getMap().keySet());
        if (paradasList.isEmpty()) return new ArrayList<>();

        List<Ruta> spanningTree = new ArrayList<>();
        Set<Parada> visited = new HashSet<>();
        int cantParadas = paradasList.size();

        // Cola de prioridad que ordena las rutas disponibles de menor a mayor costo
        PriorityQueue<Ruta> cola = new PriorityQueue<>(Comparator.comparingDouble(r -> r.obtenerCriterio(criterio)));

        //Prim puede inicializar desde cualquier nodo, en este caso iniciamos en el primero por comodidad
        Parada start = paradasList.getFirst();
        visited.add(start);

        for(Ruta route: graph.buscarRutasSalida(start)){
            if(route.isDisponibilidad()) cola.add(route);
        }

        while(!cola.isEmpty() && visited.size() < cantParadas){
            //Obtenemos la ruta mas barata disponible en ese momento
            Ruta shortestEdge = cola.poll();
            Parada destino = shortestEdge.getDestino();

            //Evitamos ciclos si ya se visito esa parada, simplemente se ignora
            if(visited.contains(destino)) continue;

            visited.add(destino);
            spanningTree.add(shortestEdge);

            for(Ruta route : graph.buscarRutasSalida(destino)){
                if(!visited.contains(route.getDestino()) && route.isDisponibilidad()){
                    cola.add(route);
                }
            }
        }

        // Si al finalizar no pudimos conectar todas las paradas, devolvemos null
        if(visited.size() < cantParadas) return null;
        return spanningTree;
    }
}
