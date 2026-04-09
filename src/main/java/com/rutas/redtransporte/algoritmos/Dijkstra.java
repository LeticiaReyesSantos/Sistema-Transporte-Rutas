package com.rutas.redtransporte.algoritmos;
import com.rutas.redtransporte.modelos.*;

import java.util.*;

/*
    Implementacion algoritmo de dijkstra, bajo el patron Strategy
    Calcula el camino mas corto de un grafo ponderado sin pesos negativos
 */
public class Dijkstra implements EstrategiaDeRuta{

    public final double infinito = Double.POSITIVE_INFINITY;

    public ShortestPath bestRoute (Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio){
        if(graph == null || origen == null || destino == null || criterio == null){
            throw new IllegalArgumentException("Ninguno de los parametros puede ser nulo");
        }

        //Se utiliza una cola de prioridad para garantizar que el que se procese primero sea siempre
        //el nodo con menor costo acumulado, optimizando su rendimiento O(E log V)
        PriorityQueue<Node> cola = new PriorityQueue<>(Comparator.comparingDouble(Node::getTotalWeight));
        Map<Parada, Double> peso = new HashMap<>();
        Map<Parada, Ruta> anterior = new HashMap<>();
        Set<Parada> visited = new HashSet<>();

        peso.put(origen, 0.0);
        cola.add(new Node(origen, 0.0));

        while(!cola.isEmpty()){
            Node node = cola.poll();
            Parada currentParade = node.getParada();

            //Evita procesar la misma parada si ya se encontro un camino
            if(visited.contains(currentParade)){
                continue;
            }
            visited.add(currentParade);

            //Early return si llegamos al destino
            if(currentParade == destino){
                break;
            }

            for(Ruta route: graph.buscarRutasSalida(currentParade)){
                //Omito las rutas no disponibles por eventos en la logica de negocio
               if(!route.isDisponible()){
                    continue;
                }

                Parada neighbour = route.getDestino();
                double routesWeight = route.obtenerCriterio(criterio);
                double nuevoPeso = node.getTotalWeight() + routesWeight;

                //Relajacion de arista: se actualiza cuando encuentra el camino mas barato
                if(nuevoPeso < peso.getOrDefault(neighbour, infinito)){
                    peso.put(neighbour, nuevoPeso);
                    anterior.put(neighbour, route);
                    cola.add(new Node(neighbour, nuevoPeso));
                }
            }
        }

        Double totalDist = peso.get(destino);
        if(totalDist == null || totalDist.isInfinite()){
            return null;
        }

        return rebuildRoute(origen, destino, criterio, peso, anterior);
    }
}
