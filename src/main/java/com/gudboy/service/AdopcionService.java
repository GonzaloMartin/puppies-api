package com.gudboy.service;

import java.util.List;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.repository.IAdopcionRepository;
import com.gudboy.repository.IAnimalRepository;

public class AdopcionService {

    private final IAdopcionRepository adopcionRepository;
    private final IAnimalRepository animalRepository;

    public AdopcionService(IAdopcionRepository adopcionRepository,
                           IAnimalRepository animalRepository) {
        this.adopcionRepository = adopcionRepository;
        this.animalRepository   = animalRepository;
    }

    public void RegistrarAdopcion(AnimalDomestico animal1, AnimalDomestico animal2,
                                  Visitador adoptante, Veterinario responsable) {
        // Validar ANTES de mutar para evitar estado inconsistente
        if (!animal1.esAdoptable()) {
            throw new IllegalStateException("El animal " + animal1.getNombre() + " no está disponible para adopción.");
        }
        if (animal2 != null && !animal2.esAdoptable()) {
            throw new IllegalStateException("El animal " + animal2.getNombre() + " no está disponible para adopción.");
        }

        // Cambiar estado en memoria
        animal1.adoptar();
        if (animal2 != null) {
            animal2.adoptar();
        }

        try {
            // Persistir el nuevo estado de adopción en el repositorio de animales
            animalRepository.actualizar(animal1);
            if (animal2 != null) {
                animalRepository.actualizar(animal2);
            }

            // Guardar la adopción
            Adopcion adopcion = new Adopcion(animal1, animal2, adoptante, responsable);
            GuardarAdopcion(adopcion);
        } catch (RuntimeException e) {
            // Si falla el guardado de la adopción, revertimos el estado de los animales
            // (tanto en memoria como en el repositorio) para que no queden marcados como
            // "adoptados" sin un registro de Adopcion asociado. Sin este rollback, el animal
            // quedaba bloqueado para futuras adopciones aunque la UI siguiera mostrándolo
            // como disponible (porque nunca se refrescaba tras el error).
            animal1.disponibilizarAdopcion();
            animalRepository.actualizar(animal1);
            if (animal2 != null) {
                animal2.disponibilizarAdopcion();
                animalRepository.actualizar(animal2);
            }
            throw e;
        }
    }

    public void GuardarAdopcion(Adopcion adopcion) {
        adopcionRepository.guardar(adopcion);
    }

    public List<Adopcion> listarAdopciones() {
        return adopcionRepository.listarTodos();
    }
}