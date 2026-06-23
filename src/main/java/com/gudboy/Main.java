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

        AnimalController animalController = new AnimalController(animalService);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        AdopcionController adopcionController = new AdopcionController(adopcionService);
        AlarmaController alarmaController = new AlarmaController(alarmaService);
        FichaMedicaController fichaMedicaController = new FichaMedicaController(fichaService);

        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(animalController, usuarioController, adopcionController, alarmaController, fichaMedicaController).setVisible(true));
    }
}