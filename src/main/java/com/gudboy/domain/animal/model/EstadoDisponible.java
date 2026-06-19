package com.gudboy.domain.animal.model;

public class EstadoDisponible implements IEstadoDeSalud {

    private final Animal animal;

    public EstadoDisponible(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void Disponibilizar() {
        // ya está disponible, no hay transición que hacer
    }

    @Override
    public void PonerEnTratamiento() {
        animal.setEstadoDeSalud(new EstadoEnTratamiento(animal));
    }

    @Override
    public void Adoptar() {
        if (!animal.esAdoptable()) {
            throw new IllegalStateException("Este animal no puede ser adoptado.");
        }
    }

}
