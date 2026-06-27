package com.gudboy.dto;

public class AnimalDTO {

    private final String nombre;
    private final String especie;
    private final double altura;
    private final double peso;
    private final int edad;
    private final String condicionMedica;
    private final String habitatNatural;

    public AnimalDTO(String nombre, String especie, double altura, double peso,
                     int edad, String condicionMedica) {
        this(nombre, especie, altura, peso, edad, condicionMedica, null);
    }

    public AnimalDTO(String nombre, String especie, double altura, double peso,
                     int edad, String condicionMedica, String habitatNatural) {
        this.nombre         = nombre;
        this.especie        = especie;
        this.altura         = altura;
        this.peso           = peso;
        this.edad           = edad;
        this.condicionMedica = condicionMedica;
        this.habitatNatural = habitatNatural;
    }

    public String getNombre()          { return nombre; }
    public String getEspecie()         { return especie; }
    public double getAltura()          { return altura; }
    public double getPeso()            { return peso; }
    public int    getEdad()            { return edad; }
    public String getCondicionMedica() { return condicionMedica; }
    public String getHabitatNatural()  { return habitatNatural; }
}
