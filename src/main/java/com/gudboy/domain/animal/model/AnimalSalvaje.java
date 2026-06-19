package com.gudboy.domain.animal.model;

import java.util.UUID;

public class AnimalSalvaje implements Animal {

    private final UUID id;
    private final String nombre;
    private final String especie;
    private final double altura;
    private final double peso;
    private final int edad;
    private final String condicionMedica;
    private final String habitatNatural;

    public AnimalSalvaje(String nombre, String especie, double altura,
                         double peso, int edad, String condicionMedica,
                         String habitatNatural) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.especie = especie;
        this.altura = altura;
        this.peso = peso;
        this.edad = edad;
        this.condicionMedica = condicionMedica;
        this.habitatNatural = habitatNatural;
    }

    /** Los animales salvajes NUNCA son adoptables. */
    @Override
    public boolean esAdoptable() {
        return false;
    }

    @Override
    public String getTipoAnimal() {
        return "SALVAJE";
    }

    // --- getters ---

    @Override public UUID   getId()              { return id; }
    @Override public String getNombre()          { return nombre; }
    @Override public double getAltura()          { return altura; }
    @Override public double getPeso()            { return peso; }
    @Override public int    getEdad()            { return edad; }
    @Override public String getCondicionMedica() { return condicionMedica; }

    public String getEspecie()                  { return especie; }
    public String getHabitatNatural()           { return habitatNatural; }

    @Override
    public String toString() {
        return "AnimalSalvaje{" +
                "nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", habitat='" + habitatNatural + '\'' +
                ", adoptable=false" +
                '}';
    }
}
