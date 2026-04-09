package com.rutas.redtransporte.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Clase utilitaria para la gestion de conexiones en PostgreSQL
public class DataBaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("No se pudo conectar a la base de datos.");
            throw e;
        }
    }
}
