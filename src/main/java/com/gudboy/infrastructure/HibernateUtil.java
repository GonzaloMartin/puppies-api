package com.gudboy.infrastructure;

import java.io.InputStream;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.gudboy.domain.Usuario.Usuario;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.AnimalSalvaje;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.seguimiento.model.Seguimiento;
import com.gudboy.domain.seguimiento.model.Visita;
import com.gudboy.domain.seguimiento.model.Encuesta;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties db = new Properties();
            try (InputStream is = HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                db.load(is);
            }
            return new Configuration()
                .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                .setProperty("hibernate.connection.url",      db.getProperty("db.url"))
                .setProperty("hibernate.connection.username", db.getProperty("db.user"))
                .setProperty("hibernate.connection.password", db.getProperty("db.password"))
                .setProperty("hibernate.dialect",             "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.hbm2ddl.auto",        "none")
                .setProperty("hibernate.show_sql",             "false")
                .addAnnotatedClass(Animal.class)
                .addAnnotatedClass(AnimalDomestico.class)
                .addAnnotatedClass(AnimalSalvaje.class)
                .addAnnotatedClass(FichaMedica.class)
                .addAnnotatedClass(Usuario.class)
                .addAnnotatedClass(Veterinario.class)
                .addAnnotatedClass(Visitador.class)
                .addAnnotatedClass(Adopcion.class)
                .addAnnotatedClass(Seguimiento.class)
                .addAnnotatedClass(Visita.class)
                .addAnnotatedClass(Encuesta.class)
                .buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar Hibernate", e);
        }
    }


    public static Session openSession() {
        return sessionFactory.openSession();
    }
}
