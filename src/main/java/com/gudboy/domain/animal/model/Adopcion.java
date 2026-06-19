package com.gudboy.domain.animal.model;

public class Adopcion {
    private Animal animal;
    private Usuario adoptante;
    private Usuario responsable;

    public Adopcion(Animal animal, Usuario adoptante, Usuario responsable) {
        this.animal = animal;
        this.adoptante = adoptante;
        this.responsable = responsable;
    }

    public void ModificarResposable (Usuario nuevoResponsable) {
        this.responsable = nuevoResponsable;
    }
}
