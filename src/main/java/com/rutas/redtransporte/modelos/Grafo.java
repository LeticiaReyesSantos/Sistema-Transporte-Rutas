package com.rutas.redtransporte.modelos;

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

    public List<Ruta> getListRutas(){return allRutas;}

    public Object addParada(Parada parada) {
        if (parada == null) {
            throw new IllegalArgumentException("Parada debe existir.");
        }

        return map.putIfAbsent(parada, new ArrayList<>());
    }

    public Parada getParada(String nombreParada){
        List<Parada> paradas = new ArrayList<>(grafo.getSetParadas());

        return paradas.stream()
                .filter(parada -> parada.getNombreParada().equals(nombreParada))
                .findFirst()
                .orElse(null);
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

        if (ruta.getOrigen() == null || ruta.getDestino() == null) { //validar si ruta == null, validar el object primero siempre
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
    }

    public void modifyRoute(Ruta route, Parada nuevoOrigen, Parada nuevoDestino, String nuevoNombre, Double nuevoPrecio, Double nuevoTiempo, Double nuevaDistancia){
        if(route == null){
            throw new IllegalArgumentException("La ruta proporcionada es nula.");
        }

        if(nuevoNombre!= null && !nuevoNombre.isBlank())
            route.setNombreRuta(nuevoNombre);
        if(nuevoPrecio != null && nuevoPrecio >=0)
            route.setCosto(nuevoPrecio);
        if(nuevaDistancia != null && nuevaDistancia >= 0)
            route.setDistancia(nuevaDistancia);
        if(nuevoTiempo != null && nuevoTiempo > 0)
            route.setTiempo(nuevoTiempo);

        if(nuevoOrigen != null && !nuevoOrigen.equals(route.getOrigen())){
            map.putIfAbsent(nuevoOrigen, new ArrayList<>());
            map.get(route.getOrigen()).remove(route); //Borro la ruta actual
            route.setOrigen(nuevoOrigen);//cambio a la parada nueva
            map.get(nuevoOrigen).add(route); //vuelvo a unir la ruta
        }

        if(nuevoDestino != null && !nuevoDestino.equals(route.getDestino())){
            map.putIfAbsent(nuevoDestino, new ArrayList<>());
            route.getDestino().removeRutaEntrada(route); //borro las rutas de entrada de la parada antigua
            route.setDestino(nuevoDestino); //cambio a la parada nueva
            nuevoDestino.addRutaEntrada(route); //uno el destino a la ruta actual a partir del listado de rutas que entran a la parada nueva
        }

        route.setDisponibilidad(true);
        route.setEventoTrafico(Ruta.Evento.STANDARD);
    }


    public List<Ruta> buscarRutasSalida(Parada parade){
        return map.get(parade);
    }


    /* Nombre: eventSimulator
       Objetivo: Simular diferentes eventualidades que cambien el flujo del trafico
       Valores -> 0 -> trafico standard, atributos standard, probabilidad 70%
       1 -> trafico -> atributos aumentan 1.5, probabilidad 10%
       2-> accidente -> se marca la ruta como no diponible, probabilidad 5%
       3-> lluvias fuertes -> atributos aumentan 1.25, probabilidad 5%
       Retorno: void
     */
    public void eventSimulator(){
        for(Ruta route: allRutas){
            Ruta.Evento eventoActual = calcularProbabilidadEvento();
            aplicarEvento(route, eventoActual);
        }
    }

    public Ruta.Evento calcularProbabilidadEvento(){
        double probabilidad = Math.random();
        if(probabilidad <= 0.10) //10% de probabilidad
            return Ruta.Evento.ACCIDENTE;
        if(probabilidad <= 0.15) //5% de probabilidad de 10 a 15
            return Ruta.Evento.LLUVIA;
        if(probabilidad <= 0.30) //15% de probabilidad de 15 a 30
            return Ruta.Evento.TRAFICO;

        return Ruta.Evento.STANDARD; //70% de probabilidad
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
            case ACCIDENTE -> {
                route.setDisponibilidad(false);
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
