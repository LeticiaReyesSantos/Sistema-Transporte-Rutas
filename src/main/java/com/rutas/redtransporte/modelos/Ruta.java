package com.rutas.redtransporte.modelos;

/* Clase: Ruta
   Proposito: Modelar los componentes de las rutas
   Metodos:///
 */

import java.util.Objects;

public class Ruta {

    public enum Peso{
        TRANSBORDO, COSTO, TIEMPO, DISTANCIA
    }

    public enum Evento{
        TRAFICO, ACCIDENTE, LLUVIA, STANDARD, DESCUENTO
    }

    private static int genIDRuta = 1;
    private int idRuta;
    private String nombreRuta;
    private boolean disponibilidad;
    private double tiempo;
    private double costo;
    private double tiempoBase;
    private double costoBase;
    private double distancia;
    private int transbordos;
    private Evento eventoTrafico;
    private Parada origen;
    private Parada destino;

    public Ruta(String nombreRuta, Parada origen, Parada destino, double costo, double tiempo, double distancia) {
        idRuta = genIDRuta++;
        this.nombreRuta = nombreRuta;
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
        this.tiempo = tiempo;
        this.distancia = distancia;
        this.costoBase = costo;
        this.tiempoBase = tiempo;

        //Valores iniciales al crear una nueva ruta
        this.transbordos = 0;
        this.disponibilidad = true;
        this.eventoTrafico = Evento.STANDARD;
    }

    public Ruta(Ruta ruta) {
        this.idRuta = ruta.getIdRuta();
        this.nombreRuta = ruta.getNombreRuta();
        this.origen = ruta.getOrigen();
        this.destino = ruta.getDestino();
        this.costo = ruta.getCosto();
        this.tiempo = ruta.getTiempo();
        this.distancia = ruta.getDistancia();
        this.costoBase = ruta.getCostoBase();
        this.tiempoBase = ruta.getTiempoBase();
        this.transbordos = ruta.getTransbordos();
        this.disponibilidad = ruta.isDisponibilidad();
        this.eventoTrafico = ruta.getEventoTrafico();
    }

    public static void setGenIDRuta(int lastId){
        if(lastId == 0)
            genIDRuta = 1;
        else
            genIDRuta = lastId;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        idRuta = genIDRuta++;
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

    public double getTiempoBase() { return tiempoBase;}

    public double getCostoBase() { return costoBase;}

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

    public Evento getEventoTrafico() { return eventoTrafico;}

    public void setEventoTrafico(Evento eventoTrafico) { this.eventoTrafico = eventoTrafico;}

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
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Ruta)) return false;

        return equalsDatos((Ruta)o);
    }

    private boolean equalsDatos(Ruta comparar){
        boolean nombre = nombreRuta.equalsIgnoreCase(comparar.nombreRuta);
        boolean origen = this.origen.equals(comparar.getOrigen());
        boolean destino = this.destino.equals(comparar.getDestino());

        return nombre || (origen && destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRuta);
    }

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
            default -> throw new IllegalArgumentException("Ponderacion desconocida");

        }
    }

    @Override
    public String toString() {
        return distancia + " km\n" + tiempo + " h\n" + "$ " + costo;
    }
}
