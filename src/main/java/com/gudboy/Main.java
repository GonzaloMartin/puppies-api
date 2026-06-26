package com.gudboy;

import com.gudboy.controller.AdopcionController;
import com.gudboy.controller.AlarmaController;
import com.gudboy.controller.AnimalController;
import com.gudboy.controller.FichaMedicaController;
import com.gudboy.controller.UsuarioController;
import com.gudboy.controller.SeguimientoController;
import com.gudboy.controller.VisitaController;
import com.gudboy.repository.*;
import com.gudboy.service.*;
import com.gudboy.view.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        try {
            IAnimalRepository animalRepository = new AnimalRepositoryMySQL();
            IUsuarioRepository usuarioRepository = new UsuarioRepositoryMySQL();
            IAdopcionRepository adopcionRepository = new AdopcionRepositoryMySQL(animalRepository, usuarioRepository);
            IAlarmaRepository alarmaRepository = new AlarmaRepositoryMySql();
            IFichaMedicaRepository fichaRepo = new FichaMedicaRepositoryMySQL(animalRepository);

            FichaMedicaService fichaService = new FichaMedicaService(fichaRepo);
            AnimalService animalService = new AnimalService(animalRepository);
            UsuarioService usuarioService = new UsuarioService(usuarioRepository);
            AdopcionService adopcionService = new AdopcionService(adopcionRepository);
            AlarmaService alarmaService = new AlarmaService(alarmaRepository, fichaService);

            // USO DE MYSQL - SEGUIMIENTO
            ISeguimientoRepository seguimientoRepository = new SeguimientoRepositoryMySQL(adopcionRepository, usuarioRepository);
            IVisitaRepository visitaRepository = new VisitaRepositoryMySQL(seguimientoRepository);

            // Servicios y Controladores de SEGUIMIENTO:
            SeguimientoService seguimientoService = new SeguimientoService(seguimientoRepository, fichaRepo);
            VisitaService visitaService = new VisitaService(visitaRepository, seguimientoRepository, fichaRepo);

            SeguimientoController seguimientoController = new SeguimientoController(seguimientoService);
            VisitaController visitaController = new VisitaController(visitaService);

            AnimalController animalController = new AnimalController(animalService);
            UsuarioController usuarioController = new UsuarioController(usuarioService);
            AdopcionController adopcionController = new AdopcionController(adopcionService);
            AlarmaController alarmaController = new AlarmaController(alarmaService);
            FichaMedicaController fichaMedicaController = new FichaMedicaController(fichaService);

            SwingUtilities.invokeLater(() ->
                    new VentanaPrincipal(animalController, usuarioController, adopcionController, alarmaController, fichaMedicaController, seguimientoController, visitaController).setVisible(true));
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Ocurrió un error al iniciar la aplicación:\n" + e.getMessage() + "\n\nVerifica tu conexión a la base de datos (MySQL).", 
                "Error de Inicio", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}