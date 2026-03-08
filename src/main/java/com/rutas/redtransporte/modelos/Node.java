package com.rutas.redtransporte.modelos;

/* Nombre: Node
   Objetivo: Wrapper para la clase Parada que almacena el peso (transbordo, tiempo, distancia, costo) de la misma
   desde el nodo origen, para no alterar los datos de mi clase.
   Metodos: getParada(), getTotalWeight(), setParada
 */

public class Node {
    private Parada parada;
    private double totalWeight;

    public Node(Parada parada, double totalWeight) {
        this.parada = parada;
        this.totalWeight = totalWeight;
    }

    public Parada getParada() {
        return parada;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setParada(Parada parada){
        this.parada = parada;
    }
}
