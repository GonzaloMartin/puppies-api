package com.gudboy.domain.animal.model;

/**
 * Representa un animal salvaje (zorro, pingüino, halcón, etc.).
 * Por su naturaleza, NUNCA puede ser adoptado.
 */
public class AnimalSalvaje extends Animal {

    private final String habitatNatural;

    public AnimalSalvaje(String nombre, String especie, double altura,
                         double peso, int edad, String condicionMedica,
                         String habitatNatural) {
        super(nombre, especie, altura, peso, edad, condicionMedica);
        this.habitatNatural = habitatNatural;
    }

    /** Los animales salvajes NUNCA son adoptables, sin importar su estado de salud. */
    @Override
    public boolean esAdoptable() {
        return false;
    }

    @Override
    public String getTipoAnimal() {
        return "SALVAJE";
    }

    public String getHabitatNatural() { return habitatNatural; }

    @Override
    public String toString() {
        return "AnimalSalvaje{" +
                "nombre='" + getNombre() + '\'' +
                ", especie='" + getEspecie() + '\'' +
                ", habitat='" + habitatNatural + '\'' +
                ", adoptable=false" +
                '}';
    }
}
