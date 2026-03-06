package com.rutas.redtransporte.modelos;
//Clase Main para hacer test

public class Main {
    public static void main(String[] args) {

        Parada Pucmm = new Parada("PUCMM", "Bus");
        Parada SuperMercado = new Parada("El nacional", "Bus");
        Parada Hospital = new Parada("HOMS", "Bus");


        Grafo.getInstance().addParada(Pucmm);
        Grafo.getInstance().addParada(SuperMercado);
        Grafo.getInstance().addParada(Hospital);

        Ruta first = Grafo.getInstance().addRoute(new Ruta("Estrella Sadhala",Pucmm, SuperMercado,50.0,10.0,100.0));
        Ruta second = Grafo.getInstance().addRoute(new Ruta("Juan Pablo Duarte",Hospital, SuperMercado,15.0, 80.0, 200.0));
        Ruta third = Grafo.getInstance().addRoute(new Ruta("Piky Lora",Pucmm, Hospital,20.0, 100.0, 300.0));


        System.out.println("First try");
        Grafo.getInstance().show();
        System.out.println("Eliminar");
        Grafo.getInstance().deleteParade(Pucmm);
        Grafo.getInstance().show();

        System.out.println("Rutas Pucmm " + Pucmm.getRutasDisponibles());
        System.out.println("Rutas SuperMercado " + SuperMercado.getRutasDisponibles());
        System.out.println("Rutas Hospital " + Hospital.getRutasDisponibles());

        System.out.println("Al modificar");
        Grafo.getInstance().modifyRoute(second, null, null, "Reposteria", 30.0,5.0, 100.0);
        Grafo.getInstance().show();
        System.out.println("Rutas Pucmm " + Pucmm.getRutasDisponibles());
        System.out.println("Rutas SuperMercado " + SuperMercado.getRutasDisponibles());
        System.out.println("Rutas Hospital " + Hospital.getRutasDisponibles());

    }
}
