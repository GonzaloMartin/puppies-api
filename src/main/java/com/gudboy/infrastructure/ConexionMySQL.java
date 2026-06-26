package com.gudboy.infrastructure;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Singleton de conexión a MySQL.
 * Aplica reconexión automática si la conexión se cierra (tests de larga duración).
 */
public class ConexionMySQL {

    private static ConexionMySQL instancia;
    private Connection connection;
    private Properties props;

    private ConexionMySQL() {
        props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            props.load(is);
            connect();
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con MySQL", e);
        }
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al abrir conexión MySQL", e);
        }
    }

    public static synchronized ConexionMySQL getInstancia() {
        if (instancia == null) {
            instancia = new ConexionMySQL();
        }
        return instancia;
    }

    /** Devuelve la conexión; reconecta automáticamente si fue cerrada. */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (Exception e) {
            connect();
        }
        return connection;
    }
}
