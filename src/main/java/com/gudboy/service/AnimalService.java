package com.gudboy.service;

import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.dto.AnimalDTO;
import com.gudboy.repository.IAnimalRepository;

import java.util.List;

public class AnimalService {

    private final IAnimalRepository animalRepository;

    public AnimalService(IAnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal registrarAnimal(FabricaAnimal fabrica, AnimalDTO dto) {
        Animal animal = fabrica.crearAnimal(dto);
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
