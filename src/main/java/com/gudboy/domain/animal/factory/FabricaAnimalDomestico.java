package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;

public class FabricaAnimalDomestico implements FabricaAnimal {

    @Override
    public Animal crearAnimal(String nombre, String especie,
                              double altura, double peso,
                              int edad, String condicionMedica) {
        return new AnimalDomestico(nombre, especie, altura, peso, edad, condicionMedica);
    }
}
