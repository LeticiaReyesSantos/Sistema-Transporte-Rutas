package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;

import javax.swing.text.rtf.RTFEditorKit;

public class Logico {

    public static void crearDatos(){
        Grafo g = Grafo.getInstance();

        Parada p1 = new Parada("A", "Bus");
        Parada p2 = new Parada("B", "Bus");
        Parada p3 = new Parada("C", "Bus");
        Parada p4 = new Parada("D", "Bus");
        Parada p5 = new Parada("E", "Bus");
        Parada p6 = new Parada("F", "Bus");
        Parada p7 = new Parada("G", "Carro");

        g.addParada(p1);
        g.addParada(p2);
        g.addParada(p3);
        g.addParada(p4);
        g.addParada(p5);
        g.addParada(p6);
        g.addParada(p7);

        Ruta first = g.addRoute(new Ruta("first",p1,p2, 10, 100, 4));
        Ruta second = g.addRoute(new Ruta("second",p1,p5, 20, 150, 8));
        Ruta third = g.addRoute(new Ruta("third",p3,p5, 15, 120, 2));
        Ruta fourth = g.addRoute(new Ruta("fourth",p3,p2, 5, 50, 4));
        Ruta fifth = g.addRoute(new Ruta("fifth",p4,p6, 12, 105, 5.5));
        Ruta sixth = g.addRoute(new Ruta("sixth",p3,p4, 11, 103, 5));
        Ruta seventh = g.addRoute(new Ruta("seventh", p1, p7, 30, 2, 4));
    }
}
