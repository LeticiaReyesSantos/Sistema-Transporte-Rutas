package com.rutas.redtransporte.modelos;

import java.util.List;

/* Nombre: ShortestPath
   Objetivo: DTO (Data Transfer Object), que sera utilizado para presentar el resultado definitivo del
   algoritmo utilizado, contiene la lista de rutas a recorrer, los pesos, el peso total, y el criterio por
   el que sera evaluado el camino
   Metodos: toString()
 */

public class ShortestPath {
    private double totalPrice;
    private double totalTime;
    private double totalDistance;
    private int totalTranfers;

    private List<Ruta> rutasRecorridas; //guaguas tomadas
    private Ruta.Peso criterioEvaluado; //
    private double pesoTotal;

    public ShortestPath(double totalPrice, double totalTime, double totalDistance, int totalTranfers,
                        List<Ruta> rutasRecorridas, Ruta.Peso criterioEvaluado, double pesoTotal) {
        this.totalPrice = totalPrice;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.totalTranfers = totalTranfers;
        this.rutasRecorridas = rutasRecorridas;
        this.criterioEvaluado = criterioEvaluado;
        this.pesoTotal = pesoTotal;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getTotalTranfers() {
        return totalTranfers;
    }

    public List<Ruta> getRutasRecorridas() {
        return rutasRecorridas;
    }

    public Ruta.Peso getCriterioEvaluado() {
        return criterioEvaluado;
    }

    public double getPesoTotal() {
        return pesoTotal;
    }

    public String toString(){
        return "Criterio Evaluado: " + criterioEvaluado + "\n"
                + "Costo ---> "+ totalPrice + "RD$"+ "\n"+ "Distancia ---> " + totalDistance + "\n" + "Tiempo ---> " + totalTime + "\n"
                + "Transbordos ---> " + totalTranfers + "\n"
                + "Rutas ----> " + rutasRecorridas.toString();
    }
}
