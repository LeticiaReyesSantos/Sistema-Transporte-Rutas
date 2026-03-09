package com.rutas.redtransporte.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* Clase: Parada
   Proposito: Modelar los componentes de las paradas
   Metodos: addRoute, removeRoute
 */

public class Parada {
    private int idParada;
    private String nombreParada;
    private String tipo;
    private List<Ruta> rutasDisponibles;

    public Parada(String nombreParada, String tipo) {
        this.nombreParada = nombreParada;
        this.tipo = tipo;
        this.rutasDisponibles = new ArrayList<>();
    }

    public int getIdParada() {
        return idParada;
    }

    public void setIdParada(int idParada) {
        this.idParada = idParada;
    }

    public String getNombreParada() {
        return nombreParada;
    }

    public void setNombreParada(String nombreParada) {
        this.nombreParada = nombreParada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parada)) return false;

        Parada parada = (Parada) o;
        String nombre = parada.getNombreParada().toLowerCase();

        return nombreParada.toLowerCase().equals(nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreParada.toLowerCase());
    }

    public List<Ruta> getRutasDisponibles() {
        return rutasDisponibles;
    }

    /* Nombre: addRoute
       Funcion: Agrega una ruta disponible para esta parada nodo/arista
       Retorno: void
     */
    public void addRoute(Ruta route){
        if(route != null){
            rutasDisponibles.add(route);
        }
    }

    /* Nombre: removeRoute
      Funcion: Eliminar la ruta como disponible para esta parada
      Retorno: void
    */
    public void removeRoute(Ruta route){
        rutasDisponibles.remove(route);
    }

    @Override
    public String toString() {
        return nombreParada;
    }
}
