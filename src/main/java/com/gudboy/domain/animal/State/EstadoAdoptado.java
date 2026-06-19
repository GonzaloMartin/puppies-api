package com.gudboy.domain.animal.State;

import com.gudboy.domain.animal.model.AnimalDomestico;

public class EstadoAdoptado implements IEstadoAdopcion {

    private AnimalDomestico animalDomestico
    ;
    public EstadoAdoptado(AnimalDomestico animalDomestico) {
        this.animalDomestico = animalDomestico;
    }
    @Override
    public void Disponibilizar() {
        animalDomestico.setEstadoAdopcion(new EstadoDisponible(animalDomestico));
    }

    @Override
    public void Adoptar() {
        throw new IllegalStateException("Este animal ya fue adoptado.");
    }
    
}
