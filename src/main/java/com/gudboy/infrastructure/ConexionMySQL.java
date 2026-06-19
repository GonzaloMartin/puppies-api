package com.gudboy.infrastructure;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConexionMySQL {

    private static ConexionMySQL instancia;
    private final Connection connection;

    private ConexionMySQL() {
        try {
            Properties props = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
            props.load(is);
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con MySQL", e);
        }
    }

    public static synchronized ConexionMySQL getInstancia() {
        if (instancia == null) {
            instancia = new ConexionMySQL();
        }
        return instancia;
    }

    public Connection getConnection() {
        return connection;
    }
}
