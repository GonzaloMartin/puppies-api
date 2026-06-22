package com.gudboy.domain.animal.State;

import com.gudboy.domain.animal.model.Animal;

public class EstadoEnTratamiento implements IEstadoDeSalud {

    private Animal animal;

    public EstadoEnTratamiento(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void Saludable() {
        animal.setEstadoDeSalud(new EstadoSaludable(animal));
    }

    @Override
    public void PonerEnTratamiento() {
        // ya está en tratamiento, no hay transición que hacer
    }
}
