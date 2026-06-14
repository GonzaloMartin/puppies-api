package com.gudboy.domain.animal.model;

/**
 * Representa un animal doméstico (perro, gato, canario, loro, tortuga, etc.).
 * Puede ser adoptado siempre y cuando no esté bajo tratamiento médico.
 */
public class AnimalDomestico implements Animal {

    private final String nombre;
    private final String especie;         // perro, gato, canario, loro, tortuga, etc.
    private final double altura;
    private final double peso;
    private final int edad;
    private final String condicionMedica;
    private boolean enTratamiento;

    public AnimalDomestico(String nombre, String especie, double altura,
                           double peso, int edad, String condicionMedica) {
        this.nombre = nombre;
        this.especie = especie;
        this.altura = altura;
        this.peso = peso;
        this.edad = edad;
        this.condicionMedica = condicionMedica;
        this.enTratamiento = false;
    }

    /** Los animales domésticos son adoptables SÓLO si no están en tratamiento. */
    @Override
    public boolean esAdoptable() {
        return !enTratamiento;
    }

    @Override
    public String getTipoAnimal() {
        return "DOMESTICO";
    }

    // --- getters ---

    @Override public String getNombre()        { return nombre; }
    @Override public double getAltura()        { return altura; }
    @Override public double getPeso()          { return peso; }
    @Override public int    getEdad()          { return edad; }
    @Override public String getCondicionMedica() { return condicionMedica; }

    public String getEspecie()               { return especie; }
    public boolean isEnTratamiento()         { return enTratamiento; }
    public void setEnTratamiento(boolean enTratamiento) {
        this.enTratamiento = enTratamiento;
    }

    @Override
    public String toString() {
        return "AnimalDomestico{" +
                "nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", adoptable=" + esAdoptable() +
                '}';
    }
}
