package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.dto.AnimalDTO;

public interface FabricaAnimal {
    Animal crearAnimal(AnimalDTO dto);
}
