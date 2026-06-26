package com.gudboy.repository;

import com.gudboy.domain.animal.model.Animal;

import java.util.*;

public class AnimalRepositoryEnMemoria implements IAnimalRepository {

    private final Map<UUID, Animal> store = new LinkedHashMap<>();

    @Override
    public void guardar(Animal animal) { store.put(animal.getId(), animal); }

    @Override
    public void actualizar(Animal animal) { store.put(animal.getId(), animal); }

    @Override
    public Optional<Animal> buscarPorId(UUID id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public List<Animal> listarTodos() { return new ArrayList<>(store.values()); }
}
