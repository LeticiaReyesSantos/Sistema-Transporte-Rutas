package com.rutas.redtransporte.utilidad;

import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;

import javax.swing.text.rtf.RTFEditorKit;

public class Logico {

    public static void crearDatos(){
        Parada origen = new Parada("Pucmm","Carro");
        Parada destino = new Parada("Casa","Carro");

        Grafo.getInstance().addParada(origen);
        Grafo.getInstance().addParada(destino);

        Ruta rut = new Ruta("Pucmm-Casa",origen,destino,250,0.15,50);

        Grafo.getInstance().addRoute(rut);
    }
}
