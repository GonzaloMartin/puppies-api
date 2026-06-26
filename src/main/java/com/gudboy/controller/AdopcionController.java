package com.gudboy.controller;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.service.AdopcionService;

import java.util.List;

public class AdopcionController {

    private final AdopcionService adopcionService;

    public AdopcionController(AdopcionService adopcionService) {
        this.adopcionService = adopcionService;
    }

    public void registrarAdopcion(AnimalDomestico animal1, AnimalDomestico animal2, Visitador adoptante, Veterinario responsable) {
        adopcionService.RegistrarAdopcion(animal1, animal2, adoptante, responsable);
    }

    public List<Adopcion> listarTodos() {
        return adopcionService.listarAdopciones();
    }
}
