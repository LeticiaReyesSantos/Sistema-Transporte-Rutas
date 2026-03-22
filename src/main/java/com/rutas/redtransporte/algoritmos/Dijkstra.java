package com.rutas.redtransporte.algoritmos;
import com.rutas.redtransporte.modelos.*;

import java.util.*;

public class Dijkstra{

    public final double infinito = Double.POSITIVE_INFINITY; //Peso inicial de todos los nodos exceptuando el primero

   //RESETEAR VALORES PARA LA SIMULACION DE EVENTOS
    public ShortestPath bestRoute (Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio){
        if(graph == null || origen == null || destino == null || criterio == null){
            throw new IllegalArgumentException("Ninguno de los parametros puede ser nulo");
        }
        PriorityQueue<Node> cola = new PriorityQueue<>(Comparator.comparingDouble(Node::getTotalWeight)); //:: es lo mismo que ->, el IDE me lo cambio como method reference
        Map<Parada, Double> peso = new HashMap<>();
        Map<Parada, Ruta> anterior = new HashMap<>(); //guarda la ruta recolectando las paradas previas, tal como propone dijkstra
        Set<Parada> visited = new HashSet<>();

        peso.put(origen, 0.0);
        cola.add(new Node(origen, 0.0)); //se agrega la primera parada como 0, como en el video, no ha recorrido nada, lo mismo con la distancia

        while(!cola.isEmpty()){
            Node node = cola.poll();
            Parada currentParade = node.getParada();

            if(visited.contains(currentParade)){
                continue;
            }
            visited.add(currentParade);

            if(currentParade == destino){
                break;
            }
            for(Ruta route: graph.buscarRutasSalida(currentParade)){
               if(!route.isDisponibilidad()){
                    continue;
                }

                Parada neighbour = route.getDestino();
                double routesWeight = route.obtenerCriterio(criterio);
                double nuevoPeso = node.getTotalWeight() + routesWeight;

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

    private ShortestPath rebuildRoute(Parada origen, Parada destino, Ruta.Peso criterio, Map<Parada, Double> distance, Map<Parada, Ruta> anterior){
        List<Ruta> path = new LinkedList<>(); //linked list por su facilidad para agregar elementos
        Parada current = destino; //me posiciono en el destino

        while (current != null && !current.equals(origen)){ //retrocede hasta que llegue al origen o hasta que sea null
            Ruta route = anterior.get(current); //obtengo la ruta que use para llegar
            if(route == null){
                break;
            }
            path.addFirst(route); //es importante usar addfirst porque estamos recorriendo al reves, si usara add normal quedara la lista invertida
            current = route.getOrigen(); //obtengo de donde salio la ruta, y la guardo en current, se repite el ciclo
        }
        return new ShortestPath(path, criterio, distance.get(destino));
    }


}
