package com.rutas.redtransporte.modelos;
//Clase Main para hacer test

import com.rutas.redtransporte.algoritmos.BellmanFord;
import com.rutas.redtransporte.algoritmos.FloydWarshall;
import com.rutas.redtransporte.algoritmos.Prim;
import com.rutas.redtransporte.db.ParadaDAO;
import com.rutas.redtransporte.db.RutaDAO;

public class Main {
    public static void main(String[] args) {

        fillDataBase();//luego de correr el main una vez comentar para no repetir datos en la bdd
        Grafo grafo = Grafo.getInstance();
        grafo.cargarDesdeDB();

        //Logico.crearDatosGrafo();

        Parada pA = grafo.getParada("A");
        Parada pC = grafo.getParada("C");

        BellmanFord bellman = new BellmanFord();
        FloydWarshall floyd = new FloydWarshall();
        Prim prim = new Prim();

        System.out.println("\n--- TRAFICO STANDARD ---\n");
        System.out.println("Bellman");
        imprimirResultado("Bellman-Ford", bellman.bestRoute(grafo, pA, pC, Ruta.Peso.COSTO));
        System.out.println("Floyd");
        imprimirResultado("Floyd Warshall", floyd.bestRoute(grafo, pA, pC, Ruta.Peso.COSTO));

        System.out.println("\n--- Simulador de eventos ---\n");
        grafo.eventSimulator();
        System.out.println("Eventos aleatorios aplicados al mapa.");

        Ruta rutaDescuento = null;
        for (Ruta r : grafo.getListRutas()) {
            if (r.getNombreRuta().equals("third (B->C)")) {
                rutaDescuento = r;
                break;
            }
        }

        //Forzo el descuento para probar ponderacion negativa
        if (rutaDescuento != null) {
            System.out.println("Se ha aplicado un descuento la ruta B->C.");
            grafo.aplicarEvento(rutaDescuento, Ruta.Evento.DESCUENTO);
            System.out.println("Nuevo costo de la ruta B->C: RD$ " + rutaDescuento.getCosto());
        }


        System.out.println("\n--- Recalculando luego de eventos ---");
        System.out.println("\nBellman:");
        imprimirResultado("Bellman-Ford", bellman.bestRoute(grafo, pA, pC, Ruta.Peso.COSTO));

        System.out.println("\nFloyd:");
        imprimirResultado("Floyd-Warshall", floyd.bestRoute(grafo, pA, pC, Ruta.Peso.COSTO));

        //Reconstruyendo la ruta con prim puedo verificar si es conexo, en este caso lo evaluamos en base a costo
        System.out.print("\nConectividad del grafo\n");
        if(prim.construirRed(grafo, Ruta.Peso.COSTO) != null)
            System.out.print("Es conexo");

    }

    private static void imprimirResultado(String buscador, ShortestPath trip) {
        if (trip != null) {
            System.out.println("  Ruta elegida  : " + trip.getRutasRecorridas());
            System.out.println("  Costo total   : RD$ " + trip.getTotalPrice());
            System.out.println("  Transbordos   : " + trip.getTotalTranfers());
        } else {
            System.out.println("  [" + buscador + "] No hay rutas disponibles.");
        }
    }
    private static void fillDataBase(){
        System.out.println("Insertando datos iniciales en PostgreSQL...");

        Parada p1 = new Parada("A", "Bus");
        Parada p2 = new Parada("B", "Carro");
        Parada p3 = new Parada("C", "Monoriel");

        ParadaDAO.getInstance().guardarParada(p1);
        ParadaDAO.getInstance().guardarParada(p2);
        ParadaDAO.getInstance().guardarParada(p3);

        Ruta first = new Ruta("first (A->C)", p1, p3, 50, 20, 10);
        Ruta second = new Ruta("second (A-> B)", p1, p2, 30, 10, 5);
        Ruta third = new Ruta("third (B->C)", p2, p3, 40, 10, 5);

        RutaDAO.getInstance().guardarRuta(first);
        RutaDAO.getInstance().guardarRuta(second);
        RutaDAO.getInstance().guardarRuta(third);

        System.out.println("Datos insertados");
    }
}
