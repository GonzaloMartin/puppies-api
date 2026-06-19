package com.gudboy.domain.animal.model;

/**
 * Representa un animal doméstico (perro, gato, canario, loro, tortuga, etc.).
 * Puede ser adoptado siempre y cuando esté en estado de salud disponible
 * (no bajo tratamiento médico).
 */
public class AnimalDomestico extends Animal {

    public AnimalDomestico(String nombre, String especie, double altura,
                           double peso, int edad, String condicionMedica) {
        super(nombre, especie, altura, peso, edad, condicionMedica);
    }

    /** Los animales domésticos son adoptables SÓLO si su estado de salud es disponible. */
    @Override
    public boolean esAdoptable() {
        return getEstadoDeSalud() instanceof EstadoDisponible;
    }

    @Override
    public String getTipoAnimal() {
        return "DOMESTICO";
    }

    @Override
    public String toString() {
        return "AnimalDomestico{" +
                "nombre='" + getNombre() + '\'' +
                ", especie='" + getEspecie() + '\'' +
                ", adoptable=" + esAdoptable() +
                '}';
    }
}
