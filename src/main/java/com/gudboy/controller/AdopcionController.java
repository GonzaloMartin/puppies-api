package com.gudboy.controller;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.service.AdopcionService;

public class AdopcionController {

    private final AdopcionService adopcionService;

    public AdopcionController(AdopcionService adopcionService) {
        this.adopcionService = adopcionService;
    }

    public void registrarAdopcion(Animal animal1, Animal animal2, Visitador adoptante, Veterinario responsable) {
        adopcionService.RegistrarAdopcion(animal1, animal2, adoptante, responsable);
    }
}
