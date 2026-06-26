package com.gudboy.service;

import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.repository.IAnimalRepository;

import java.util.List;

public class AnimalService {

    private final IAnimalRepository animalRepository;

    public AnimalService(IAnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal registrarAnimal(FabricaAnimal fabrica, String nombre, String especie,
                                  double altura, double peso,
                                  int edad, String condicionMedica) {
        Animal animal = fabrica.crearAnimal(nombre, especie, altura, peso, edad, condicionMedica);
        animalRepository.guardar(animal);
        return animal;
    }

    public List<Animal> listarAnimales() {
        return animalRepository.listarTodos();
    }

    public void ponerEnTratamiento(Animal animal) {
        animal.ponerEnTratamiento();
        animalRepository.actualizar(animal);
    }

    public void disponibilizar(Animal animal) {
        animal.disponibilizar();
        animalRepository.actualizar(animal);
    }
}
