package com.rutas.redtransporte.modelos;
//Clase Main para hacer test

import com.rutas.redtransporte.algoritmos.Dijkstra;

import javax.swing.text.Keymap;

public class Main {
    public static void main(String[] args) {

        Parada p1 = new Parada("A", "Bus");
        Parada p2 = new Parada("B", "Bus");
        Parada p3 = new Parada("C", "Bus");
        Parada p4 = new Parada("D", "Bus");
        Parada p5 = new Parada("E", "Bus");
        Parada p6 = new Parada("F", "Bus");

        Grafo g = Grafo.getInstance();
        g.addParada(p1);
        g.addParada(p2);
        g.addParada(p3);
        g.addParada(p4);
        g.addParada(p5);
        g.addParada(p6);

        Ruta first = g.addRoute(new Ruta("first",p1,p2, 10, 100, 4));
        Ruta second = g.addRoute(new Ruta("second",p1,p5, 20, 150, 8));
        Ruta third = g.addRoute(new Ruta("third",p3,p5, 15, 120, 2));
        Ruta fourth = g.addRoute(new Ruta("fourth",p3,p2, 5, 50, 4));
        Ruta fifth = g.addRoute(new Ruta("fifth",p4,p6, 12, 105, 5.5));
        Ruta sixth = g.addRoute(new Ruta("sixth",p3,p4, 11, 103, 5));

        Dijkstra buscador = new Dijkstra();
        System.out.println("Calculando ruta mas optima desde p1 a p5");
        ShortestPath trip = buscador.bestRoute(g,p1,p5,Ruta.Peso.DISTANCIA);

        if(trip != null){
            System.out.print("Ruta encontrada\n\n");
            System.out.print("Tiempo --> " + trip.getTotalTime());
            System.out.print("\nCosto ---> " + trip.getTotalPrice());
            System.out.print("\nDistancia ---> " + trip.getTotalDistance());
            System.out.println("\nTrasbordos ---> " + trip.getTotalTranfers());
        } else{
            System.out.println("\nNo hay rutas disponibles");
        }


    }
}
