package com.gudboy.dto;

import java.util.List;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.model.AnimalDomestico;


public class AdopcionDTO {
    private int id;
    private List<AnimalDomestico> animales;
    private Veterinario responsable;
    private Visitador adoptante;


    public AdopcionDTO(int id, List<AnimalDomestico> animales, Veterinario responsable, Visitador adoptante) {
        this.id = id;
        this.animales = animales;
        this.responsable = responsable;
        this.adoptante = adoptante;
    }

    public int getId() {return id;}
    public List<AnimalDomestico> getAnimales() {return animales;}
    public Veterinario getResponsable() {return responsable;}
    public Visitador getAdoptante() {return adoptante;}
}
