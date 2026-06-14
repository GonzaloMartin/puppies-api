package com.gudboy.service;

import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.repository.IAnimalRepository;

/**
 * Capa de servicio para la lógica de negocio relacionada con animales.
 */
public class AnimalService {

    private final IAnimalRepository animalRepository;

    public AnimalService(IAnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    /**
     * Crea el animal a través de la fábrica y lo persiste en el repositorio.
     */
    public Animal registrarAnimal(FabricaAnimal fabrica, String nombre, String especie,
                                  double altura, double peso,
                                  int edad, String condicionMedica) {
        Animal animal = fabrica.crearAnimal(nombre, especie, altura, peso, edad, condicionMedica);
        animalRepository.guardar(animal);
        return animal;
    }
}
