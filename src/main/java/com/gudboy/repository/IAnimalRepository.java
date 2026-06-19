package com.gudboy.repository;

import com.gudboy.domain.animal.model.Animal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAnimalRepository {
    void guardar(Animal animal);
    void actualizar(Animal animal);
    Optional<Animal> buscarPorId(UUID id);
    List<Animal> listarTodos();
}
