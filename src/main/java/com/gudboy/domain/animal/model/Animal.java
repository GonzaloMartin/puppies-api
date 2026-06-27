package com.gudboy.domain.animal.model;

import java.util.UUID;

import com.gudboy.domain.animal.State.EstadoEnTratamiento;
import com.gudboy.domain.animal.State.EstadoSaludable;
import com.gudboy.domain.animal.State.IEstadoDeSalud;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "animal")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_animal", discriminatorType = DiscriminatorType.STRING)
public abstract class Animal {

    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id = UUID.randomUUID();

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "especie")
    private String especie;

    @Column(name = "altura")
    private double altura;

    @Column(name = "peso")
    private double peso;

    @Column(name = "edad")
    private int edad;

    @Column(name = "condicion_medica")
    private String condicionMedica;

    @Column(name = "en_tratamiento")
    private boolean enTratamiento;

    @Transient
    private IEstadoDeSalud estadoDeSalud;

    protected Animal() { }

    protected Animal(String nombre, String especie, double altura,
                     double peso, int edad, String condicionMedica) {
        this.nombre          = nombre;
        this.especie         = especie;
        this.altura          = altura;
        this.peso            = peso;
        this.edad            = edad;
        this.condicionMedica = condicionMedica;
        this.estadoDeSalud   = new EstadoSaludable(this);
    }

    @PostLoad
    protected void inicializarEstados() {
        estadoDeSalud = enTratamiento
                ? new EstadoEnTratamiento(this)
                : new EstadoSaludable(this);
    }

    public void disponibilizar() {
        estadoDeSalud.Saludable();
        this.enTratamiento = false;
    }

    public void ponerEnTratamiento() {
        estadoDeSalud.PonerEnTratamiento();
        this.enTratamiento = true;
    }

    public void setEstadoDeSalud(IEstadoDeSalud estadoDeSalud) {
        this.estadoDeSalud  = estadoDeSalud;
        this.enTratamiento  = estadoDeSalud instanceof EstadoEnTratamiento;
    }

    public abstract boolean esAdoptable();
    public abstract String  getTipoAnimal();

    public IEstadoDeSalud getEstadoDeSalud() { return estadoDeSalud; }

    public UUID   getId()                { return id; }
    public void   setId(UUID id)         { this.id = id; }
    public String getNombre()            { return nombre; }
    public String getEspecie()           { return especie; }
    public double getAltura()            { return altura; }
    public double getPeso()              { return peso; }
    public int    getEdad()              { return edad; }
    public String getCondicionMedica()   { return condicionMedica; }
}
