package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;

/**
 * Fábrica concreta de animales domésticos (perro, gato, canario, loro, etc.).
 *
 * Garantiza que todo animal creado por esta fábrica:
 *  - Sea una instancia de {@link AnimalDomestico}.
 *  - Implemente correctamente {@code esAdoptable()}, que devuelve {@code true}
 *    por defecto (puede cambiar si el animal entra en tratamiento médico).
 *
 * Patrón: Concrete Factory (parte del Abstract Factory)
 */
public class FabricaAnimalDomestico implements FabricaAnimal {

    /**
     * Crea un {@link AnimalDomestico} con los datos de su ficha técnica.
     * El animal se crea sin tratamiento médico activo (adoptable por defecto).
     */
    @Override
    public Animal crearAnimal(String nombre, String especie,
                              double altura, double peso,
                              int edad, String condicionMedica) {
        return new AnimalDomestico(nombre, especie, altura, peso, edad, condicionMedica);
    }
}
