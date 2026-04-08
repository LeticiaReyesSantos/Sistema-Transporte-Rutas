package com.rutas.redtransporte.servicios;

import com.rutas.redtransporte.controllers.CrearParadaController;
import com.rutas.redtransporte.controllers.CrearRutaController;
import com.rutas.redtransporte.db.ParadaDAO;
import com.rutas.redtransporte.db.RutaDAO;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.GrafoVisual;
import com.rutas.redtransporte.modelos.Ruta;
import com.rutas.redtransporte.utilidad.Resultado;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class RutaService {
    GrafoVisual grafoVisual = ClaseService.getInstance().getClase(GrafoVisual.class);

    public Resultado verDetalles(Ruta ruta){
        if(Grafo.getInstance().getListRutas().isEmpty()) return Resultado.NO_EXISTE;
        if(ruta == null) return Resultado.VACIO;

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

        //Grafo.getInstance().addRoute(ruta);
        RutaDAO.getInstance().guardarRuta(ruta);
        grafoVisual.crearRuta(ruta);
        return Resultado.EXITO;
    }

    public Resultado modificar(Ruta rutaOriginal, Ruta rutaModificada) {
        //Resultado resultado = Grafo.getInstance().modifyRoute(rutaOriginal, rutaModificada);
//
//        if(resultado != Resultado.EXITO)
//            return resultado;

        Resultado existe = Grafo.getInstance().rutaExiste(rutaModificada,null);
        if(existe != Resultado.NO_EXISTE)
            return existe;


        grafoVisual.modificarRuta(rutaOriginal, rutaModificada);
        RutaDAO.getInstance().actualizarRuta(rutaOriginal);

        return Resultado.EXITO;
    }

    public void eliminar(Ruta ruta){
        Grafo.getInstance().deleteRoute(ruta);
        grafoVisual.eliminarRuta(ruta);
        RutaDAO.getInstance().eliminarRuta(ruta.getIdRuta());
    }
}
