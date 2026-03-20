package com.rutas.redtransporte.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        return nombreParada.toLowerCase().equals(nombre);
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

    public void addRutaSalida(Ruta route){
        if(route != null){
            rutasDeSalida.add(route);
        }
    }

    public void removeRutaSalida(Ruta route){
        rutasDeSalida.remove(route);
    }

    public void addRutaEntrada(Ruta route){
        if(route != null){
            rutasDeEntrada.add(route);
        }
    }

    public void removeRutaEntrada(Ruta route){
        rutasDeEntrada.remove(route);
    }

    @Override
    public String toString() {
        return nombreParada;
    }
}
