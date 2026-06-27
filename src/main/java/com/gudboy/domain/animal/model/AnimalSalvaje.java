package com.gudboy.domain.animal.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SALVAJE")
public class AnimalSalvaje extends Animal {

    @Column(name = "habitat_natural")
    private String habitatNatural;

    protected AnimalSalvaje() { }

    public AnimalSalvaje(String nombre, String especie, double altura,
                         double peso, int edad, String condicionMedica,
                         String habitatNatural) {
        super(nombre, especie, altura, peso, edad, condicionMedica);
        this.habitatNatural = habitatNatural;
    }

    @Override public boolean esAdoptable()  { return false; }
    @Override public String getTipoAnimal() { return "SALVAJE"; }
    public String getHabitatNatural()       { return habitatNatural; }

    @Override
    public String toString() {
        String salud = getEstadoDeSalud().getClass().getSimpleName().replace("Estado","");
        return String.format("[Salvaje] %s (%s) | %d años | %.1f kg | Hábitat: %s | Salud: %s",
            getNombre(), getEspecie(), getEdad(), getPeso(), habitatNatural, salud);
    }
}
