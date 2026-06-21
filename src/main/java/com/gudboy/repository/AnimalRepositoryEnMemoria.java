package com.gudboy.repository;

import com.gudboy.domain.animal.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalRepositoryEnMemoria implements IAnimalRepository {

    private final List<Animal> animales = new ArrayList<>();

    @Override
    public void guardar(Animal animal) {
        animales.add(animal);
    }

    @Override
    public void actualizar(Animal animal) {
        if (!animales.contains(animal)) {
            animales.add(animal);
        }
    }

    @Override
    public List<Animal> listarTodos() {
        return new ArrayList<>(animales);
    }
}
