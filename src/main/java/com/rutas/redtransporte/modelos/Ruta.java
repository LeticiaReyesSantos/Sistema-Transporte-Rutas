package com.rutas.redtransporte.modelos;

/* Clase: Ruta
   Proposito: Modelar los componentes de las rutas
   Metodos:///
 */

import java.util.Objects;

public class Ruta {

    public enum Peso{
        TRANSBORDO, COSTO, TIEMPO, DISTANCIA;
    }

    private int idRuta;
    private String nombreRuta;
    private boolean disponibilidad;
    private double tiempo;
    private double costo;
    private double distancia;
    private int transbordos;
    private String actividad; //podria ser una clase, describe si hay trafico, si no ha pasado nada, si hay mucha lluvia o si hubo un accidente
    private Parada origen;
    private Parada destino;

    public Ruta(String nombreRuta, Parada origen, Parada destino, double costo, double tiempo, double distancia) {
        this.nombreRuta = nombreRuta;
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
        this.tiempo = tiempo;
        this.distancia = distancia;

        //Valores iniciales al crear una nueva ruta
        this.transbordos = 0;
        this.disponibilidad = true;
        this.actividad = "Standard";
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getTransbordos() {
        return transbordos;
    }

    public void setTransbordos(int transbordos) {
        this.transbordos = transbordos;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Parada getOrigen() {
        return origen;
    }

    public void setOrigen(Parada origen) {
        this.origen = origen;
    }

    public Parada getDestino() {
        return destino;
    }

    public void setDestino(Parada destino) {
        this.destino = destino;
    }

    /* Nombre: obtenerCriterio
     Funcion: Devuelve un valor numerico segun la ponderacion que le pidan
     Retorno: double
   */
    public double obtenerCriterio(Peso peso){

        switch (peso){
            case COSTO -> {
                return costo;
            }
            case DISTANCIA -> {
                return distancia;
            }
            case TIEMPO -> {
                return tiempo;
            }
            case TRANSBORDO -> {
                return transbordos;
            }
            default -> {
                throw new IllegalArgumentException("Ponderacion desconocida");
            }

        }
    }

    @Override
    public String toString() {
        return nombreRuta + "-->" + destino;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Ruta)) return false;

        return equalsDatos((Ruta)o);
    }

    private boolean equalsDatos(Ruta comparar){
        boolean nombre = nombreRuta.toLowerCase().equals(comparar.nombreRuta.toLowerCase());
        boolean origen = this.origen.equals(comparar.getOrigen());
        boolean destino = this.destino.equals(comparar.getDestino());

        return nombre || (origen && destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreRuta.toLowerCase());
    }
}
