package com.gudboy.controller;

import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.service.AnimalService;

/**
 * Controller (capa MVC) para el registro y consulta de animales.
 * Recibe los datos del usuario / API y delega en {@link AnimalService}.
 */
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * Registra un nuevo animal usando la fábrica adecuada (doméstica o salvaje).
     *
     * @param fabrica         Fábrica a usar (FabricaAnimalDomestico / FabricaAnimalSalvaje).
     * @param nombre          Nombre del animal.
     * @param especie         Especie concreta.
     * @param altura          Altura en metros.
     * @param peso            Peso en kg.
     * @param edad            Edad aproximada.
     * @param condicionMedica Estado de salud.
     * @return                Animal registrado.
     */
    public Animal registrarAnimal(FabricaAnimal fabrica, String nombre, String especie,
                                  double altura, double peso,
                                  int edad, String condicionMedica) {
        return animalService.registrarAnimal(fabrica, nombre, especie,
                altura, peso, edad, condicionMedica);
    }
}
