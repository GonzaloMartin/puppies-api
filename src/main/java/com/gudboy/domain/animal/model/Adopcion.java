package com.gudboy.domain.animal.model;
import java.util.ArrayList;
import java.util.List;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;

public class Adopcion {
    private List<Animal> animales;
    private Visitador adoptante;
    private Veterinario responsable;

    public Adopcion(Animal animal1, Animal animal2, Visitador adoptante, Veterinario responsable) {
        this.animales = new ArrayList<>();
        this.animales.add(animal1);
        if (animal2 != null) {
            this.animales.add(animal2);
        }
        this.adoptante = adoptante;
        this.responsable = responsable;
    }

    public void ModificarResposable (Veterinario nuevoResponsable) {
        this.responsable = nuevoResponsable;
    }

    public List<Animal> getAnimales() {
        return animales;
    }
    public Visitador getAdoptante() {
        return adoptante;
    }
    public Veterinario getResponsable() {
        return responsable;
    }
}
