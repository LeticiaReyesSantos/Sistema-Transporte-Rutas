package com.rutas.redtransporte.servicios;

import java.util.HashMap;
import java.util.Map;

/* Esta clase se encarga de manejar las instancias de las distintas clases controladoras.
   Las clases registran sus instancias llamando al método registrarClase.
   Otras clases pueden acceder a su instancia con el método getClase.
    */
public class ClaseService {
    private static ClaseService claseService;
    private final Map<Class<?>, Object> registro = new HashMap<>();

    public static ClaseService getInstance(){
        if(claseService == null)
            claseService = new ClaseService();

        return claseService;
    }

    /* Nombre: registrarClase
       Funcion: Registra la instancia de una clase.
       Retorno: void
    */
    public <T> void registrarClase(Class<T> clase, T referencia) {
        registro.put(clase, referencia);
    }

    /* Nombre: getClase
       Funcion: Devuelve la instancia de la clase solicitada.
       Retorno: (T) instancia solicitada.
    */
    public <T> T getClase(Class<T> clase) {
        Object obj = registro.get(clase);
        if (obj == null) throw new IllegalStateException("No registrado: " + clase.getSimpleName());
        return clase.cast(obj);
    }

}
