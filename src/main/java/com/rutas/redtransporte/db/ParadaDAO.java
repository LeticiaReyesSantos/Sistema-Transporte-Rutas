package com.rutas.redtransporte.db;

import com.rutas.redtransporte.modelos.Parada;

import java.sql.*;
import java.util.HashMap;

/*
    Patron de diseno: Data Access Object
    Esta clase encapsula todas las consultas SQL para la entidad Parada
    Separa la logica de persistencia de datos de la logica matematica del programa
 */
public class ParadaDAO {
    private static ParadaDAO instance = null;

    private ParadaDAO() {}

    public static ParadaDAO getInstance() {
        if (instance == null) {
            instance = new ParadaDAO();
        }
        return instance;
    }

    public void guardarParada(Parada parada) {
        final String sql = "INSERT INTO parada (nombre, tipo) VALUES (?, ?)";

        //Se utiliza try para garantizar que la conexion se cierre automaticamente, evitando memory leaks
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, parada.getNombreParada());
            preparedStatement.setString(2, parada.getTipo());
            preparedStatement.executeUpdate();

            //Se inyecta el id generado en la base de datos al onjeto
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    parada.setIdParada(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la parada: " + e.getMessage());
        }
    }

    //Retornamos un HashMap utilizando el ID como clave, al hacer esto la complejidad de la busqueda es O(1)
    public HashMap<Integer, Parada> obtenerParadas() {
        HashMap<Integer, Parada> paradas = new HashMap<>();
        final String sql = "SELECT * FROM parada";

        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String tipo = resultSet.getString("tipo");

                Parada parada = new Parada(nombre, tipo);
                parada.setIdParada(id);
                paradas.put(id, parada);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las paradas: " + e.getMessage());
        }
        return paradas;
    }

    public void actualizarParada(Parada parada) {
        final String sql = "UPDATE parada SET nombre = ?, tipo = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, parada.getNombreParada());
            statement.setString(2, parada.getTipo());
            statement.setInt(3, parada.getIdParada());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarParada(int id) {
        final String sql = "DELETE FROM parada WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }
    }
}