package com.rutas.redtransporte.modelos;

import java.util.ArrayList;
import java.util.List;

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

    public List<Ruta> getRutasDisponibles() {
        return rutasDisponibles;
    }

    public void setRutasDisponibles(List<Ruta> rutasDisponibles) {
        this.rutasDisponibles = rutasDisponibles;
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
}
