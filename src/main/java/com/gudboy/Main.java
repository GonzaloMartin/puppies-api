package com.gudboy;

import com.gudboy.controller.AdopcionController;
import com.gudboy.controller.AnimalController;
import com.gudboy.controller.UsuarioController;
import com.gudboy.repository.AdopcionRepositoryEnMemoria;
import com.gudboy.repository.AnimalRepositoryEnMemoria;
import com.gudboy.repository.IAdopcionRepository;
import com.gudboy.repository.IAnimalRepository;
import com.gudboy.repository.IUsuarioRepository;
import com.gudboy.repository.UsuarioRepositoryEnMemoria;
import com.gudboy.service.AdopcionService;
import com.gudboy.service.AnimalService;
import com.gudboy.service.UsuarioService;
import com.gudboy.view.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        IAnimalRepository animalRepository = new AnimalRepositoryEnMemoria();
        IUsuarioRepository usuarioRepository = new UsuarioRepositoryEnMemoria();
        IAdopcionRepository adopcionRepository = new AdopcionRepositoryEnMemoria();

        AnimalService animalService = new AnimalService(animalRepository);
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        AdopcionService adopcionService = new AdopcionService(adopcionRepository);

        AnimalController animalController = new AnimalController(animalService);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        AdopcionController adopcionController = new AdopcionController(adopcionService);

        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(animalController, usuarioController, adopcionController).setVisible(true));
    }
}
