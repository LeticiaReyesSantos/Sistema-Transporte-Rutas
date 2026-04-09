package com.rutas.redtransporte.servicios;

import com.rutas.redtransporte.controllers.CrearParadaController;
import com.rutas.redtransporte.db.ParadaDAO;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.controllers.GrafoVisual;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.utilidad.Resultado;
import com.rutas.redtransporte.utilidad.Visual;

/* Esta clase se encarga se hacer el enlace con las tres capas del programa:
   visual, lógica, persistencia

   Actualiza los datos cuando se crea, modifica o elimina.
    */
public class ParadaService {
    GrafoVisual grafoVisual;

    public ParadaService(){
        grafoVisual = ClaseService.getInstance().getClase(GrafoVisual.class);
    }

    public Resultado verDetalles(Parada parada){
        ClaseService.getInstance().registrarClase(ParadaService.class,this);

        if(Grafo.getInstance().getSetParadas().isEmpty()) return Resultado.NO_EXISTE;
        if(parada == null) return Resultado.VACIO;

        Visual.Ventana<CrearParadaController> ventana = Visual.prepararVentana("CrearParada.fxml", "Estilo.css");
        if (ventana == null) return Resultado.ERROR;

        ventana.controller.loadDatos(parada);
        ventana.stage.showAndWait();

        return Resultado.EXITO;
    }

    public Resultado guardar(Parada parada){
        if(Grafo.getInstance().paradaExiste(parada))
            return Resultado.EXISTE;


        ParadaDAO.getInstance().guardarParada(parada);
        Grafo.getInstance().addParada(parada);
        grafoVisual.crearParada(parada);
        return Resultado.EXITO;
    }

    public Resultado modificar(Parada paradaOriginal, Parada paradaModificada){
        Resultado resultado = Grafo.getInstance().modificarParada(paradaOriginal, paradaModificada);

        if(resultado != Resultado.EXITO)
            return resultado;

        paradaOriginal.modificarParada(paradaModificada);
        grafoVisual.modificarParada();
        ParadaDAO.getInstance().actualizarParada(paradaOriginal);

        return Resultado.EXITO;
    }

    public void eliminar(Parada parada){
        Grafo.getInstance().deleteParada(parada);
        grafoVisual.eliminarParada(parada);
        ParadaDAO.getInstance().eliminarParada(parada.getIdParada());
    }
}
