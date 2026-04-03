package com.rutas.redtransporte.db;

import com.rutas.redtransporte.modelos.Parada;
import com.rutas.redtransporte.modelos.Ruta;

import java.sql.*;
import java.util.HashMap;

public class RutaDAO {
    private static RutaDAO instance = null;

    private RutaDAO() {}

    public static RutaDAO getInstance() {
        if (instance == null) {
            instance = new RutaDAO();
        }
        return instance;
    }

    //Metodo auxiliar para no repetir codigo
    private void setParametrosBase(PreparedStatement ps, Ruta ruta) throws SQLException {ps.setInt(1, ruta.getIdRuta());
        ps.setInt(1, ruta.getIdRuta());
        ps.setString(2, ruta.getNombreRuta());
        ps.setInt(3, ruta.getOrigen().getIdParada());
        ps.setInt(4, ruta.getDestino().getIdParada());
        ps.setDouble(5, ruta.getDistancia());
        ps.setDouble(6, ruta.getCostoBase());
        ps.setDouble(7, ruta.getTiempoBase());
        ps.setInt(8, ruta.getTransbordos());
    }

    public void guardarRuta(Ruta ruta) {
        final String sql = "INSERT INTO ruta (id, nombre, id_origen, id_destino, distancia, costo_base, tiempo_base, transbordos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParametrosBase(ps, ruta);
            ps.executeUpdate();

            // Obtenemos el ID que le asigno postgre
//            try (ResultSet rs = ps.getGeneratedKeys()) {
//                if (rs.next()) {
//                    ruta.setIdRuta(rs.getInt(1));
//                }
//            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la ruta: " + e.getMessage());
        }
    }

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
            ps.setInt(8, ruta.getIdRuta());

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