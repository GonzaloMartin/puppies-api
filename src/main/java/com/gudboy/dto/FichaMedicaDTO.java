package com.gudboy.dto;

import java.util.UUID;

public class FichaMedicaDTO {

    private final UUID   id;
    private final String nombreAnimal;
    private final String tipoAnimal;
    private final String especie;
    private final double peso;
    private final float  altura;
    private final int    edad;
    private final int    cantTratamientos;
    private final int    cantComentarios;
    private final int    cantVisitas;

    public FichaMedicaDTO(UUID id, String nombreAnimal, String tipoAnimal, String especie,
                          double peso, float altura, int edad,
                          int cantTratamientos, int cantComentarios, int cantVisitas) {
        this.id               = id;
        this.nombreAnimal     = nombreAnimal;
        this.tipoAnimal       = tipoAnimal;
        this.especie          = especie;
        this.peso             = peso;
        this.altura           = altura;
        this.edad             = edad;
        this.cantTratamientos = cantTratamientos;
        this.cantComentarios  = cantComentarios;
        this.cantVisitas      = cantVisitas;
    }

    public String resumen() {
        return "Ficha[" + id + "] " + nombreAnimal + " | " + tipoAnimal
             + " | peso=" + peso + "kg | altura=" + altura + "m | edad=" + edad + " años";
    }

    public UUID   getId()              { return id; }
    public String getNombreAnimal()    { return nombreAnimal; }
    public String getTipoAnimal()      { return tipoAnimal; }
    public String getEspecie()         { return especie; }
    public double getPeso()            { return peso; }
    public float  getAltura()          { return altura; }
    public int    getEdad()            { return edad; }
    public int    getCantTratamientos(){ return cantTratamientos; }
    public int    getCantComentarios() { return cantComentarios; }
    public int    getCantVisitas()     { return cantVisitas; }
}
