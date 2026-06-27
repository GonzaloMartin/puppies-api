package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.dto.AnimalDTO;

public class FabricaAnimalDomestico implements FabricaAnimal {

    @Override
    public Animal crearAnimal(AnimalDTO dto) {
        return new AnimalDomestico(
            dto.getNombre(), dto.getEspecie(),
            dto.getAltura(), dto.getPeso(),
            dto.getEdad(),   dto.getCondicionMedica()
        );
    }
}
