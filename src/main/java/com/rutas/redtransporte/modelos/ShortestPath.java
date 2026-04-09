package com.rutas.redtransporte.modelos;

import java.util.List;

// DTO que encapsula el resultado final de los algoritmos de búsqueda y calcula el total del viaje por cada ponderacion de la arista
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

                //Se cuenta un transbordo unicamente cuando se cambia el tipo de vehiculo
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
