package com.gudboy.controller;

import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.service.AnimalService;

import java.util.List;

public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    public Animal registrarAnimal(FabricaAnimal fabrica, String nombre, String especie,
                                  double altura, double peso,
                                  int edad, String condicionMedica) {
        return animalService.registrarAnimal(fabrica, nombre, especie,
                altura, peso, edad, condicionMedica);
    }

    public List<Animal> listarAnimales() {
        return animalService.listarAnimales();
    }

    public void ponerEnTratamiento(Animal animal) {
        animalService.ponerEnTratamiento(animal);
    }

    public void disponibilizar(Animal animal) {
        animalService.disponibilizar(animal);
    }
}
