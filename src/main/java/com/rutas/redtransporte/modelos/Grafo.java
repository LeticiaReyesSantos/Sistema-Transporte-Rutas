package com.rutas.redtransporte.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Clase: Grafo
   Proposito: Modelar grafos dirigidos utilizando HashMaps
   Metodos:///
 */

public class Grafo {
    private Map<Parada, List<Ruta>> map;


    public Grafo() {
        map = new HashMap<>();
    }

    public Map<Parada, List<Ruta>> getMap() {
        return map;
    }

    /* Nombre: addParada
       Funcion: agrega un nodo (parada) al grafo
       Retorno: void
     */
    public void addParada(Parada parada) {
        if (parada == null) {
            throw new IllegalArgumentException("Parada debe existir.");
        }
        map.putIfAbsent(parada, new ArrayList<>());
    }

    /* Nombre: addRoute
      Funcion: crea una arista que une los nodos (origen/destino) y agrega la ruta a la lista de rutas disp para esa parada
      Retorno: Ruta creada
    */
    public Ruta addRoute(Parada origen, Parada destino, String nombre, double tiempo, double costo, double distancia) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("El origen o destino de la ruta debe existir.");
        }

        map.putIfAbsent(origen, new ArrayList<>());
        map.putIfAbsent(destino, new ArrayList<>());
        Ruta newRoute = new Ruta(nombre, costo, tiempo, distancia, origen, destino);

        map.get(origen).add(newRoute);
        origen.addRoute(newRoute);

        return newRoute;
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
        origen.removeRoute(routeToDel);
        destino.removeRoute(routeToDel);
    }

    /* Nombre: deleteParade
      Funcion: Se encarga de eliminar una parada, eliminando sus conexiones (las rutas que llevan a esta) y luego eliminandola del map para no dejar conexion
      Retorno: void
    */
    public void deleteParade(Parada parada){
        if(parada == null){
            throw new IllegalArgumentException("La parada proporcionada es nula");
        }

        while(!parada.getRutasDisponibles().isEmpty()){
            deleteRoute(parada.getRutasDisponibles().getFirst()); //por la logica de queue, la lista reduce su tam, por lo que la pos 0 va cambiando
        }

        if(map.containsKey(parada)){
            while(!map.get(parada).isEmpty()){
                deleteRoute(map.get(parada).getFirst());
            }
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
            route.getDestino().removeRoute(route); //borro las rutas de entrada de la parada antigua
            route.setDestino(nuevoDestino); //cambio a la parada nueva
            nuevoDestino.addRoute(route); //uno el destino a la ruta actual a partir del listado de rutas que entran a la parada nueva
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

    //for debugging only
    public void show(){
        for(Parada parade : map.keySet()){
            System.out.println(parade + "--> " + map.get(parade));
        }
    }
}
