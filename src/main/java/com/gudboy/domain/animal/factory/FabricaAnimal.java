package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;

public interface FabricaAnimal {

    Animal crearAnimal(String nombre, String especie,
                       double altura, double peso,
                       int edad, String condicionMedica);
}
