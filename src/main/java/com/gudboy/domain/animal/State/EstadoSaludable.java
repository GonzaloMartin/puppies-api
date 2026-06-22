package com.gudboy.domain.animal.State;

import com.gudboy.domain.animal.model.Animal;

public class EstadoSaludable implements IEstadoDeSalud {

    private Animal animal;

    public EstadoSaludable(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void Saludable() {
        // ya está disponible, no hay transición que hacer
    }

    @Override
    public void PonerEnTratamiento() {
        animal.setEstadoDeSalud(new EstadoEnTratamiento(animal));
    }
}
