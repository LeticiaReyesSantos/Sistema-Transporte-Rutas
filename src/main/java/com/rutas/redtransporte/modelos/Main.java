package com.rutas.redtransporte.modelos;
//Clase Main para hacer test

import com.rutas.redtransporte.algoritmos.Dijkstra;
import com.rutas.redtransporte.utilidad.Logico;

import javax.swing.text.Keymap;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Grafo grafo = Grafo.getInstance();

        crearDatosStatic(grafo);
        //Logico.crearDatosGrafo();

        ArrayList<Parada> paradas = new ArrayList<>(grafo.getSetParadas());

        Dijkstra buscador = new Dijkstra();
        System.out.println("Calculando ruta mas optima desde p1 a p5");
        ShortestPath trip = buscador.bestRoute(grafo,paradas.get(0),paradas.get(4),Ruta.Peso.DISTANCIA);


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

    private static void crearDatosStatic(Grafo grafo){
        Parada p1 = new Parada("A", "Bus");
        Parada p2 = new Parada("B", "Bus");
        Parada p3 = new Parada("C", "Bus");
        Parada p4 = new Parada("D", "Bus");
        Parada p5 = new Parada("E", "Bus");
        Parada p6 = new Parada("F", "Bus");


        grafo.addParada(p1);
        grafo.addParada(p2);
        grafo.addParada(p3);
        grafo.addParada(p4);
        grafo.addParada(p5);
        grafo.addParada(p6);

        Ruta first = grafo.addRoute(new Ruta("first",p1,p2, 10, 100, 4));
        Ruta second = grafo.addRoute(new Ruta("second",p1,p5, 20, 150, 8));
        Ruta third = grafo.addRoute(new Ruta("third",p3,p5, 15, 120, 2));
        Ruta fourth = grafo.addRoute(new Ruta("fourth",p3,p2, 5, 50, 4));
        Ruta fifth = grafo.addRoute(new Ruta("fifth",p4,p6, 12, 105, 5.5));
        Ruta sixth = grafo.addRoute(new Ruta("sixth",p3,p4, 11, 103, 5));
    }
}
