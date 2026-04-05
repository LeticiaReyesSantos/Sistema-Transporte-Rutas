package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.*;

import java.util.*;

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

        //Limite V-1
        for (int i = 0; i < paradasSet.size() - 1; i++) {
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

        //No deberiamos tener un ciclo negativo REVISAR
        for(Parada p : paradasSet){
            if(peso.get(p) == infinito) continue;
            for(Ruta ruta: graph.buscarRutasSalida(p)){
                double currentPrice = peso.get(p);
                double costoArista = ruta.obtenerCriterio(criterio);
                if(currentPrice + costoArista < peso.get(ruta.getDestino()))
                    throw new IllegalArgumentException("Ciclo negativo detectado");
            }
        }

        if (peso.get(destino) == infinito) {
            return null;
        }

        return rebuildRoute(origen, destino, criterio, peso, anterior);
    }
}
