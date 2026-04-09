package com.rutas.redtransporte.servicios;

import com.rutas.redtransporte.controllers.CrearRutaController;
import com.rutas.redtransporte.db.RutaDAO;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.GrafoVisual;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Resultado;
import com.rutas.redtransporte.utilidad.Visual;


/* Esta clase se encarga se hacer el enlace con las tres capas del programa:
   visual, lógica, almacenamiento

   Actualiza los datos cuando se crea, modifica o elimina.
    */
public class RutaService {
    GrafoVisual grafoVisual = ClaseService.getInstance().getClase(GrafoVisual.class);

    public Resultado verDetalles(Ruta ruta){
        if(Grafo.getInstance().getListRutas().isEmpty()) return Resultado.VACIO;

        Visual.Ventana<CrearRutaController> ventana = Visual.prepararVentana("CrearRuta.fxml", "Estilo.css");
        if (ventana == null) return Resultado.ERROR;

        ventana.controller.loadDatos(ruta);
        ventana.stage.showAndWait();

        return Resultado.EXITO;
    }

    public Resultado guardar(Ruta ruta){
        Resultado existe = Grafo.getInstance().rutaExiste(ruta,null);
        if(existe != Resultado.NO_EXISTE)
            return existe;

        RutaDAO.getInstance().guardarRuta(ruta);
        Grafo.getInstance().addRoute(ruta);
        grafoVisual.crearRuta(ruta);
        return Resultado.EXITO;
    }

    /*
    Una vez se modifica la ruta en el grafo,
    las demás funciones solo necesitan la ruta original.
     */
    public Resultado modificar(Ruta rutaOriginal, Ruta rutaModificada) {
        Resultado resultado = Grafo.getInstance().modifyRoute(rutaOriginal, rutaModificada);

        if(resultado != Resultado.EXITO)
            return resultado;

        grafoVisual.modificarRuta(rutaOriginal);
        RutaDAO.getInstance().actualizarRuta(rutaOriginal);

        return Resultado.EXITO;
    }

    public void eliminar(Ruta ruta){
        Grafo.getInstance().deleteRoute(ruta);
        grafoVisual.eliminarRuta(ruta);
        RutaDAO.getInstance().eliminarRuta(ruta.getIdRuta());
    }
}
