package com.rutas.redtransporte.modelos;

import java.util.*;

/* Clase: Grafo
   Proposito: Modelar grafos dirigidos utilizando HashMaps
   Metodos://///
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

    public List<Ruta> getListRutas(){return allRutas;}

    /* Nombre: addParada
       Funcion: agrega un nodo (parada) al grafo
       Retorno: void
     */
    public Object addParada(Parada parada) {
        if (parada == null) {
            throw new IllegalArgumentException("Parada debe existir.");
        }

        return map.putIfAbsent(parada, new ArrayList<>());
    }

    /* Nombre: getParada
        Funcion: Buscar parada a partir de su nombre.
        Retorno: Parada.
    */
    public Parada getParada(String nombreParada){
        List<Parada> paradas = new ArrayList<>(grafo.getSetParadas());

        return paradas.stream()
                .filter(parada -> parada.getNombreParada().equals(nombreParada))
                .findFirst()
                .orElse(null);
    }

    /* Nombre: deleteParade
    Funcion: Se encarga de eliminar una parada, eliminando sus conexiones (las rutas que llevan a esta) y luego eliminandola del map para no dejar conexion
    Retorno: void
  */
    public void deleteParade(Parada parada){
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

    /* Nombre: modifyParade
      Funcion: Modificar el nombre de una parada
      Retorno: void
    */
    public void modifyParade(Parada paradaMod, String nuevoNombre){
        if(paradaMod != null){
            paradaMod.setNombreParada(nuevoNombre);
        }
    }

    /* Nombre: addRoute
      Funcion: crea una arista que une los nodos (origen/destino) y agrega la ruta a la lista de rutas disp para esa parada
      Retorno: Ruta creada
    */
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

    /* Nombre: routeExists
      Funcion: Verifica que una ruta exista para la parada de origen.
      Retorno: boolean.
    */
    public boolean routeExists(Ruta newRuta){

        List<Ruta> rutas = newRuta.getOrigen().getRutasDeSalida();

        return rutas.stream().anyMatch(ruta -> ruta.equals(newRuta));
    }

    /* Nombre: deleteRoute
      Funcion: Eliminar la ruta tomando el origen y el destino de esta y verificando si se encuentra en el map, en caso de encontrarla
      Retorno: void
    */
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

    /* Nombre: modifyRoute
      Funcion: Modificar los datos de una ruta tales como el nombre, el precio, la distancia(ponderacion) y el tiempo. Asi como tambien modificar su parada
      origen y destino, eliminando sus conexiones y volviendo a generarlas con nuestras nuevas paradas
      Retorno: void
    */
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
        if(nuevoTiempo != null && nuevoTiempo != 0)
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
        route.setActividad("Trafico usual");
    }


    /* Nombre: buscarRutasSalida
    Funcion: Devolver un menu de rutas que puedes tomar estando en una parada especifica
    Retorno: Lista de rutas
  */
    public List<Ruta> buscarRutasSalida(Parada parade){
        return map.get(parade);
    }


    /* Nombre: eventSimulator
       Objetivo: Simular diferentes eventualidades que cambien el flujo del trafico
       Valores -> 0 -> trafico standard, atributos standard
       1 -> trafico -> atributos * 1.5
       2-> accidente -> atributos se duplican
       3-> lluvias fuertes ->
       Retorno: void
     */
    public void eventSimulator(){
        Random random = new Random();

    }

    //for debugging only
    public void show(){
        for(Parada parade : map.keySet()){
            System.out.println(parade + "--> " + map.get(parade));
        }
    }
}
