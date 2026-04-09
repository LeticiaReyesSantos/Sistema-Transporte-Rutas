package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;

import java.util.*;
/*
    Implementacion del algoritmo Floyd-Warshall
    Este algoritmo utiliza programacion dinamica para encontrar la ruta mas corta entre todos los nodos
    Debido a su complejidad O(v^3) es bastante pesado para calculos en tiempo real
 */
public class FloydWarshall {
    public final double infinito = Double.POSITIVE_INFINITY;
    public ShortestPath bestRoute(Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio) {
        if (graph == null || origen == null || destino == null || criterio == null) {
            throw new IllegalArgumentException("Ninguno de los parametros puede ser nulo");
        }

        Set<Parada> paradasSet = graph.getSetParadas();
        int n = paradasSet.size();

        //Mapeamos las paradas a indices numericos para bajar la complejidad a O(1) al construir la matriz
        Map<Parada, Integer> indexMap = new HashMap<>();
        int index = 0;
        for (Parada parada : paradasSet) {
            indexMap.put(parada, index++);
        }

        double [][] peso  = new double[n][n];
        Ruta[][] next = new Ruta[n][n];

        //La distancia para rutas lejanas inicializa en infinito y en caso de no encontrar forma de llegar se queda como tal
        for (int i = 0; i < n; i++) {
            Arrays.fill(peso[i], infinito);
            peso[i][i] = 0.0;
        }

        for(Parada parada: paradasSet){
            int p = indexMap.get(parada);
            for(Ruta route: graph.buscarRutasSalida(parada)){
                if(!route.isDisponibilidad()) continue;
                int q = indexMap.get(route.getDestino());

                double pesoCriterio = route.obtenerCriterio(criterio);
                if(pesoCriterio < peso[p][q]){
                    peso[p][q] = pesoCriterio;
                    next[p][q] = route;
                }
            }
        }

        //Relajacion de la matriz y el corazon del floyd-warshall
        //i= nodo origen, j = nodo destino, k = nodo intermedio entre estos
        //Se encarga de evaluar si ir directamente de origen a destino es mas caro que hacer una parada en el nodo intermedio
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) { //origen
                for (int j = 0; j < n; j++) { //destino
                    if(peso[i][k] != infinito && peso[k][j] != infinito && peso[i][k] + peso[k][j] < peso[i][j]){
                        peso[i][j] = peso[i][k] + peso[k][j];
                        next[i][j] = next[i][k];
                    }
                }

            }

        }

        int start = indexMap.get(origen);
        int end = indexMap.get(destino);
        if(peso[start][end] == infinito) return null;

        return rebuildMatrixPath(origen, destino, peso, next, indexMap, criterio);
    }

    private ShortestPath rebuildMatrixPath(Parada origen, Parada destino, double[][] peso, Ruta[][] next, Map<Parada, Integer> indexMap, Ruta.Peso criterio){
        List<Ruta> path = new LinkedList<>();
        int current = indexMap.get(origen);
        int end = indexMap.get(destino);

        while(current != end){
            Ruta pathTaken = next[current][end];
            if(pathTaken == null) return null;
            path.add(pathTaken);
            current = indexMap.get(pathTaken.getDestino());
        }
        return new ShortestPath(path, criterio, peso[indexMap.get(origen)][end]);
    }
}
