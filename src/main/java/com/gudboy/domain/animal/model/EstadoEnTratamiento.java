package com.gudboy.domain.animal.model;

public class EstadoEnTratamiento implements IEstadoDeSalud {

    private final Animal animal;

    public EstadoEnTratamiento(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void Disponibilizar() {
        animal.setEstadoDeSalud(new EstadoDisponible(animal));
    }

    @Override
    public void PonerEnTratamiento() {
        // ya está en tratamiento, no hay transición que hacer
    }

    @Override
    public void Adoptar() {
        throw new IllegalStateException("El animal está en tratamiento médico y no puede ser adoptado.");
    }

}
