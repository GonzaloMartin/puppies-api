package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalSalvaje;

public class FabricaAnimalSalvaje implements FabricaAnimal {

    private static final String HABITAT_DEFAULT = "Desconocido";

    @Override
    public Animal crearAnimal(String nombre, String especie,
                              double altura, double peso,
                              int edad, String condicionMedica) {
        return new AnimalSalvaje(nombre, especie, altura, peso,
                edad, condicionMedica, HABITAT_DEFAULT);
    }

    public Animal crearAnimal(String nombre, String especie,
                              double altura, double peso,
                              int edad, String condicionMedica,
                              String habitatNatural) {
        return new AnimalSalvaje(nombre, especie, altura, peso,
                edad, condicionMedica, habitatNatural);
    }
}
