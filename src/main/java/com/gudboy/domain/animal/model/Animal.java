package com.gudboy.domain.animal.model;

import com.gudboy.domain.animal.State.EstadoSaludable;
import com.gudboy.domain.animal.State.IEstadoDeSalud;
import java.util.UUID;

/**
 * Clase base que representa a cualquier animal del refugio.
 * Tanto animales domésticos como salvajes extienden esta clase.
 *
 * Centraliza los datos comunes y el estado de salud (patrón State), que
 * por tener un campo mutable propio de cada instancia no puede vivir en
 * una interfaz.
 */
public abstract class Animal {

    private final UUID id = UUID.randomUUID();
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
    public String getNombre()          { return nombre; }
    public String getEspecie()         { return especie; }
    public double getAltura()          { return altura; }
    public double getPeso()            { return peso; }
    public int    getEdad()            { return edad; }
}
