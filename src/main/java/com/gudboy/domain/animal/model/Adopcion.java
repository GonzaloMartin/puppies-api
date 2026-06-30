package com.gudboy.domain.animal.model;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "adopcion")

public class Adopcion {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    // FK de la BD (autoincremental); 0 = no persistido aún
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
    name = "adopcion_animal",
    joinColumns = @JoinColumn(name = "adopcion_id"),
    inverseJoinColumns = @JoinColumn(name = "animal_id")
    )   
    private List<AnimalDomestico> animales;

    @ManyToOne
    @JoinColumn(name = "adoptante_email", referencedColumnName = "email")
    private Visitador adoptante;

    @ManyToOne
    @JoinColumn(name = "responsable_email", referencedColumnName = "email")
    private Veterinario responsable;

    protected Adopcion() { }

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
