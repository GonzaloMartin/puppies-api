package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalSalvaje;
import com.gudboy.dto.AnimalDTO;

public class FabricaAnimalSalvaje implements FabricaAnimal {

    private static final String HABITAT_DEFAULT = "Desconocido";

    @Override
    public Animal crearAnimal(AnimalDTO dto) {
        String habitat = dto.getHabitatNatural() != null
                ? dto.getHabitatNatural()
                : HABITAT_DEFAULT;
        return new AnimalSalvaje(
            dto.getNombre(), dto.getEspecie(),
            dto.getAltura(), dto.getPeso(),
            dto.getEdad(),   dto.getCondicionMedica(),
            habitat
        );
    }
}
