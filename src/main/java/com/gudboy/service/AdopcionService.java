package com.gudboy.service;

import java.util.List;

import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.repository.IAdopcionRepository;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.Adopcion;

public class AdopcionService {

    private final IAdopcionRepository adopcionRepository;

    public AdopcionService(IAdopcionRepository adopcionRepository) {
        this.adopcionRepository = adopcionRepository;
    }

    public void RegistrarAdopcion(AnimalDomestico animal1, AnimalDomestico animal2, Visitador adoptante, Veterinario responsable) {
        animal1.adoptar();
        if (animal2 != null) {
            animal2.adoptar();
        }
        Adopcion adopcion = new Adopcion(animal1, animal2, adoptante, responsable);
        GuardarAdopcion(adopcion);
    }

    public void GuardarAdopcion(Adopcion adopcion) {
        adopcionRepository.guardar(adopcion);
    }
    
    public List<Adopcion> listarAdopciones() {
        return adopcionRepository.listarTodos();
    }
}


