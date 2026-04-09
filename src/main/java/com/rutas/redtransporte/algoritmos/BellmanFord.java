package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.*;

import java.util.*;

/*
    Implementacion algoritmo de Bellman-Ford
    Se utiliza especificamente cuando existen aristas con pesos negativos (caso descuentos en la simulacion de eventos)
    ya que Dijkstra no puede procesar estas.
 */

public class BellmanFord implements EstrategiaDeRuta{
    public final double infinito = Double.POSITIVE_INFINITY;

    public ShortestPath bestRoute(Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio){
        if(graph == null || origen == null || destino == null || criterio == null){
            throw new IllegalArgumentException("Ninguno de los parametros puede ser nulo");
        }
        Map<Parada, Double> peso = new HashMap<>();
        Map<Parada, Ruta> anterior = new HashMap<>();
        Set<Parada> paradasSet = graph.getSetParadas();

        for(Parada parade: paradasSet){
            peso.put(parade, infinito);
        }
        peso.put(origen, 0.0);

        //Relajacion de aristas -> el camino mas largo sin ciclos tiene V-1 aristas
        for (int i = 0; i < paradasSet.size() - 1; i++) {
            //Early return en caso de que no se actualice ningun peso, ya que esto sugiere que el algoritmo ya encontro la mejor ruta
            boolean changed = false;
            for(Parada p: paradasSet){
                if(peso.get(p) == infinito)
                    continue;

                for (Ruta route: graph.buscarRutasSalida(p)){
                    if(!route.isDisponibilidad())
                        continue;

                    Parada neighbour = route.getDestino();
                    double routesWeight = route.obtenerCriterio(criterio);
                    double nuevoPeso = peso.get(p)+ routesWeight;

                    if(nuevoPeso < peso.getOrDefault(neighbour, infinito)){
                        peso.put(neighbour, nuevoPeso);
                        anterior.put(neighbour, route);
                        changed = true;
                    }
                }
            }
            if(!changed) break;
        }

        //Validacion que evita un ciclo negativo en el que hacer transbordos en circulo imposibilite el calculo optimo de esta ponderacion
        for(Parada p : paradasSet){
            if(peso.get(p) == infinito) continue;
            for(Ruta ruta: graph.buscarRutasSalida(p)){
                if(!ruta.isDisponibilidad()) continue;

                double currentPrice = peso.get(p);
                double costoArista = ruta.obtenerCriterio(criterio);
                if(currentPrice + costoArista < peso.get(ruta.getDestino()))
                    return null;
            }
        }

        if (peso.get(destino) == infinito) {
            return null;
        }

        return rebuildRoute(origen, destino, criterio, peso, anterior);
    }
}
