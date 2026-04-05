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

    private List<Ruta> rutasRecorridas;
    private Ruta.Peso criterioEvaluado;
    private double pesoTotal;

    public ShortestPath(List<Ruta> rutasRecorridas, Ruta.Peso criterioEvaluado, double pesoTotal) {
        this.rutasRecorridas = rutasRecorridas;
        this.criterioEvaluado = criterioEvaluado;
        this.pesoTotal = pesoTotal;

        this.totalPrice = 0;
        this.totalTime = 0;
        this.totalDistance = 0;
        this.totalTranfers = 0;

        if (rutasRecorridas != null && !rutasRecorridas.isEmpty()) {
            String pastType = rutasRecorridas.getFirst().getOrigen().getTipo();

            for (Ruta r : rutasRecorridas) {
                this.totalPrice += r.getCosto();
                this.totalTime += r.getTiempo();
                this.totalDistance += r.getDistancia();

                String currentType = r.getOrigen().getTipo();
                if (!pastType.equals(currentType)) {
                    this.totalTranfers++;
                    pastType = currentType;
                }
            }
        }
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
