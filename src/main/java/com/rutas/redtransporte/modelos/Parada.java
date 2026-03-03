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
    private List<Ruta> rutasDeEntrada;
    private List<Ruta> rutasDeSalida;

    public Parada(String nombreParada, String tipo) {
        this.nombreParada = nombreParada;
        this.tipo = tipo;
        this.rutasDeSalida = new ArrayList<>();
        this.rutasDeEntrada = new ArrayList<>();
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

        return nombreParada.toLowerCase().equals(nombre) && tipo.equals(((Parada) o).getTipo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreParada.toLowerCase());
    }

    public List<Ruta> getRutasDeEntrada() {
        return rutasDeEntrada;
    }

    public List<Ruta> getRutasDeSalida() {
        return rutasDeSalida;
    }

    /* Nombre: addRutaSalida
           Funcion: Agrega una ruta disponible que sale de esta parada, es decir del origen al destino
           Retorno: void
         */
    public void addRutaSalida(Ruta route){
        if(route != null){
            rutasDeSalida.add(route);
        }
    }

    /* Nombre: removeRutaSalida
      Funcion: Eliminar la ruta como disponible para esta parada
      Retorno: void
    */
    public void removeRutaSalida(Ruta route){
        rutasDeSalida.remove(route);
    }

    /* Nombre: addRutaEntrada
          Funcion: Agrega una ruta que entra al destino
          Retorno: void
        */
    public void addRutaEntrada(Ruta route){
        if(route != null){
            rutasDeEntrada.add(route);
        }
    }

    /* Nombre: removeRutaEntrada
      Funcion: Eliminar la ruta como entrada para este destino
      Retorno: void
    */
    public void removeRutaEntrada(Ruta route){
        rutasDeEntrada.remove(route);
    }

    @Override
    public String toString() {
        return nombreParada;
    }
}
