package com.gudboy.repository;

import com.gudboy.domain.animal.model.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<Animal> buscarPorId(UUID id) {
        return animales.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Animal> listarTodos() {
        return new ArrayList<>(animales);
    }
}
