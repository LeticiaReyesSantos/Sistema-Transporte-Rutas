package com.rutas.redtransporte.servicios;

import com.rutas.redtransporte.controllers.CrearParadaController;
import com.rutas.redtransporte.controllers.MainController;
import com.rutas.redtransporte.db.ParadaDAO;
import com.rutas.redtransporte.modelos.Grafo;
import com.rutas.redtransporte.modelos.GrafoVisual;
import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.utilidad.Mensaje;
import com.rutas.redtransporte.utilidad.Visual;
import javafx.fxml.FXMLLoader;

/* Esta clase se encarga se hacer el enlace con las tres capas del programa
   cuando se utilizan paradas. Actualiza los datos cuando se crea, modifica o elimina
   una parada en el visual, la estructura lógica (mapa) y la base de datos.
    */
public class ParadaService {
    GrafoVisual grafoVisual = ClaseService.getInstance().getClase(GrafoVisual.class);

    public void verDetalles(Parada parada){
        FXMLLoader loader = Visual.openNewWindow("CrearParada.fxml","Estilo.css");
        CrearParadaController crearParada = loader.getController();
        crearParada.setScene(parada);
    }

    public boolean crear(Parada parada){
        if(Grafo.getInstance().paradaExiste(parada)){
            return false;
        }

        ParadaDAO.getInstance().guardarParada(parada);
        grafoVisual.crearParada(parada);

        return true;
    }

    public boolean modificar(Parada paradaOriginal, Parada paradaModificada){

         if(Grafo.getInstance().paradaExiste(paradaModificada))
            return false;


         grafoVisual.modificarParada(paradaOriginal,paradaModificada);
         paradaOriginal.modificarParada(paradaModificada);
         ParadaDAO.getInstance().actualizarParada(paradaOriginal);

        return true;
    }

    public void eliminar(Parada parada){
        Grafo.getInstance().deleteParada(parada);
        grafoVisual.eliminarParada(parada);
        ParadaDAO.getInstance().eliminarParada(parada.getIdParada());
    }
}
