package com.rutas.redtransporte.modelos;

import com.rutas.redtransporte.utilidad.Visual;

import java.util.*;

/* Clase: Grafo
   Proposito: Modelar grafos dirigidos utilizando HashMaps
   Metodos:///
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

    public List<Parada> getListParadas(){
        return map.keySet().stream().toList();
    }

    public List<Ruta> getListRutas(){return allRutas;}

    /* Nombre: addParada
       Funcion: agrega un nodo (parada) al grafo
       Retorno: void
     */
    public boolean addParada(Parada parada) {
        if (parada == null) {
            throw new IllegalArgumentException("Parada debe existir.");
        }

        if(map.containsKey(parada))
            return false;
        else
            map.put(parada, new ArrayList<>());

        return true;
    }

    /* Nombre: addRoute
      Funcion: crea una arista que une los nodos (origen/destino) y agrega la ruta a la lista de rutas disp para esa parada
      Retorno: Ruta creada
    */
    public Ruta addRoute(Ruta ruta) {
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

    /* Nombre: routeExists
      Funcion: Verifica que una ruta exista para la parada de origen.
      Retorno: boolean.
    */
    public boolean routeExists(Ruta ruta){
        boolean exists = false;

        List<Ruta> rutas = ruta.getOrigen().getRutasDeSalida();

        for (int i = 0; i < rutas.size(); i++){
            if(rutas.get(i).equals(ruta))
                return true;

        }

        return exists;
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
        origen.removeRutaSalida(routeToDel);
        destino.removeRutaEntrada(routeToDel);
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
            route.getDestino().removeRutaSalida(route); //borro las rutas de entrada de la parada antigua
            route.setDestino(nuevoDestino); //cambio a la parada nueva
            nuevoDestino.addRutaEntrada(route); //uno el destino a la ruta actual a partir del listado de rutas que entran a la parada nueva
        }

        route.setDisponibilidad(true);
        route.setActividad("Trafico usual");
    }

    /* Nombre: directNeighbours
      Funcion: identifica los vecinos directos mediante las paradas destino que puede tener la parada origen, son agregadas a una lista de vecinos
      Retorno: Lista de vecinos directos
    */
    public List<Parada> directNeighbours(Parada origen){
        List<Parada> paradasVecinas = new ArrayList<>();
        if(map.containsKey(origen)){
            List<Ruta> rutasDeSalida = map.get(origen);
            for (Ruta route: rutasDeSalida) {
                Parada destino = route.getDestino();
                if(!paradasVecinas.contains(destino)){ //reviso si el destino es agregado como vecino directo de la parada
                    paradasVecinas.add(destino);
                }
            }
        }
        return paradasVecinas;
    }

    /* Nombre: buscarRutasSalida
    Funcion: Devolver un menu de rutas que puedes tomar estando en una parada especifica
    Retorno: Lista de rutas
  */
    public List<Ruta> buscarRutasSalida(Parada parade){
        return map.get(parade);
    }

    /* Nombre: hasEdge
    Funcion: Verifica la existencia de una arista siempre y cuando exista el origen de su parada y que esta lleve al destino que buscamos
    Retorno: boolean, true or false
  */
    public boolean hasEdge(Parada origen, Parada destino){
        if(!map.containsKey(origen)){
            return false;
        }

        List<Ruta> rutasDeSalida = map.get(origen); //obtengo la lista de todas las rutas que salen de este origen
        for (Ruta routes: rutasDeSalida){
            if(routes.getDestino().equals(destino)){
                return true;
            }
        }

        return false;
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
