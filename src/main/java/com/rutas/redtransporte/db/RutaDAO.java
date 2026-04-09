package com.rutas.redtransporte.db;

import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;

import java.sql.*;
import java.util.HashMap;

/*
    Patron de diseno: Data Access Object
    Esta clase encapsula todas las consultas SQL para la entidad Ruta
    Trabaja en conjunto a ParadaDAO para mantener las referencias entre los nodos y sus conexiones
 */
public class RutaDAO {
    private static RutaDAO instance = null;

    private RutaDAO() {}

    public static RutaDAO getInstance() {
        if (instance == null) {
            instance = new RutaDAO();
        }
        return instance;
    }

    public void guardarRuta(Ruta ruta) {
        final String sql = "INSERT INTO ruta (nombre, id_origen, id_destino, distancia, costo_base, tiempo_base, transbordos) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParametrosBase(ps, ruta);
            ps.executeUpdate();

            //Recuperamos el primaryKey
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ruta.setIdRuta(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la ruta: " + e.getMessage());
        }
    }

    //Metodo auxiliar para aplicar el principio DRY (Dont repeat Yourself), centraliza la inyeccion de parametros compartidos
    private void setParametrosBase(PreparedStatement ps, Ruta ruta) throws SQLException {ps.setInt(1, ruta.getIdRuta());
        ps.setString(1, ruta.getNombreRuta());
        ps.setInt(2, ruta.getOrigen().getIdParada());
        ps.setInt(3, ruta.getDestino().getIdParada());
        ps.setDouble(4, ruta.getDistancia());
        ps.setDouble(5, ruta.getCostoBase());
        ps.setDouble(6, ruta.getTiempoBase());
        ps.setInt(7, ruta.getTransbordos());
    }

    /*
        Reconstruyo la ruta desde la base de datos, con paradasCargadas actuando como un diccionario en memoria
        con las paradas
     */
    public HashMap<Integer, Ruta> obtenerTodas(HashMap<Integer, Parada> paradasCargadas) {
        HashMap<Integer, Ruta> rutas = new HashMap<>();
        final String sql = "SELECT * FROM ruta";

        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int idOrigen = rs.getInt("id_origen");
                int idDestino = rs.getInt("id_destino");
                double distancia = rs.getDouble("distancia");
                double costoBase = rs.getDouble("costo_base");
                double tiempoBase = rs.getDouble("tiempo_base");
                int transbordos = rs.getInt("transbordos");

                Parada origen = paradasCargadas.get(idOrigen);
                Parada destino = paradasCargadas.get(idDestino);

                if (origen != null && destino != null) {
                    Ruta ruta = new Ruta(nombre, origen, destino, costoBase, tiempoBase, distancia);
                    ruta.setIdRuta(id);
                    ruta.setTransbordos(transbordos);
                    rutas.put(id, ruta);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las rutas: " + e.getMessage());
        }
        return rutas;
    }

    public void actualizarRuta(Ruta ruta) {
        final String sql = "UPDATE ruta SET nombre = ?, id_origen = ?, id_destino = ?, distancia = ?, costo_base = ?, tiempo_base = ?, transbordos = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            setParametrosBase(ps, ruta);
            ps.setInt(8, ruta.getIdRuta()); //Parametro 8 corresponde a WHERE

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar la ruta: " + e.getMessage());
        }
    }

    public void eliminarRuta(int idRuta) {
        final String sql = "DELETE FROM ruta WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idRuta);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar la ruta: " + e.getMessage());
        }
    }

    //Simula ON DELETE CASCADE en el codigo para asegurar que no queden aristas huerfanas
    public void eliminarRutasPorParada(int paradaId) {
        final String sql = "DELETE FROM ruta WHERE id_origen = ? OR id_destino = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, paradaId);
            ps.setInt(2, paradaId);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar rutas por parada: " + e.getMessage());
        }
    }
}