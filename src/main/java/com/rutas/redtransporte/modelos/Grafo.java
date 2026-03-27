package com.rutas.redtransporte.modelos;

import com.rutas.redtransporte.db.ParadaDAO;
import com.rutas.redtransporte.db.RutaDAO;

import java.util.*;

public class Grafo {
    private Map<Parada, List<Ruta>> map;
    private static Grafo grafo;
    private List<Ruta> allRutas;

    private Grafo() {
        map = new HashMap<>();
        allRutas = new ArrayList<>();
    }

    public static Grafo getInstance(){
        if(grafo == null)
            grafo = new Grafo();
        return grafo;
    }


    public Map<Parada, List<Ruta>> getMap() {
        return map;
    }

    public Set<Parada> getSetParadas(){
        return map.keySet();
    }

    public Parada getParada(String nombreParada){

        return map.keySet().stream()
                .filter(parada -> parada.getNombreParada().equals(nombreParada))
                .findFirst()
                .orElse(null);

    }

    public List<Ruta> getListRutas(){return allRutas;}

    public Ruta getRuta(Ruta ruta){

        return map.get(ruta.getOrigen()).stream()
                .filter(rutaEncontrada -> rutaEncontrada.getNombreRuta().equals(ruta.getNombreRuta()))
                .findFirst()
                .orElse(null);
    }

    public Object addParada(Parada parada) {
        if (parada == null) {
            throw new IllegalArgumentException("Parada debe existir.");
        }

        return map.putIfAbsent(parada, new ArrayList<>());
    }

    public void deleteParada(Parada parada){
        if(parada == null){
            throw new IllegalArgumentException("La parada proporcionada es nula");
        }

        List<Ruta> rutas = new ArrayList<>(parada.getRutasDeSalida());
        rutas.addAll(parada.getRutasDeEntrada());

        for (Ruta ruta : rutas) {
            deleteRoute(ruta);
        }

        map.remove(parada);
        ParadaDAO.getInstance().eliminarParada(parada.getIdParada());
    }

    public void modifyParade(Parada paradaMod, String nuevoNombre){
        if(paradaMod != null){
            paradaMod.setNombreParada(nuevoNombre);
        }
    }

    public Ruta addRoute(Ruta ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta debe existir.");
        }

        if (ruta.getOrigen() == null || ruta.getDestino() == null) {
            throw new IllegalArgumentException("El origen o destino de la ruta debe existir.");
        }

        if(routeExists(ruta)){
            return null;
        }else{
            map.get(ruta.getOrigen()).add(ruta);
            ruta.getOrigen().addRutaSalida(ruta);
            ruta.getDestino().addRutaEntrada(ruta);
            allRutas.add(ruta);
        }


        return ruta;
    }


    public boolean routeExists(Ruta newRuta){

        List<Ruta> rutas = newRuta.getOrigen().getRutasDeSalida();

        return rutas.stream().anyMatch(ruta -> ruta.equals(newRuta));
    }

    public void deleteRoute(Ruta routeToDel) {
        if (routeToDel == null) {
            throw new IllegalArgumentException("La ruta proporcionada es nula.");
        }

        Parada origen = routeToDel.getOrigen();
        Parada destino = routeToDel.getDestino();

        if (map.containsKey(origen)) {
            map.get(origen).remove(routeToDel);
        }
        origen.removeRutaSalida(routeToDel); //quito la referencia de mis objetos parada
        destino.removeRutaEntrada(routeToDel);
        allRutas.remove(routeToDel);
    }

    public void modifyRoute(Ruta oldRuta, Ruta newRuta){
        if(newRuta == null){
            throw new IllegalArgumentException("La ruta proporcionada es nula.");
        }

        if(newRuta.getOrigen() != null && !newRuta.getOrigen().equals(oldRuta.getOrigen())){
            map.get(oldRuta.getOrigen()).remove(newRuta);
            map.get(newRuta.getOrigen()).add(newRuta);
            newRuta.getOrigen().addRutaEntrada(newRuta);
        }

        if(newRuta.getDestino() != null && !newRuta.getDestino().equals(oldRuta.getDestino())){
            oldRuta.getDestino().removeRutaEntrada(newRuta);
            newRuta.getDestino().addRutaEntrada(newRuta);
        }

        newRuta.setDisponibilidad(true);
        newRuta.setEventoTrafico(Ruta.Evento.STANDARD);
    }

    public List<Ruta> buscarRutasSalida(Parada parade){
        return map.get(parade);
    }


    /* Nombre: eventSimulator
       Objetivo: Simular diferentes eventualidades que cambien el flujo del trafico
     */
    public void eventSimulator(){
        for(Ruta route: allRutas){
            Ruta.Evento eventoActual = calcularProbabilidadEvento();
            aplicarEvento(route, eventoActual);
        }
    }

    public Ruta.Evento calcularProbabilidadEvento(){
        double probabilidad = Math.random();
        if(probabilidad <= 0.05) //5% de probabilidad
            return Ruta.Evento.ACCIDENTE;
        if(probabilidad <= 0.10) //5% de probabilidad
            return Ruta.Evento.LLUVIA;
        if(probabilidad <= 0.25) //15% de probabilidad
            return Ruta.Evento.TRAFICO;
        if(probabilidad <= 0.35) //10% de probabilidad
            return Ruta.Evento.DESCUENTO;
        return Ruta.Evento.STANDARD; //65% de probabilidad
    }

    public void aplicarEvento(Ruta route, Ruta.Evento evento){
        route.setEventoTrafico(evento);
        switch (evento){
            case STANDARD -> {
                route.setDisponibilidad(true);
                route.setTiempo(route.getTiempoBase());
                route.setCosto(route.getCostoBase());
            }
            case TRAFICO -> {
                route.setDisponibilidad(true);
                route.setTiempo(route.getTiempoBase() * 1.5);
                route.setCosto(route.getCostoBase() * 1.5);
            }
            case LLUVIA -> {
                route.setDisponibilidad(true);
                route.setTiempo(route.getTiempoBase() * 1.25);
                route.setCosto(route.getCostoBase() * 1.25);
            }
            case ACCIDENTE -> route.setDisponibilidad(false);
            case DESCUENTO -> {
                route.setDisponibilidad(true);
                route.setTiempo(route.getTiempoBase());
                route.setCosto(route.getCostoBase() * -0.5); //Rebajamos el costo a la mitad 50% off, y se le devuelve al pasajero
            }
        }
    }

    public void cargarDesdeDB() {

        ParadaDAO paradaDAO = ParadaDAO.getInstance();
        HashMap<Integer, Parada> paradasDB = paradaDAO.obtenerParadas();
        for (Parada p : paradasDB.values()) {
            addParada(p);
        }

        RutaDAO rutaDAO = RutaDAO.getInstance();
        HashMap<Integer, Ruta> rutasDB = rutaDAO.obtenerTodas(paradasDB);

        for (Ruta r : rutasDB.values()) {
            try {
                addRoute(r);
            } catch (IllegalArgumentException e) {
                System.out.println("Ruta omitida: " + e.getMessage());
            }
        }
    }

    //for debugging only
    public void show(){
        for(Parada parade : map.keySet()){
            System.out.println(parade + "--> " + map.get(parade));
        }
    }
}
