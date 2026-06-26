package com.gudboy.domain.animal.model;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;

import java.util.ArrayList;
import java.util.List;

public class Adopcion {
    private int id;    // FK de la BD (autoincremental); 0 = no persistido aún
    private List<AnimalDomestico> animales;
    private Visitador adoptante;
    private Veterinario responsable;

    public Adopcion(AnimalDomestico animal1, AnimalDomestico animal2,
                    Visitador adoptante, Veterinario responsable) {
        this.animales = new ArrayList<>();
        this.animales.add(animal1);
        if (animal2 != null) {
            this.animales.add(animal2);
        }
        this.adoptante = adoptante;
        this.responsable = responsable;
    }

    public void modificarResponsable(Veterinario nuevoResponsable) {
        this.responsable = nuevoResponsable;
    }

    // compatibilidad con nombre antiguo
    public void ModificarResposable(Veterinario nuevoResponsable) {
        modificarResponsable(nuevoResponsable);
    }

    // --- Getters / Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public List<AnimalDomestico> getAnimales() { return animales; }
    public Visitador getAdoptante() { return adoptante; }
    public Veterinario getResponsable() { return responsable; }

    @Override
    public String toString() {
        return "Adopcion{adoptante=" + adoptante + ", animales=" + animales + "}";
    }
}
