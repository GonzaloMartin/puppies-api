package com.gudboy.domain.animal.model;

import com.gudboy.domain.animal.State.EstadoAdoptado;
import com.gudboy.domain.animal.State.EstadoDisponible;
import com.gudboy.domain.animal.State.EstadoSaludable;
import com.gudboy.domain.animal.State.IEstadoAdopcion;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
@DiscriminatorValue("DOMESTICO")
public class AnimalDomestico extends Animal {

    @Column(name = "adoptado")
    private boolean adoptado;

    @Transient
    private IEstadoAdopcion estadoAdopcion;

    protected AnimalDomestico() { }

    public AnimalDomestico(String nombre, String especie, double altura,
                           double peso, int edad, String condicionMedica) {
        super(nombre, especie, altura, peso, edad, condicionMedica);
        this.estadoAdopcion = new EstadoDisponible(this);
    }

    @Override
    protected void inicializarEstados() {
        super.inicializarEstados();
        estadoAdopcion = adoptado ? new EstadoAdoptado(this) : new EstadoDisponible(this);
    }

    public void adoptar()                { estadoAdopcion.Adoptar(); }
    public void disponibilizarAdopcion() { estadoAdopcion.Disponibilizar(); }

    public void setEstadoAdopcion(IEstadoAdopcion e) {
        this.estadoAdopcion = e;
        this.adoptado       = !(e instanceof EstadoDisponible);
    }

    public IEstadoAdopcion getEstadoAdopcion() { return estadoAdopcion; }

    @Override
    public boolean esAdoptable() {
        return getEstadoDeSalud() instanceof EstadoSaludable
            && estadoAdopcion instanceof EstadoDisponible;
    }

    @Override public String getTipoAnimal() { return "DOMESTICO"; }

    @Override
    public String toString() {
        String salud  = getEstadoDeSalud().getClass().getSimpleName()
                            .replace("Estado","").replace("De","");
        String adopc  = estadoAdopcion instanceof EstadoDisponible ? "Disponible" : "Adoptado";
        return String.format("[Doméstico] %s (%s) | %d años | %.1f kg | Salud: %s | Adopción: %s",
            getNombre(), getEspecie(), getEdad(), getPeso(), salud, adopc);
    }
}
