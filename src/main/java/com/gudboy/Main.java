package com.gudboy;

import com.gudboy.controller.AdopcionController;
import com.gudboy.controller.AlarmaController;
import com.gudboy.controller.AnimalController;
import com.gudboy.controller.FichaMedicaController;
import com.gudboy.controller.UsuarioController;
import com.gudboy.repository.*;
import com.gudboy.service.*;
import com.gudboy.view.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        IAnimalRepository animalRepository = new AnimalRepositoryMySQL();
        IUsuarioRepository usuarioRepository = new UsuarioRepositoryMySQL();
        IAdopcionRepository adopcionRepository = new AdopcionRepositoryMySQL(animalRepository, usuarioRepository);
        IAlarmaRepository alarmaRepository = new AlarmaRepository();
        IFichaMedicaRepository fichaRepo = new FichaMedicaRepositoryMySQL(animalRepository);
        FichaMedicaService fichaService = new FichaMedicaService(fichaRepo);

        AnimalService animalService = new AnimalService(animalRepository);
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        AdopcionService adopcionService = new AdopcionService(adopcionRepository);
        AlarmaService alarmaService = new AlarmaService(alarmaRepository, fichaService);

        // TRABAJO EN MEMORIA:
        ISeguimientoRepository seguimientoRepository = new com.gudboy.repository.SeguimientoRepositoryEnMemoria();
        IVisitaRepository visitaRepository = new com.gudboy.repository.VisitaRepositoryEnMemoria();

        // USO DE MYSQL - SEGUIMIENTO
        // ISeguimientoRepository seguimientoRepository = new com.gudboy.repository.SeguimientoRepositoryMySQL(adopcionRepository, usuarioRepository);
        // IVisitaRepository visitaRepository = new com.gudboy.repository.VisitaRepositoryEnMemoria(); // (VisitaRepositoryEnMemoria se usa como auxiliar en ciertos servicios si es necesario)

        // PARA SWING - Servicios y Controladores de SEGUIMIENTO:
        // SeguimientoService seguimientoService = new SeguimientoService(seguimientoRepository, fichaRepo);
        // VisitaService visitaService = new VisitaService(visitaRepository, seguimientoRepository, fichaRepo);
        //
        // com.gudboy.controller.SeguimientoController seguimientoController = new com.gudboy.controller.SeguimientoController(seguimientoService);
        // com.gudboy.controller.VisitaController visitaController = new com.gudboy.controller.VisitaController(visitaService);


        AnimalController animalController = new AnimalController(animalService);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        AdopcionController adopcionController = new AdopcionController(adopcionService);
        AlarmaController alarmaController = new AlarmaController(alarmaService);
        FichaMedicaController fichaMedicaController = new FichaMedicaController(fichaService);

        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(animalController, usuarioController, adopcionController, alarmaController, fichaMedicaController).setVisible(true));
    }
}