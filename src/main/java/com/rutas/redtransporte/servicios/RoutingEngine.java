package com.rutas.redtransporte.servicios;

import com.rutas.redtransporte.algoritmos.*;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;

/*  Patron de diseno: Facade
    Esta clase representa la logica de negocio, se creo con el proposito de tomar decisiones y enviarlas ya procesadas a lo que es
    la parte visual, ya que esta no deberia saber nada de la parte matematica del programa
 */
public class RoutingEngine {
    private final Dijkstra dijkstra;
    private final BellmanFord bellmanFord;
    private final Grafo graph;
    private final FloydWarshall floydWarshall;

    public RoutingEngine() {
        this.graph = Grafo.getInstance();

        this.dijkstra = new Dijkstra();
        this.bellmanFord = new BellmanFord();
        this.floydWarshall = new FloydWarshall();
    }

    public ShortestPath optimizedPath(Parada origen, Parada destino, Ruta.Peso criterio){
        EstrategiaDeRuta engine;

        //Seleccion dinamica del algoritmo segun el criterio de evaluacion
        if(criterio == Ruta.Peso.COSTO){
            engine = bellmanFord;
        } else
            engine = dijkstra;

        return engine.bestRoute(graph, origen, destino, criterio);
    }

    public ShortestPath floydOptimizedPath(Parada origen, Parada destino, Ruta.Peso criterio){
        return floydWarshall.bestRoute(graph, origen, destino, criterio);
    }

    /*
        Segunda mejor ruta o ruta alternativa
        Se aprovecha la existencia del atributo "disponibilidad" en la clase RUTA, evitando asi
        la creacion de un nuevo algoritmo complejo como el algoritmo de Yen.
        Inhabilita la primera arista de la mejor ruta para forzar un desvio en el calculo, luego se
        vuelve a habilitar
     */

    public ShortestPath alternativePath(Parada origen, Parada destino, Ruta.Peso criterio){
        ShortestPath bestRoute = optimizedPath(origen, destino, criterio);
        //si no hay ruta principal no deberia haber alternativa
        if(bestRoute == null || bestRoute.getRutasRecorridas() == null || bestRoute.getRutasRecorridas().isEmpty())
            return null;

        Ruta disabledRoute = bestRoute.getRutasRecorridas().getFirst();
        boolean originalState = disabledRoute.isDisponible();
        disabledRoute.setDisponible(false);

        ShortestPath alternative = optimizedPath(origen, destino, criterio);
        disabledRoute.setDisponible(originalState);

        return alternative;
    }

    public boolean esGrafoConexo(){
        return !graph.esConexo();
    }

}
