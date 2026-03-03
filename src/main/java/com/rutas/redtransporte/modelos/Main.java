package com.rutas.redtransporte.modelos;
//Clase Main para hacer test

public class Main {
    public static void main(String[] args) {

        Parada Pucmm = new Parada("PUCMM", "Bus");
        Parada SuperMercado = new Parada("El nacional", "Bus");
        Parada Hospital = new Parada("HOMS", "Bus");

        Grafo g = Grafo.getInstance();
        g.addParada(Pucmm);
        g.addParada(SuperMercado);
        g.addParada(Hospital);

        Ruta first = g.addRoute(Pucmm, SuperMercado, "Estrella Sadhala", 10.0, 50.0, 100.0);
        Ruta second = g.addRoute(Hospital, SuperMercado, "Juan Pablo Duarte", 15.0, 80.0, 200.0);
        Ruta third = g.addRoute(Pucmm, Hospital, "Piky Lora", 20.0, 100.0, 300.0);

        System.out.println("First try");
        g.show();
        //System.out.println("Eliminar");
        //g.deleteParade(Pucmm);
        //g.show();

        System.out.println("Rutas de entrada y salida: ");
        System.out.println("Rutas Pucmm " + "Salida: "+ Pucmm.getRutasDeSalida() + "Entrada: " + Pucmm.getRutasDeEntrada());
        System.out.println("Rutas SuperMercado " + "Salida: " + SuperMercado.getRutasDeSalida()  + "Entrada: " + SuperMercado.getRutasDeEntrada());
        System.out.println("Rutas Hospital " + "Salida: " + Hospital.getRutasDeSalida() + "Entrada: " + Hospital.getRutasDeEntrada());

        System.out.println("Al modificar");
        g.modifyRoute(second, null, null, "Reposteria", 30.0,5.0, 100.0);
        g.show();
        System.out.println("Rutas Pucmm " + "Salida: "+ Pucmm.getRutasDeSalida() + "Entrada: " + Pucmm.getRutasDeEntrada());
        System.out.println("Rutas SuperMercado " + "Salida: " + SuperMercado.getRutasDeSalida()  + "Entrada: " + SuperMercado.getRutasDeEntrada());
        System.out.println("Rutas Hospital " + "Salida: " + Hospital.getRutasDeSalida() + "Entrada: " + Hospital.getRutasDeEntrada());

    }
}
