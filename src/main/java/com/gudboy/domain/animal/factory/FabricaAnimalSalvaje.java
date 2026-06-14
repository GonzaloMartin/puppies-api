package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalSalvaje;

/**
 * Fábrica concreta de animales salvajes (zorro, pingüino, halcón, etc.).
 *
 * Garantiza que todo animal creado por esta fábrica:
 *  - Sea una instancia de {@link AnimalSalvaje}.
 *  - Implemente {@code esAdoptable()} devolviendo siempre {@code false},
 *    respetando la restricción de dominio: los animales salvajes nunca se adoptan.
 *
 * Incluye un parámetro adicional de hábitat natural, propio del contexto salvaje,
 * expuesto a través de un método sobrecargado para no romper el contrato base.
 *
 * Patrón: Concrete Factory (parte del Abstract Factory)
 */
public class FabricaAnimalSalvaje implements FabricaAnimal {

    private static final String HABITAT_DEFAULT = "Desconocido";

    /**
     * Crea un {@link AnimalSalvaje} con hábitat natural por defecto.
     * Útil cuando aún no se cuenta con información sobre el hábitat.
     */
    @Override
    public Animal crearAnimal(String nombre, String especie,
                              double altura, double peso,
                              int edad, String condicionMedica) {
        return new AnimalSalvaje(nombre, especie, altura, peso,
                edad, condicionMedica, HABITAT_DEFAULT);
    }

    /**
     * Crea un {@link AnimalSalvaje} incluyendo información del hábitat natural.
     *
     * @param habitatNatural Hábitat de origen del animal (p.ej. "Bosque patagónico").
     */
    public Animal crearAnimal(String nombre, String especie,
                              double altura, double peso,
                              int edad, String condicionMedica,
                              String habitatNatural) {
        return new AnimalSalvaje(nombre, especie, altura, peso,
                edad, condicionMedica, habitatNatural);
    }
}
