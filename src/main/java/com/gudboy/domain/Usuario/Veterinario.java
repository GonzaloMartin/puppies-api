package com.gudboy.domain.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VETERINARIO")
public class Veterinario extends Usuario {

    @Column(name = "matricula_profesional")
    private int matriculaProfesional;

    @Column(name = "especialidad")
    private String especialidad;
    
    public Veterinario(String nombre, String apellido, String email,
                       String telefono, int matriculaProfesional, String especialidad) {
        super(nombre, apellido, email, telefono);
        this.matriculaProfesional = matriculaProfesional;
        this.especialidad         = especialidad;
    }

    public Veterinario() { super(); }

    public int    getMatriculaProfesional() { return matriculaProfesional; }
    public String getEspecialidad()         { return especialidad; }
    public int    getMatricula()            { return matriculaProfesional; }

    @Override
    public String toString() {
        return String.format("Dr/a. %s %s (Mat. %d — %s)",
            getNombre(), getApellido(), matriculaProfesional, especialidad);
    }
}
