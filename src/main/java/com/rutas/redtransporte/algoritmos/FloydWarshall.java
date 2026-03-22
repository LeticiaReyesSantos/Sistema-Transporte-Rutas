package com.rutas.redtransporte.algoritmos;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;

import java.util.*;

public class FloydWarshall {
    public final double infinito = Double.POSITIVE_INFINITY;
    public ShortestPath bestRoute(Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio) {
        if (graph == null || origen == null || destino == null || criterio == null) {
            throw new IllegalArgumentException("Ninguno de los parametros puede ser nulo");
        }

        List<Parada> paradasList =  new ArrayList<>(graph.getMap().keySet()); //arreglar con un getListParadas
        int n = paradasList.size();

        //Baja la complejidad de armar la matriz a O(V + E), el hecho de usar un hace que se tarde un tiempo de O(1) para obtener los indices de las paradas
        Map<Parada, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indexMap.put(paradasList.get(i), i);
        }

        double [][] peso  = new double[n][n];
        Ruta[][] next = new Ruta[n][n];

        //Inicializo la matriz, diagonales en 0, y lo que no es ruta directa en infinito, tal como el video
        for (int i = 0; i < n; i++) {
            Arrays.fill(peso[i], infinito);
            peso[i][i] = 0.0;
        }

        for(Parada parada: paradasList){
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

        //Corazon del Floyd-Warshall O(v^3)
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) { //origen
                for (int j = 0; j < n; j++) { //destino
                    if(peso[i][k] != infinito && peso[k][j] != infinito && peso[i][k] + peso[k][j] < peso[i][j]){ //verifico si existe un camino desde mi origen hasta la escala, luego lo mismo para destino y por ultimo, el costo de ir origen -> escala -> destino es menor al que ya tenia
                        peso[i][j] = peso[i][k] + peso[k][j]; //me olvido del precio viejo ya que encontre uno mas barato
                        next[i][j] = next[i][k]; //actualizo el intermediario
                    }
                }

            }

        }

        int start = indexMap.get(origen);
        int end = indexMap.get(destino);
        if(peso[start][end] == infinito) return null; //si la ruta es inalcanzable retorno null

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
            current = indexMap.get(pathTaken.getDestino()); //mi nuevo actual es la parada del camino que tome
        }
        return new ShortestPath(path, criterio, peso[indexMap.get(origen)][end]);
    }
}
