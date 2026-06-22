package com.gudboy;

import com.gudboy.controller.AdopcionController;
import com.gudboy.controller.AlarmaController;
import com.gudboy.controller.AnimalController;
import com.gudboy.controller.UsuarioController;
import com.gudboy.repository.*;
import com.gudboy.service.AdopcionService;
import com.gudboy.service.AlarmaService;
import com.gudboy.service.AnimalService;
import com.gudboy.service.UsuarioService;
import com.gudboy.view.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        IAnimalRepository animalRepository = new AnimalRepositoryEnMemoria();
        IUsuarioRepository usuarioRepository = new UsuarioRepositoryEnMemoria();
        IAdopcionRepository adopcionRepository = new AdopcionRepositoryEnMemoria();
        IAlarmaRepository alarmaRepository = new AlarmaRepositoryEnMemoria();

        AnimalService animalService = new AnimalService(animalRepository);
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        AdopcionService adopcionService = new AdopcionService(adopcionRepository);
        AlarmaService alarmaService = new AlarmaService(alarmaRepository);

        AnimalController animalController = new AnimalController(animalService);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        AdopcionController adopcionController = new AdopcionController(adopcionService);
        AlarmaController alarmaController = new AlarmaController(alarmaService);

        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(animalController, usuarioController, adopcionController, alarmaController).setVisible(true));
    }
}