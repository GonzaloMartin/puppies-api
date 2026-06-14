package com.gudboy.repository;

import com.gudboy.domain.animal.model.Animal;
import java.util.List;

/**
 * Contrato de persistencia para animales.
 */
public interface IAnimalRepository {
    void guardar(Animal animal);
    void actualizar(Animal animal);
    List<Animal> listarTodos();
}
