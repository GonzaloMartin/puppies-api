package com.gudboy.domain.animal.State;
import com.gudboy.domain.animal.model.AnimalDomestico;

public class EstadoDisponible implements IEstadoAdopcion {

    private AnimalDomestico animalDomestico;

    public EstadoDisponible(AnimalDomestico animalDomestico) {
        this.animalDomestico = animalDomestico;
    }

    @Override
    public void Disponibilizar() {
        // ya está disponible, no hay transición que hacer
    }

    @Override
    public void Adoptar() {
        if (!animalDomestico.esAdoptable()) {
            throw new IllegalStateException("Este animal no puede ser adoptado.");
        }
        animalDomestico.setEstadoAdopcion(new EstadoAdoptado(animalDomestico));
    }
    
}
