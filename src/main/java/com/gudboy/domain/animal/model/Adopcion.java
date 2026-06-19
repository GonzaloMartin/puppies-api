package com.gudboy.domain.animal.model;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;

public class Adopcion {
    private Animal animal;
    private Visitador adoptante;
    private Veterinario responsable;

    public Adopcion(Animal animal, Visitador adoptante, Veterinario responsable) {
        this.animal = animal;
        this.adoptante = adoptante;
        this.responsable = responsable;
    }

    public void ModificarResposable (Veterinario nuevoResponsable) {
        this.responsable = nuevoResponsable;
    }

    public Animal getAnimal() {
        return animal;
    }
    public Visitador getAdoptante() {
        return adoptante;
    }
    public Veterinario getResponsable() {
        return responsable;
    }
}
