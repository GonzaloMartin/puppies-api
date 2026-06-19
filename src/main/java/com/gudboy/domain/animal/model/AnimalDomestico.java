package com.gudboy.domain.animal.model;

import com.gudboy.domain.animal.State.EstadoDisponible;
import com.gudboy.domain.animal.State.EstadoSaludable;
import com.gudboy.domain.animal.State.IEstadoAdopcion;

public class AnimalDomestico extends Animal {

    private IEstadoAdopcion estadoAdopcion;

    public AnimalDomestico(String nombre, String especie, double altura,
                           double peso, int edad, String condicionMedica) {
        super(nombre, especie, altura, peso, edad, condicionMedica);
        this.estadoAdopcion = new EstadoDisponible(this);
    }

    public void adoptar() {
        estadoAdopcion.Adoptar();
    }

    public void disponibilizarAdopcion() {
        estadoAdopcion.Disponibilizar();
    }

    public void setEstadoAdopcion(IEstadoAdopcion estadoAdopcion) {
        this.estadoAdopcion = estadoAdopcion;
    }

    /** Los animales domésticos son adoptables SÓLO si están sanos y no fueron adoptados ya. */
    @Override
    public boolean esAdoptable() {
        return getEstadoDeSalud() instanceof EstadoSaludable
                && estadoAdopcion instanceof EstadoDisponible;
    }

    @Override
    public String getTipoAnimal() {
        return "DOMESTICO";
    }

    @Override
    public String toString() {
        return "AnimalDomestico{" +
                "nombre='" + getNombre() + '\'' +
                ", especie='" + getEspecie() + '\'' +
                ", adoptable=" + esAdoptable() +
                '}';
    }
}
