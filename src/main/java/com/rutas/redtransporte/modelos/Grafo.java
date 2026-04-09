package com.rutas.redtransporte.modelos;

import com.rutas.redtransporte.db.ParadaDAO;
import com.rutas.redtransporte.db.RutaDAO;
import com.rutas.redtransporte.utilidad.Resultado;

import java.util.*;

/*
    Clase principal del modelo
    Utiliza el Patron Singleton
    Maneja la relacion Parada-Ruta, centralizando la estructura de datos del mapa
 */
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

    public boolean paradaExiste(Parada parada){
        return map.keySet().stream().anyMatch(p -> p.equals(parada));
    }

    public Resultado modificarParada (Parada paradaOriginal, Parada paradaModificada){

        if(paradaOriginal.equals(paradaModificada)){
            if (paradaOriginal.cambiosParada(paradaModificada)) return Resultado.EXITO;
            else return Resultado.NO_CAMBIOS;

        }else{
            if(paradaExiste(paradaModificada)) return Resultado.EXISTE;
            else return Resultado.EXITO;
        }

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
    }

    public Ruta addRoute(Ruta ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta debe existir.");
        }

        if (ruta.getOrigen() == null || ruta.getDestino() == null) {
            throw new IllegalArgumentException("El origen o destino de la ruta debe existir.");
        }

        if(rutaExiste(ruta,null) != Resultado.NO_EXISTE){
            return null;
        }else{
            map.get(ruta.getOrigen()).add(ruta);
            ruta.getOrigen().addRutaSalida(ruta);
            ruta.getDestino().addRutaEntrada(ruta);
            allRutas.add(ruta);
        }


        return ruta;
    }


    /*
    Detemina si una para existe basado en:
        Tramo(origen-destino): EXISTE
        Nombre: NOMBRE_EXISTE:
     */
    public Resultado rutaExiste(Ruta newRuta, Ruta excluir) {
        boolean existeTramo = newRuta.getOrigen().getRutasDeSalida().stream()
                .filter(r -> r != excluir)
                .anyMatch(r -> r.equals(newRuta));


        if (existeTramo) return Resultado.EXISTE;

        boolean existeNombre = allRutas.stream()
                .filter(r -> r != excluir)
                .anyMatch(r -> r.getNombreRuta().equalsIgnoreCase(newRuta.getNombreRuta()));

        if (existeNombre) return Resultado.NOMBRE_EXISTE;

        return Resultado.NO_EXISTE;
    }

    /*
    Valida si es posible hacer la modificación.
    Si es posible, realiza los cambios necesarios.
     */
    public Resultado modifyRoute(Ruta oldRuta, Ruta newRuta) {
        if (newRuta == null) {
            throw new IllegalArgumentException("La ruta proporcionada es nula.");
        }

        Resultado validacion = verificarRutaModificada(oldRuta, newRuta);
        if (validacion != Resultado.EXITO)  return validacion;

        if (!oldRuta.getOrigen().equals(newRuta.getOrigen())) {
            map.get(oldRuta.getOrigen()).remove(oldRuta);
            oldRuta.getOrigen().getRutasDeSalida().remove(oldRuta);

            map.get(newRuta.getOrigen()).add(oldRuta);
            newRuta.getOrigen().getRutasDeSalida().add(oldRuta);
        }

        if (!oldRuta.getDestino().equals(newRuta.getDestino())) {
            oldRuta.getDestino().removeRutaEntrada(oldRuta);
            newRuta.getDestino().addRutaEntrada(oldRuta);
        }

        oldRuta.modificarRuta(newRuta);

        oldRuta.setDisponible(true);
        oldRuta.setEventoTrafico(Ruta.Evento.STANDARD);

        return Resultado.EXITO;
    }

    /*
    Verifica si la ruta puede ser modificada.
        1. Al verificar si existe, se excluye la original para evitar un falso existe.
            Existe : Devuelve el resultado.

            No existe: Verifica si tiene el mismo tramo y nombre.
                       Verifica si hay cambios.
     */
    public Resultado verificarRutaModificada(Ruta rutaOriginal, Ruta rutaModificada){

        Resultado existe = rutaExiste(rutaModificada, rutaOriginal);

        if(existe != Resultado.NO_EXISTE){
            return existe;

        }else{

            if(rutaOriginal.equals(rutaModificada)) {
                if (rutaOriginal.cambiosRuta(rutaModificada)) return Resultado.EXITO;
                else return Resultado.NO_CAMBIOS;
            }

            return Resultado.EXITO;
        }

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
        origen.removeRutaSalida(routeToDel);
        destino.removeRutaEntrada(routeToDel);
        allRutas.remove(routeToDel);
    }



    public List<Ruta> buscarRutasSalida(Parada parade){
        List<Ruta> rutas = map.get(parade);
        if (rutas != null) {
            return rutas;
        } else {

            return Collections.emptyList();
        }
    }


    /*
        Utilizado para simular alteraciones en el flujo del trafico
     */
    public void aplicarEvento(Ruta route, Ruta.Evento evento){
        route.setEventoTrafico(evento);
        switch (evento){
            case STANDARD -> {
                route.setDisponible(true);
                route.setTiempo(route.getTiempoBase());
                route.setCosto(route.getCostoBase());
            }
            case TRAFICO -> {
                route.setDisponible(true);
                route.setTiempo(route.getTiempoBase() * 1.5);
                route.setCosto(route.getCostoBase() * 1.5);
            }
            case LLUVIA -> {
                route.setDisponible(true);
                route.setTiempo(route.getTiempoBase() * 1.25);
                route.setCosto(route.getCostoBase() * 1.25);
            }
            case ACCIDENTE -> route.setDisponible(false);
            case DESCUENTO -> {
                route.setDisponible(true);
                route.setTiempo(route.getTiempoBase());
                route.setCosto(route.getCostoBase() * 0.5);
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


    public boolean esConexo() {
        if (map.isEmpty()) return true;
        List<Parada> paradasList = new ArrayList<>(map.keySet());
        Parada inicio = paradasList.getFirst();
        Set<Parada> visited = new HashSet<>();

        dfsRecursivo(inicio, visited);
        return visited.size() == map.size();
    }

    //dfs recursivo
    private void dfsRecursivo(Parada current, Set<Parada> visited) {
        visited.add(current);

        List<Ruta> rutasDeSalida = map.get(current); //pido todas las rutas que salen de la parada actual
        if (rutasDeSalida != null) {
            for (Ruta ruta : rutasDeSalida) {
                if (ruta.isDisponible()) {
                    Parada destino = ruta.getDestino(); //veo hacia que nodo me lleva esa ruta
                    if (!visited.contains(destino)) { //si no esta en el hash set
                        dfsRecursivo(destino, visited); //lo llamo para que haga lo mismo nuevamente con la nueva parada
                    }
                }
            }
        }

        for (Ruta ruta : current.getRutasDeEntrada()) {
            if (ruta.isDisponible()) {
                Parada origen = ruta.getOrigen();
                if (!visited.contains(origen)) {
                    dfsRecursivo(origen, visited);
                }
            }
        }
    }
}
