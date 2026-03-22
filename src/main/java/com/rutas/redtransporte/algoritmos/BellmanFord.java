package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.*;

import java.util.*;

public class BellmanFord {
    public final double infinito = Double.POSITIVE_INFINITY;

    public ShortestPath bestRoute(Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio){
        if(graph == null || origen == null || destino == null || criterio == null){
            throw new IllegalArgumentException("Ninguno de los parametros puede ser nulo");
        }
        Map<Parada, Double> peso = new HashMap<>();
        Map<Parada, Ruta> anterior = new HashMap<>();
        List<Parada> paradasList = new ArrayList<>(graph.getMap().keySet()); //cambiar cuando pueda modificar grafo a un getParadas

        for(Parada parade: paradasList){
            peso.put(parade, infinito);
        }
        peso.put(origen, 0.0);

        //Limite V-1
        for (int i = 0; i < paradasList.size() - 1; i++) {
            boolean changed = false; //me evito hacer iteraciones de mas si luego de la segunda vuelta la ruta mas optima no cambia
            for(Parada p: paradasList){
                if(peso.get(p) == infinito)//ignoro la parada si aun no se llegar a ella
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
            if(!changed) break; //si nada cambio el algoritmo termino antes de completar los recorridos
        }

        //No deberiamos tener un ciclo negativo
        for(Parada p : paradasList){
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

    private ShortestPath rebuildRoute(Parada origen, Parada destino, Ruta.Peso criterio, Map<Parada, Double> peso, Map<Parada, Ruta> anterior){
        List<Ruta> path = new LinkedList<>(); //podemos tener esto en una clase padre para limpieza de codigo y evitar duplicar tanto la misma funcion
        Parada current = destino;

        while (current != null && !current.equals(origen)){
            Ruta route = anterior.get(current);
            if(route == null)break;
            path.addFirst(route);
            current = route.getOrigen();
        }

        return new ShortestPath(path, criterio, peso.get(destino));
    }
}
