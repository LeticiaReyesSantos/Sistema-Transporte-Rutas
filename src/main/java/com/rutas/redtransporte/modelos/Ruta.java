package com.rutas.redtransporte.modelos;

/* Clase: Ruta
   Proposito: Modelar los componentes de las rutas
   Metodos:///
 */

public class Ruta {
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

    public Ruta(String nombreRuta, double costo, double tiempo, double distancia, Parada origen, Parada destino) {
        this.nombreRuta = nombreRuta;
        this.costo = costo;
        this.tiempo = tiempo;
        this.distancia = distancia;
        this.origen = origen;
        this.destino = destino;

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

    @Override
    public String toString() {
        return nombreRuta + "-->" + destino;
    }
}
