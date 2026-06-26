package com.gudboy.domain.animal.model;

import com.gudboy.domain.animal.State.EstadoSaludable;
import com.gudboy.domain.animal.State.IEstadoDeSalud;
import java.util.UUID;

public abstract class Animal {

    private UUID id = UUID.randomUUID();
    private final String nombre;
    private final String especie;
    private final double altura;
    private final double peso;
    private final int edad;
    private IEstadoDeSalud estadoDeSalud;

    protected Animal(String nombre, String especie, double altura,
                      double peso, int edad, String condicionMedica) {
        this.nombre = nombre;
        this.especie = especie;
        this.altura = altura;
        this.peso = peso;
        this.edad = edad;
        this.estadoDeSalud = new EstadoSaludable(this);
    }

    public void disponibilizar() { estadoDeSalud.Saludable(); }
    public void ponerEnTratamiento() { estadoDeSalud.PonerEnTratamiento(); }

    public void setEstadoDeSalud(IEstadoDeSalud estadoDeSalud) { this.estadoDeSalud = estadoDeSalud; }
    public IEstadoDeSalud getEstadoDeSalud() { return estadoDeSalud; }

    public abstract boolean esAdoptable();
    public abstract String getTipoAnimal();

    public UUID   getId()               { return id; }
    public void   setId(UUID id)        { this.id = id; }
    public String getNombre()          { return nombre; }
    public String getEspecie()         { return especie; }
    public double getAltura()          { return altura; }
    public double getPeso()            { return peso; }
    public int    getEdad()            { return edad; }
}
