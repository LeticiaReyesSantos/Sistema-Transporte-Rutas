package com.rutas.redtransporte.algoritmos;
import com.rutas.redtransporte.modelos.*;

import java.util.*;

public class Dijkstra {

    public final double infinito = Double.POSITIVE_INFINITY; //Peso inicial de todos los nodos exceptuando el primero

    //constructor
    public ShortestPath bestRoute (Grafo graph, Parada origen, Parada destino, Ruta.Peso criterio){
        if(graph == null || origen == null || destino == null || criterio == null){
            throw new IllegalArgumentException("Ninguno de los parametros puede ser nulo");
        }
        PriorityQueue<Node> cola = new PriorityQueue<>(Comparator.comparingDouble(Node::getTotalWeight)); //:: es lo mismo que ->, el IDE me lo cambio como method reference
        Map<Parada, Double> distance = new HashMap<>();
        Map<Parada, Ruta> anterior = new HashMap<>(); //guarda la ruta recolectando las paradas previas, tal como propone dijkstra
        Set<Parada> visited = new HashSet<>();

        distance.put(origen, 0.0);
        cola.add(new Node(origen, 0.0)); //se agrega la primera parada como 0, como en el video, no ha recorrido nada, lo mismo con la distancia

        while(!cola.isEmpty()){
            Node node = cola.poll(); //obtengo el primero
            Parada currentParade = node.getParada(); //obtengo la parada de ese nodo

            if(visited.contains(currentParade)){ //si ya esta en la lista de visitadas debo ignorarlo
                continue;
            }
            visited.add(currentParade); // si no, lo agrego

            //Aplicamos early return si la parada que obtuve es la misma a la que quiero llegar, no hay recorrido por hacer
            if(currentParade == destino){
                break;
            }
            //Iteramos las rutas que salen de esta parada
            for(Ruta route: graph.buscarRutasSalida(currentParade)){
               if(!route.isDisponibilidad()){ //si la ruta esta cerrada, la ignoramos
                    continue;
                }

                Parada neighbour = route.getDestino(); //Parada a la que quiero ir
                double routesWeight = route.obtenerCriterio(criterio);
                double nuevoPeso = node.getTotalWeight() + routesWeight; //Peso necesario para llegar al destino

                if(nuevoPeso < distance.getOrDefault(neighbour, infinito)){ //si no encuentra el vecino en la lista, se asigna infinito, sino me devuelve su peso, si este es mayor que el que calculamos, no es optimo
                    distance.put(neighbour, nuevoPeso); //agrego la parada y su distancia
                    anterior.put(neighbour, route); //guardo la ruta anterior a esa parada
                    cola.add(new Node(neighbour, nuevoPeso)); //guardo la optimizacion en el queue
                }
            }
        }

        Double totalDist = distance.get(destino);
        if(totalDist == null || totalDist.isInfinite()){ //si la distancia total es null o se quedo con infinito, significa que no hay ruta para llegar
            return null;
        }

        return rebuildRoute(origen, destino, criterio, distance, anterior);
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

        //Calculamos
        double cost = 0, time = 0, dist = 0;
        for (Ruta r : path) {
            cost += r.getCosto();
            time += r.getTiempo();
            dist += r.getDistancia();
        }
        //Temporal hasta que se aplique la logica de contar trasbordos
        int transfers = Math.max(0, path.size() - 1);
        //retornamos para que se use en bestRoute
        return new ShortestPath(cost, time, dist, transfers, path, criterio, distance.get(destino));
    }

}
