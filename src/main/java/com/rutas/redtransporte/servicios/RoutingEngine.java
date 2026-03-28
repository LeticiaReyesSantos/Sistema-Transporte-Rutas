package com.rutas.redtransporte.servicios;

import com.rutas.redtransporte.algoritmos.*;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.modelos.ShortestPath;

import java.util.List;

/*  Patron de diseno: Facade
    Esta clase representa la logica de negocio, se creo con el proposito de tomar decisiones y enviarlas ya procesadas a lo que es
    la parte visual, ya que esta no deberia saber nada de la parte matematica del programa
 */
public class RoutingEngine {
    private final Dijkstra dijkstra;
    private final BellmanFord bellmanFord;
    private final Grafo graph;
    private final FloydWarshall floydWarshall;
    private final Prim prim;

    public RoutingEngine() {
        this.graph = Grafo.getInstance();

        this.dijkstra = new Dijkstra();
        this.bellmanFord = new BellmanFord();
        this.floydWarshall = new FloydWarshall();
        this.prim = new Prim();
    }

    public ShortestPath optimizedPath(Parada origen, Parada destino, Ruta.Peso criterio){
        EstrategiaDeRuta engine;

        //link a lo visual, trabajar en lo visual y luego bdd
        if(criterio == Ruta.Peso.COSTO && hasNegativePricing()){
            engine = bellmanFord;
        } else
            engine = dijkstra;

        return engine.bestRoute(graph, origen, destino, criterio);
    }

    public ShortestPath floydOptimizedPath(Parada origen, Parada destino, Ruta.Peso criterio){
        return floydWarshall.bestRoute(graph, origen, destino, criterio);
    }

    /*
    Segunda mejor ruta, o ruta alternativa, lo que hace es inhabilitar la mejor ruta
    para que tenga que recalcular, de manera que se reciclan los algoritmos en vez de crear
    uno nuevo
     */

    public ShortestPath alternativePath(Parada origen, Parada destino, Ruta.Peso criterio){
        ShortestPath bestRoute = optimizedPath(origen, destino, criterio);
        //si no hay ruta principal no deberia haber alternativa
        if(bestRoute == null || bestRoute.getRutasRecorridas() == null || bestRoute.getRutasRecorridas().isEmpty())
            return null;

        Ruta disabledRoute = bestRoute.getRutasRecorridas().getFirst();
        boolean originalState = disabledRoute.isDisponibilidad();
        disabledRoute.setDisponibilidad(false); //inhabilitamos la mejor ruta

        ShortestPath alternative = optimizedPath(origen, destino, criterio);
        disabledRoute.setDisponibilidad(originalState); //restauramos la calle

        return alternative;
    }

    private boolean hasNegativePricing(){
        for(Ruta route: graph.getListRutas()){
            if(route.getCosto() < 0) return  true;
        }

        return false;
    }

    public boolean esConexo(Ruta.Peso criterio){
        return prim.construirRed(graph, criterio) != null; //si devuelve null no pudo construir
    }

    //para darle un uso a prim, puede cambiar
    public List<Ruta> getMinimumSpanningTree(Ruta.Peso criterio) {
        return prim.construirRed(graph, criterio);
    }
}
